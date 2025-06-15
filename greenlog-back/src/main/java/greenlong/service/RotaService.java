package greenlong.service;

import greenlong.dto.CalculoRotaResponseDTO;
import greenlong.dto.ConexaoDTO;
import greenlong.dto.RotaRequestDTO;
import greenlong.dto.RotaResponseDTO;
import greenlong.dto.CaminhoDTO;
import greenlong.dto.RotaUpdateRequestDTO;
import greenlong.mapper.RotaMapper;
import greenlong.model.Bairro;
import greenlong.model.Caminhao;
import greenlong.model.Conexao;
import greenlong.model.Rota;
import greenlong.repository.BairrosRepository;
import greenlong.repository.CaminhaoRepository;
import greenlong.repository.ItinerarioRepository;
import greenlong.repository.RotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 10/06/2025
 * @brief Versão final e corrigida do RotaService.
 * Gerencia a lógica de negócio das Rotas, garantindo a integridade dos dados
 * ao recalcular os caminhos sempre que necessário.
 */
@Service
@RequiredArgsConstructor
public class RotaService {

    private final RotaRepository rotaRepository;
    private final CaminhaoRepository caminhaoRepository;
    private final BairrosRepository bairroRepository;
    private final DijkstraService dijkstraService;
    private final RotaMapper rotaMapper;
    private final ItinerarioRepository itinerarioRepository;

    @Value("${greenlong.logistica.bairro-origem-padrao:Centro}")
    private String nomeBairroOrigem;

    /**
     * Cria uma nova Rota. O caminho é sempre recalculado no backend para garantir
     * a integridade dos dados, com base nos IDs fornecidos.
     * @param dto DTO simples contendo os IDs de caminhão, destino e o tipo de resíduo.
     * @return O DTO da rota que foi salva no banco de dados.
     */
    @Transactional
    public RotaResponseDTO criarRota(RotaRequestDTO dto) {
        Caminhao caminhao = caminhaoRepository.findById(dto.getCaminhaoId().getId())
                .orElseThrow(() -> new IllegalArgumentException("Caminhão não encontrado."));

        boolean caminhaoPodeColetar = caminhao.getResiduos().stream()
                .anyMatch(residuo -> residuo.getNome().equalsIgnoreCase(dto.getTipoResiduo()));

        if (!caminhaoPodeColetar) {
            throw new IllegalStateException("O caminhão com placa " + caminhao.getPlaca() + " "
                    + "não está configurado para coletar o resíduo do tipo '" + dto.getTipoResiduo() + "'.");
        }

        Bairro destino = bairroRepository.findById(dto.getDestinoId().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bairro de destino não encontrado."));
        
        if (destino.getNome().equalsIgnoreCase(nomeBairroOrigem)) {
            throw new IllegalStateException("O bairro de destino não pode ser o bairro de origem (Centro).");
        }

        Bairro origem = bairroRepository.findByNomeIgnoreCase(nomeBairroOrigem)
                .orElseThrow(() -> new IllegalArgumentException("Bairro de origem '" + nomeBairroOrigem + "' não encontrado no banco de dados."));

        CaminhoDTO caminho = dijkstraService.calcularMenorCaminho(origem.getId(), destino.getId());
        if (caminho.getDistanciaTotal() == -1) {
            throw new IllegalStateException("Destino inalcançável. A rota não pode ser criada.");
        }

        Rota rota = new Rota();
        rota.setCaminhao(caminhao);
        rota.setDestino(destino);
        rota.setTipoResiduo(dto.getTipoResiduo());
        rota.setBairrosPercorridos(caminho.getBairros().stream().map(Bairro::getNome).collect(Collectors.toList()));
        rota.setArestasPercorridas(caminho.getArestas());
        rota.setDistanciaTotal(caminho.getDistanciaTotal());

        Rota rotaSalva = rotaRepository.save(rota);
        return rotaMapper.toResponseDTO(rotaSalva);
    }

    /**
     * Atualiza uma Rota existente. Se o destino for alterado, o caminho é
     * obrigatoriamente recalculado.
     * @param id O ID da rota a ser atualizada.
     * @param dto DTO com os novos dados da rota.
     * @return O DTO da rota atualizada.
     */
    @Transactional
    public RotaResponseDTO atualizarRota(Long id, RotaUpdateRequestDTO dto) {
        Rota rotaExistente = rotaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rota com ID " + id + " não encontrada."));

        Caminhao caminhao = caminhaoRepository.findById(dto.getCaminhao().getId())
                .orElseThrow(() -> new IllegalArgumentException("Caminhão com ID " + dto.getCaminhao().getId() + " não encontrado."));

        Bairro novoDestino = bairroRepository.findById(dto.getDestino().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bairro de destino com ID " + dto.getDestino().getId() + " não encontrado."));
        
         if (novoDestino.getNome().equalsIgnoreCase(nomeBairroOrigem)) {
            throw new IllegalStateException("O bairro de destino não pode ser o bairro de origem (Centro).");
        }

        boolean caminhaoPodeColetar = caminhao.getResiduos().stream()
                .anyMatch(residuo -> residuo.getNome().equalsIgnoreCase(dto.getTipoResiduo()));

        if (!caminhaoPodeColetar) {
            throw new IllegalStateException("O novo caminhão com placa " + caminhao.getPlaca() + " não está configurado para coletar o resíduo do tipo '" + dto.getTipoResiduo() + "'.");
        }

        if (!rotaExistente.getDestino().getId().equals(novoDestino.getId())) {
            Bairro origem = bairroRepository.findByNomeIgnoreCase(nomeBairroOrigem)
                    .orElseThrow(() -> new IllegalArgumentException("Bairro de origem '" + nomeBairroOrigem + "' não encontrado no banco de dados."));

            CaminhoDTO novoCaminho = dijkstraService.calcularMenorCaminho(origem.getId(), novoDestino.getId());
            if (novoCaminho.getDistanciaTotal() == -1) {
                throw new IllegalStateException("O novo destino é inalcançável. Não foi possível atualizar a rota.");
            }

            rotaExistente.setBairrosPercorridos(novoCaminho.getBairros().stream().map(Bairro::getNome).collect(Collectors.toList()));
            rotaExistente.setArestasPercorridas(novoCaminho.getArestas());
            rotaExistente.setDistanciaTotal(novoCaminho.getDistanciaTotal());
        }

        rotaExistente.setCaminhao(caminhao);
        rotaExistente.setDestino(novoDestino);
        rotaExistente.setTipoResiduo(dto.getTipoResiduo());

        Rota rotaSalva = rotaRepository.save(rotaExistente);
        return rotaMapper.toResponseDTO(rotaSalva);
    }

    /**
     * Apenas calcula uma rota para visualização no frontend, sem salvar.
     * @param destinoId O ID do bairro de destino.
     * @return Um DTO detalhado com o caminho completo para o frontend.
     */
    @Transactional(readOnly = true)
    public CalculoRotaResponseDTO calcularRota(Long destinoId) {
        bairroRepository.findById(destinoId).ifPresent(destino -> {
            if (destino.getNome().equalsIgnoreCase(nomeBairroOrigem)) {
                throw new IllegalStateException("Não é possível calcular uma rota para o próprio bairro de origem (Centro).");
            }
        });
        
        Bairro origem = bairroRepository.findByNomeIgnoreCase(nomeBairroOrigem)
                .orElseThrow(() -> new IllegalArgumentException("Bairro de origem '" + nomeBairroOrigem + "' não encontrado no banco de dados."));

        CaminhoDTO caminho = dijkstraService.calcularMenorCaminho(origem.getId(), destinoId);

        List<ConexaoDTO> arestasDTO = caminho.getArestas().stream()
                .map(this::toConexaoDTO)
                .collect(Collectors.toList());

        return new CalculoRotaResponseDTO(caminho.getBairros(), arestasDTO, caminho.getDistanciaTotal());
    }

    @Transactional(readOnly = true)
    public List<RotaResponseDTO> listarTodos() {
        return rotaRepository.findAll().stream()
                .map(rotaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<RotaResponseDTO> buscarPorId(Long id) {
        return rotaRepository.findById(id).map(rotaMapper::toResponseDTO);
    }

    @Transactional
    public boolean deletarRota(Long id) {
        if (!rotaRepository.existsById(id)) {
            return false;
        }

        if (itinerarioRepository.existsByRotaId(id)) {
            throw new DataIntegrityViolationException("Esta rota não pode ser excluída, "
                    + "pois está sendo utilizada em um ou mais itinerários agendados.");
        }
        
        rotaRepository.deleteById(id);
        return true;
    }

    private ConexaoDTO toConexaoDTO(Conexao con) {
        ConexaoDTO.BairroDTO origemDTO = new ConexaoDTO.BairroDTO(con.getOrigem().getId(), con.getOrigem().getNome());
        ConexaoDTO.BairroDTO destinoDTO = new ConexaoDTO.BairroDTO(con.getDestino().getId(), con.getDestino().getNome());
        return new ConexaoDTO(con.getId(), con.getRua(), origemDTO, destinoDTO, con.getQuilometros());
    }
}
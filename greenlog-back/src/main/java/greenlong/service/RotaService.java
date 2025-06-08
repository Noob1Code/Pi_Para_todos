/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.service;

import greenlong.dto.CaminhaoDTO;
import greenlong.dto.PontoColetaResponseDTO;
import greenlong.dto.RotaResponseDTO;
import greenlong.dto.RotaRequestDTO;
import greenlong.model.Caminhao;
import greenlong.model.PontoColeta;
import greenlong.model.Rota;
import greenlong.repository.CaminhaoRepository;
import greenlong.repository.PontoColetaRepository;
import greenlong.repository.RotaRepository;
import org.springframework.transaction.annotation.Transactional;;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class RotaService
 */

@Service
@RequiredArgsConstructor
public class RotaService {

    private final RotaRepository rotaRepository;
    private final CaminhaoRepository caminhaoRepository;
    private final PontoColetaRepository pontoColetaRepository;

    @Transactional
    public RotaResponseDTO criarRota(RotaRequestDTO dto) {
        Caminhao caminhao = caminhaoRepository.findById(dto.getCaminhaoId())
                .orElseThrow(() -> new IllegalArgumentException("Caminhão não encontrado com ID: " + dto.getCaminhaoId()));

        PontoColeta origem = pontoColetaRepository.findById(dto.getOrigemId())
                .orElseThrow(() -> new IllegalArgumentException("Ponto de Coleta de origem não encontrado com ID: " + dto.getOrigemId()));

        PontoColeta destino = pontoColetaRepository.findById(dto.getDestinoId())
                .orElseThrow(() -> new IllegalArgumentException("Ponto de Coleta de destino não encontrado com ID: " + dto.getDestinoId()));

        Rota novaRota = new Rota();
        novaRota.setCaminhao(caminhao);
        novaRota.setPontoColetaOrigem(origem);
        novaRota.setPontoColetaDestino(destino);
        novaRota.setData(dto.getData());

        Rota rotaSalva = rotaRepository.save(novaRota);
        return toResponseDTO(rotaSalva);
    }

    @Transactional(readOnly = true)
    public List<RotaResponseDTO> listarTodas() {
        return rotaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<RotaResponseDTO> buscarPorId(Long id) {
        return rotaRepository.findById(id).map(this::toResponseDTO);
    }

    @Transactional
    public boolean deletarRota(Long id) {
        if (rotaRepository.existsById(id)) {
            rotaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private RotaResponseDTO toResponseDTO(Rota rota) {
        CaminhaoDTO caminhaoDTO = new CaminhaoDTO(rota.getCaminhao().getId(), rota.getCaminhao().getPlaca(), rota.getCaminhao().getMotorista(), rota.getCaminhao().getCapacidade(), rota.getCaminhao().getResiduos());
        PontoColetaResponseDTO origemDTO = toPontoColetaResponseDTO(rota.getPontoColetaOrigem());
        PontoColetaResponseDTO destinoDTO = toPontoColetaResponseDTO(rota.getPontoColetaDestino());

        return new RotaResponseDTO(rota.getId(), rota.getData(), caminhaoDTO, origemDTO, destinoDTO);
    }

    private PontoColetaResponseDTO toPontoColetaResponseDTO(PontoColeta pontoColeta) {
        PontoColetaResponseDTO.BairroDTO bairroDTO = new PontoColetaResponseDTO.BairroDTO(pontoColeta.getBairro().getId(), pontoColeta.getBairro().getNome());
        return new PontoColetaResponseDTO(pontoColeta.getId(), bairroDTO, pontoColeta.getNome(), pontoColeta.getResponsavel(), pontoColeta.getTelefoneResponsavel(), pontoColeta.getEmailResponsavel(), pontoColeta.getEndereco(), pontoColeta.getHorarioFuncionamento(), pontoColeta.getTiposResiduosAceitos());
    }
    
        @Transactional
    public RotaResponseDTO atualizarRota(Long id, RotaRequestDTO dto) {
        // 1. Encontra a rota que será atualizada
        Rota rotaExistente = rotaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rota com ID " + id + " não encontrada."));

        // 2. Busca as novas entidades pelos IDs fornecidos no DTO
        Caminhao caminhao = caminhaoRepository.findById(dto.getCaminhaoId())
                .orElseThrow(() -> new IllegalArgumentException("Caminhão com ID " + dto.getCaminhaoId() + " não encontrado."));

        PontoColeta origem = pontoColetaRepository.findById(dto.getOrigemId())
                .orElseThrow(() -> new IllegalArgumentException("Ponto de coleta de origem com ID " + dto.getOrigemId() + " não encontrado."));

        PontoColeta destino = pontoColetaRepository.findById(dto.getDestinoId())
                .orElseThrow(() -> new IllegalArgumentException("Ponto de coleta de destino com ID " + dto.getDestinoId() + " não encontrado."));

        // 3. Atualiza os dados da rota existente
        rotaExistente.setCaminhao(caminhao);
        rotaExistente.setPontoColetaOrigem(origem);
        rotaExistente.setPontoColetaDestino(destino);
        rotaExistente.setData(dto.getData());

        // 4. Salva a rota atualizada no banco de dados
        Rota rotaSalva = rotaRepository.save(rotaExistente);

        // 5. Converte a entidade salva para um DTO de resposta
        return toResponseDTO(rotaSalva);
    }
}
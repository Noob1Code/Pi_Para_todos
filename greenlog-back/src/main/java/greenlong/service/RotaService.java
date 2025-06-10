/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package greenlong.service;

import greenlong.dto.CalculoRotaResponseDTO;
import greenlong.dto.CaminhoDTO;
import greenlong.dto.CaminhaoDTO;
import greenlong.dto.ConexaoDTO;
import greenlong.dto.PontoColetaResponseDTO;
import greenlong.dto.RotaResponseDTO;
import greenlong.dto.RotaRequestDTO;
import greenlong.model.Bairro;
import greenlong.model.Caminhao;
import greenlong.model.Conexao;
import greenlong.model.PontoColeta;
import greenlong.model.Residuo;
import greenlong.model.Rota;
import greenlong.repository.BairrosRepository;
import greenlong.repository.CaminhaoRepository;
import greenlong.repository.ConexaoRepository;
import greenlong.repository.PontoColetaRepository;
import greenlong.repository.RotaRepository;
import org.springframework.transaction.annotation.Transactional;
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
    private final BairrosRepository bairroRepository;
    private final ConexaoRepository conexaoRepository;
    private final DijkstraService dijkstraService;

    @Transactional
    public RotaResponseDTO criarRota(RotaRequestDTO dto) {
        Caminhao caminhao = caminhaoRepository.findById(dto.getCaminhaoId())
                .orElseThrow(() -> new IllegalArgumentException("Caminhão com ID " + dto.getCaminhaoId() + " não encontrado."));

        Bairro destino = bairroRepository.findById(dto.getDestinoId())
                .orElseThrow(() -> new IllegalArgumentException("Destino com ID " + dto.getDestinoId() + " não encontrado."));

        Bairro origem = bairroRepository.findByNomeIgnoreCase("Centro")
                .orElseThrow(() -> new IllegalArgumentException("Bairro de origem 'Centro' não encontrado."));

        CaminhoDTO caminho = dijkstraService.calcularMenorCaminho(origem.getId(), destino.getId());

        List<String> bairrosPercorridos = caminho.getBairros().stream()
                .map(Bairro::getNome).collect(Collectors.toList());

        List<Long> conexaoIds = caminho.getArestas().stream()
                .map(Conexao::getId).collect(Collectors.toList());

        List<Conexao> conexoes = conexaoRepository.findAllById(conexaoIds);

        Rota rota = new Rota();
        rota.setCaminhao(caminhao);
        rota.setDestino(destino);
        rota.setTipoResiduo(dto.getTipoResiduo());
        rota.setBairrosPercorridos(bairrosPercorridos);
        rota.setArestasPercorridas(conexoes);
        rota.setDistanciaTotal(caminho.getDistanciaTotal());

        rotaRepository.save(rota);
        return toResponseDTO(rota);
    }

    @Transactional(readOnly = true)
    public List<RotaResponseDTO> listarTodos() {
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

    @Transactional
    public RotaResponseDTO atualizarRota(Long id, RotaRequestDTO dto) {
        Rota rotaExistente = rotaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rota com ID " + id + " não encontrada."));

        Caminhao caminhao = caminhaoRepository.findById(dto.getCaminhaoId())
                .orElseThrow(() -> new IllegalArgumentException("Caminhão com ID " + dto.getCaminhaoId() + " não encontrado."));

        Bairro destino = bairroRepository.findById(dto.getDestinoId())
                .orElseThrow(() -> new IllegalArgumentException("Destino com ID " + dto.getDestinoId() + " não encontrado."));

        List<Conexao> conexoes = conexaoRepository.findAllById(dto.getConexaoIds());

        rotaExistente.setCaminhao(caminhao);
        rotaExistente.setDestino(destino);
        rotaExistente.setTipoResiduo(dto.getTipoResiduo());
        rotaExistente.setBairrosPercorridos(dto.getBairrosPercorridos());
        rotaExistente.setArestasPercorridas(conexoes);
        rotaExistente.setDistanciaTotal(dto.getDistanciaTotal());

        Rota rotaSalva = rotaRepository.save(rotaExistente);
        return toResponseDTO(rotaSalva);
    }

    public RotaResponseDTO toResponseDTO(Rota rota) {
        List<ConexaoDTO> arestasDTO = rota.getArestasPercorridas().stream().map(con -> {
            ConexaoDTO.BairroDTO origemDTO = new ConexaoDTO.BairroDTO(
                    con.getOrigem().getId(),
                    con.getOrigem().getNome()
            );
            ConexaoDTO.BairroDTO destinoDTO = new ConexaoDTO.BairroDTO(
                    con.getDestino().getId(),
                    con.getDestino().getNome()
            );
            return new ConexaoDTO(
                    con.getId(),
                    con.getRua(),
                    origemDTO,
                    destinoDTO,
                    con.getQuilometros()
            );
        }).collect(Collectors.toList());

        return new RotaResponseDTO(
                rota.getId(),
                rota.getCaminhao().getPlaca(),
                rota.getDestino().getNome(),
                rota.getTipoResiduo(),
                rota.getBairrosPercorridos(),
                arestasDTO,
                rota.getDistanciaTotal()
        );
    }

    public CalculoRotaResponseDTO calcularRota(Long caminhaoId, Long destinoId, String tipoResiduo) {
    Caminhao caminhao = caminhaoRepository.findById(caminhaoId)
        .orElseThrow(() -> new IllegalArgumentException("Caminhão não encontrado"));

    Bairro destino = bairroRepository.findById(destinoId)
        .orElseThrow(() -> new IllegalArgumentException("Destino não encontrado"));

    Bairro origem = bairroRepository.findByNomeIgnoreCase("Centro")
        .orElseThrow(() -> new IllegalArgumentException("Bairro 'Centro' não encontrado"));

    CaminhoDTO caminho = dijkstraService.calcularMenorCaminho(origem.getId(), destino.getId());

    List<String> bairros = caminho.getBairros().stream().map(Bairro::getNome).toList();
    List<Long> conexaoIds = caminho.getArestas().stream().map(Conexao::getId).toList();
    List<Conexao> conexoes = conexaoRepository.findAllById(conexaoIds);

    List<ConexaoDTO> arestasDTO = conexoes.stream().map(con -> new ConexaoDTO(
        con.getId(),
        con.getRua(),
        new ConexaoDTO.BairroDTO(con.getOrigem().getId(), con.getOrigem().getNome()),
        new ConexaoDTO.BairroDTO(con.getDestino().getId(), con.getDestino().getNome()),
        con.getQuilometros()
    )).toList();

    return new CalculoRotaResponseDTO(
        caminhao.getPlaca(),
        destino.getNome(),
        tipoResiduo,
        bairros,
        arestasDTO,
        caminho.getDistanciaTotal()
    );
}
}

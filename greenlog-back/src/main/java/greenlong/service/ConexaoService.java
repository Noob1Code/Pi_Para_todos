/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.service;

import greenlong.dto.ConexaoDTO;
import greenlong.model.Bairro;
import greenlong.model.Conexao;
import greenlong.repository.BairrosRepository;
import greenlong.repository.ConexaoRepository;
import greenlong.repository.RotaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class ConexaoService
 */

@Service
@RequiredArgsConstructor
public class ConexaoService {

    private final ConexaoRepository conexaoRepository;
    private final BairrosRepository bairrosRepository;
    private final RotaRepository rotaRepository;

    @Transactional
    public ConexaoDTO criarConexao(ConexaoDTO dto) {
        Bairro origem = bairrosRepository.findById(dto.getOrigem().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bairro de origem não encontrado com ID: " + dto.getOrigem().getId())); //

        Bairro destino = bairrosRepository.findById(dto.getDestino().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bairro de destino não encontrado com ID: " + dto.getDestino().getId())); //

        if (origem.getId().equals(destino.getId())) {
            throw new IllegalArgumentException("O bairro de origem não pode ser o mesmo que o de destino.");
        }

        Conexao conexao = new Conexao();
        conexao.setRua(dto.getRua()); //
        conexao.setQuilometros(dto.getQuilometros()); //
        conexao.setOrigem(origem); //
        conexao.setDestino(destino); //

        Conexao novaConexao = conexaoRepository.save(conexao);
        return toDTO(novaConexao);
    }

    @Transactional
    public Optional<ConexaoDTO> atualizarConexao(Long id, ConexaoDTO dto) {
        return conexaoRepository.findById(id).map(conexaoExistente -> {
            Bairro origem = bairrosRepository.findById(dto.getOrigem().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Bairro de origem não encontrado com ID: " + dto.getOrigem().getId())); //

            Bairro destino = bairrosRepository.findById(dto.getDestino().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Bairro de destino não encontrado com ID: " + dto.getDestino().getId())); //
            
            if (origem.getId().equals(destino.getId())) {
                throw new IllegalArgumentException("O bairro de origem não pode ser o mesmo que o de destino.");
            }
            
            conexaoExistente.setRua(dto.getRua()); //
            conexaoExistente.setQuilometros(dto.getQuilometros()); //
            conexaoExistente.setOrigem(origem); //
            conexaoExistente.setDestino(destino); //

            conexaoRepository.save(conexaoExistente);
            return toDTO(conexaoExistente);
        });
    }

    @Transactional
    public boolean deletarConexao(Long id) {
        // -> VALIDAÇÃO DE INTEGRIDADE ADICIONADA
        if (rotaRepository.existsByArestasPercorridas_Id(id)) {
            throw new DataIntegrityViolationException("Esta conexão não pode ser excluída, pois faz parte de uma ou mais rotas salvas.");
        }

        if (conexaoRepository.existsById(id)) { //
            conexaoRepository.deleteById(id); //
            return true;
        }
        return false;
    }

    // O método listarTodas() e toDTO() permanecem os mesmos.
    @Transactional(readOnly = true)
    public List<ConexaoDTO> listarTodas() {
        return conexaoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ConexaoDTO toDTO(Conexao conexao) {
        ConexaoDTO.BairroDTO origemDto = new ConexaoDTO.BairroDTO(
            conexao.getOrigem().getId(),
            conexao.getOrigem().getNome()
        );

        ConexaoDTO.BairroDTO destinoDto = new ConexaoDTO.BairroDTO(
            conexao.getDestino().getId(),
            conexao.getDestino().getNome()
        );

        return new ConexaoDTO(
            conexao.getId(),
            conexao.getRua(),
            origemDto,
            destinoDto,
            conexao.getQuilometros()
        );
    }
}

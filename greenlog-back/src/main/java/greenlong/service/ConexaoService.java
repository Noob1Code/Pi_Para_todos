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
 * @brief Class ConexaoService
 */

@Service
@RequiredArgsConstructor
public class ConexaoService {

        private final ConexaoRepository conexaoRepository;
    private final BairrosRepository bairrosRepository;

    @Transactional
    public ConexaoDTO criarConexao(ConexaoDTO dto) {
        // Agora busca no repositório de Bairros
        Bairro origem = bairrosRepository.findById(dto.getOrigem().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bairro de origem não encontrado com ID: " + dto.getOrigem().getId()));

        Bairro destino = bairrosRepository.findById(dto.getDestino().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bairro de destino não encontrado com ID: " + dto.getDestino().getId()));

        Conexao conexao = new Conexao();
        conexao.setRua(dto.getRua());
        conexao.setQuilometros(dto.getQuilometros());
        conexao.setOrigem(origem);
        conexao.setDestino(destino);

        Conexao novaConexao = conexaoRepository.save(conexao);
        return toDTO(novaConexao);
    }

    @Transactional(readOnly = true)
    public List<ConexaoDTO> listarTodas() {
        return conexaoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<ConexaoDTO> atualizarConexao(Long id, ConexaoDTO dto) {
        return conexaoRepository.findById(id).map(conexaoExistente -> {
            Bairro origem = bairrosRepository.findById(dto.getOrigem().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Bairro de origem não encontrado com ID: " + dto.getOrigem().getId()));

            Bairro destino = bairrosRepository.findById(dto.getDestino().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Bairro de destino não encontrado com ID: " + dto.getDestino().getId()));
            
            conexaoExistente.setRua(dto.getRua());
            conexaoExistente.setQuilometros(dto.getQuilometros());
            conexaoExistente.setOrigem(origem);
            conexaoExistente.setDestino(destino);

            conexaoRepository.save(conexaoExistente);
            return toDTO(conexaoExistente);
        });
    }

    @Transactional
    public boolean deletarConexao(Long id) {
        if (conexaoRepository.existsById(id)) {
            conexaoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ConexaoDTO toDTO(Conexao conexao) {
        // --- MUDANÇA AQUI: Agora passamos o ID e o NOME para o BairroDTO ---
        
        // Pega o objeto Bairro da entidade Conexao e extrai o ID e o Nome
        ConexaoDTO.BairroDTO origemDto = new ConexaoDTO.BairroDTO(
            conexao.getOrigem().getId(),
            conexao.getOrigem().getNome() // Adicionado o nome da origem
        );

        ConexaoDTO.BairroDTO destinoDto = new ConexaoDTO.BairroDTO(
            conexao.getDestino().getId(),
            conexao.getDestino().getNome() // Adicionado o nome do destino
        );
        // --- FIM DA MUDANÇA ---

        return new ConexaoDTO(
            conexao.getId(),
            conexao.getRua(),
            origemDto,
            destinoDto,
            conexao.getQuilometros()
        );
    }
}

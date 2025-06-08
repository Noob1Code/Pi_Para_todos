/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.service;

import greenlong.dto.BairroRequestDTO;
import greenlong.dto.BairroDTO;
import greenlong.model.Bairro;
import greenlong.repository.BairrosRepository;
import greenlong.repository.PontoColetaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class BairroService
 */

@Service
@RequiredArgsConstructor
public class BairroService {

    private final BairrosRepository bairrosRepository;
    private final PontoColetaRepository pontoColetaRepository; 

    @Transactional(readOnly = true)
    public List<BairroDTO> listarTodos() {
        return bairrosRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BairroDTO criarBairro(BairroRequestDTO dto) {
        Bairro bairro = new Bairro();
        bairro.setNome(dto.getNome());
        Bairro novoBairro = bairrosRepository.save(bairro);
        return toDTO(novoBairro);
    }
    
@Transactional
    public Optional<BairroDTO> atualizarBairro(Long id, BairroRequestDTO dto) {
        return bairrosRepository.findById(id).map(bairroExistente -> {
            bairroExistente.setNome(dto.getNome());
            Bairro bairroAtualizado = bairrosRepository.save(bairroExistente);
            return toDTO(bairroAtualizado);
        });
    }

     @Transactional
    public boolean deletarBairro(Long id) {
        if (!bairrosRepository.existsById(id)) {
            return false;
        }
        
        if (pontoColetaRepository.existsByBairroId(id)) {
            throw new DataIntegrityViolationException("Este bairro não pode ser excluído, pois está associado a um ou mais pontos de coleta.");
        }

        // 3. Se não houver dependências, exclui o bairro
        bairrosRepository.deleteById(id);
        return true;
    }
    
    private BairroDTO toDTO(Bairro bairro) {
        return new BairroDTO(bairro.getId(), bairro.getNome());
    }
}

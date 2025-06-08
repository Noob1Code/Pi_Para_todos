/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.service;

import greenlong.dto.PontoColetaCadastroDTO;
import greenlong.dto.PontoColetaResponseDTO;
import greenlong.model.Bairro;
import greenlong.model.PontoColeta;
import greenlong.repository.BairrosRepository;
import greenlong.repository.PontoColetaRepository;
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
 * @brief Class PontoColetaService
 */

@Service
@RequiredArgsConstructor
public class PontoColetaService {

    private final PontoColetaRepository pontoColetaRepository;
    private final BairrosRepository bairrosRepository;

    @Transactional
    public PontoColetaResponseDTO criarPontoColeta(PontoColetaCadastroDTO dto) {
        if (pontoColetaRepository.existsByNome(dto.getNome())) {
            throw new IllegalArgumentException("Já existe um ponto de coleta com o nome: " + dto.getNome());
        }
        Bairro bairro = bairrosRepository.findById(dto.getBairro().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bairro não encontrado com o ID: " + dto.getBairro().getId()));
        
        PontoColeta novoPontoColeta = toEntity(new PontoColeta(), dto, bairro);
        PontoColeta salvo = pontoColetaRepository.save(novoPontoColeta);
        return toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<PontoColetaResponseDTO> listarTodosPontosColeta() {
        return pontoColetaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PontoColetaResponseDTO> buscarPontoColetaPorId(Long id) {
        return pontoColetaRepository.findById(id).map(this::toResponseDTO);
    }

    @Transactional
    public Optional<PontoColetaResponseDTO> atualizarPontoColeta(Long id, PontoColetaCadastroDTO dto) {
        return pontoColetaRepository.findById(id).map(existente -> {
            pontoColetaRepository.findByNome(dto.getNome()).ifPresent(porNome -> {
                if (!porNome.getId().equals(id)) {
                    throw new IllegalArgumentException("Já existe outro ponto de coleta com o nome: " + dto.getNome());
                }
            });
            Bairro bairro = bairrosRepository.findById(dto.getBairro().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Bairro não encontrado com o ID: " + dto.getBairro().getId()));
            
            PontoColeta atualizado = toEntity(existente, dto, bairro);
            pontoColetaRepository.save(atualizado);
            return toResponseDTO(atualizado);
        });
    }

    @Transactional
    public boolean deletarPontoColeta(Long id) {
        if (pontoColetaRepository.existsById(id)) {
            pontoColetaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private PontoColetaResponseDTO toResponseDTO(PontoColeta pontoColeta) {
        PontoColetaResponseDTO.BairroDTO bairroDTO = null;
        if (pontoColeta.getBairro() != null) {
            bairroDTO = new PontoColetaResponseDTO.BairroDTO(
                pontoColeta.getBairro().getId(),
                pontoColeta.getBairro().getNome()
            );
        }
        return new PontoColetaResponseDTO(pontoColeta.getId(), bairroDTO, pontoColeta.getNome(), pontoColeta.getResponsavel(), pontoColeta.getTelefoneResponsavel(), pontoColeta.getEmailResponsavel(), pontoColeta.getEndereco(), pontoColeta.getHorarioFuncionamento(), pontoColeta.getTiposResiduosAceitos());
    }

    private PontoColeta toEntity(PontoColeta pontoColeta, PontoColetaCadastroDTO dto, Bairro bairro) {
        pontoColeta.setBairro(bairro);
        pontoColeta.setNome(dto.getNome());
        pontoColeta.setResponsavel(dto.getResponsavel());
        pontoColeta.setTelefoneResponsavel(dto.getTelefoneResponsavel());
        pontoColeta.setEmailResponsavel(dto.getEmailResponsavel());
        pontoColeta.setEndereco(dto.getEndereco());
        pontoColeta.setHorarioFuncionamento(dto.getHorarioFuncionamento());
        pontoColeta.setTiposResiduosAceitos(dto.getTiposResiduosAceitos());
        return pontoColeta;
    }
}

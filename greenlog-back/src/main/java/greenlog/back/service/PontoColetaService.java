/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlog.back.service;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 04/06/2025
 * @brief Class PontoColetaService
 */

import greenlog.back.dto.PontoColetaCadastroDTO;
import greenlog.back.dto.PontoColetaResponseDTO;
import greenlog.back.model.Bairro;
import greenlog.back.model.PontoColeta;
import greenlog.back.repository.BairrosRepository;
import greenlog.back.repository.PontoColetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PontoColetaService {

    private final PontoColetaRepository pontoColetaRepository;
    private final BairrosRepository bairrosRepository; // NOVO: Injetar o repositório de bairros

    @Autowired
    public PontoColetaService(PontoColetaRepository pontoColetaRepository, BairrosRepository bairrosRepository) { // NOVO: Construtor atualizado
        this.pontoColetaRepository = pontoColetaRepository;
        this.bairrosRepository = bairrosRepository;
    }

    @Transactional
    public PontoColetaResponseDTO criarPontoColeta(PontoColetaCadastroDTO dto) {
        if (pontoColetaRepository.existsByNome(dto.getNome())) {
            throw new IllegalArgumentException("Já existe um ponto de coleta com o nome: " + dto.getNome());
        }
        
        // NOVO: Buscar a entidade Bairro a partir do ID recebido no DTO
        Bairro bairro = bairrosRepository.findById(dto.getBairro().getId().intValue())
                .orElseThrow(() -> new IllegalArgumentException("Bairro não encontrado com o ID: " + dto.getBairro().getId()));

        PontoColeta novoPontoColeta = new PontoColeta();
        novoPontoColeta.setBairro(bairro); // ALTERADO: Seta o objeto Bairro completo
        novoPontoColeta.setNome(dto.getNome());
        novoPontoColeta.setResponsavel(dto.getResponsavel());
        novoPontoColeta.setTelefoneResponsavel(dto.getTelefoneResponsavel());
        novoPontoColeta.setEmailResponsavel(dto.getEmailResponsavel());
        novoPontoColeta.setEndereco(dto.getEndereco());
        novoPontoColeta.setHorarioFuncionamento(dto.getHorarioFuncionamento());
        novoPontoColeta.setTiposResiduosAceitos(dto.getTiposResiduosAceitos());

        PontoColeta salvo = pontoColetaRepository.save(novoPontoColeta);
        return mapToResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<PontoColetaResponseDTO> listarTodosPontosColeta() {
        return pontoColetaRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PontoColetaResponseDTO> buscarPontoColetaPorId(Long id) {
        return pontoColetaRepository.findById(id).map(this::mapToResponseDTO);
    }

    @Transactional
    public Optional<PontoColetaResponseDTO> atualizarPontoColeta(Long id, PontoColetaCadastroDTO dto) {
        return pontoColetaRepository.findById(id).map(existente -> {
            pontoColetaRepository.findByNome(dto.getNome()).ifPresent(porNome -> {
                if (!porNome.getId().equals(id)) {
                    throw new IllegalArgumentException("Já existe outro ponto de coleta com o nome: " + dto.getNome());
                }
            });

            // NOVO: Lógica para atualizar o bairro
            Bairro bairro = bairrosRepository.findById(dto.getBairro().getId().intValue())
                    .orElseThrow(() -> new IllegalArgumentException("Bairro não encontrado com o ID: " + dto.getBairro().getId()));
            
            existente.setBairro(bairro); // ALTERADO
            existente.setNome(dto.getNome());
            existente.setResponsavel(dto.getResponsavel());
            existente.setTelefoneResponsavel(dto.getTelefoneResponsavel());
            existente.setEmailResponsavel(dto.getEmailResponsavel());
            existente.setEndereco(dto.getEndereco());
            existente.setHorarioFuncionamento(dto.getHorarioFuncionamento());
            existente.setTiposResiduosAceitos(dto.getTiposResiduosAceitos());

            PontoColeta atualizado = pontoColetaRepository.save(existente);
            return mapToResponseDTO(atualizado);
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

    // ALTERADO: O método de mapeamento agora precisa criar o BairroDTO aninhado
    private PontoColetaResponseDTO mapToResponseDTO(PontoColeta pontoColeta) {
        PontoColetaResponseDTO.BairroDTO bairroDTO = null;
        if (pontoColeta.getBairro() != null) {
            bairroDTO = new PontoColetaResponseDTO.BairroDTO(
                pontoColeta.getBairro().getId(),
                pontoColeta.getBairro().getNome()
            );
        }

        return new PontoColetaResponseDTO(
                pontoColeta.getId(),
                bairroDTO, // Passa o DTO do bairro
                pontoColeta.getNome(),
                pontoColeta.getResponsavel(),
                pontoColeta.getTelefoneResponsavel(),
                pontoColeta.getEmailResponsavel(),
                pontoColeta.getEndereco(),
                pontoColeta.getHorarioFuncionamento(),
                pontoColeta.getTiposResiduosAceitos()
        );
    }
}
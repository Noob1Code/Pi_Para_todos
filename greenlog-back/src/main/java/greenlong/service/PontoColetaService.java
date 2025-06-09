/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.service;

import greenlong.dto.PontoColetaCadastroDTO;
import greenlong.dto.PontoColetaResponseDTO;
import greenlong.model.Bairro;
import greenlong.model.PontoColeta;
import greenlong.model.Residuo;
import greenlong.repository.BairrosRepository;
import greenlong.repository.PontoColetaRepository;
import greenlong.repository.ResiduoRepository;
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
    private final ResiduoRepository residuoRepository;
    private final AuditoriaService auditoriaService;

    @Transactional
    public PontoColetaResponseDTO criarPontoColeta(PontoColetaCadastroDTO dto) {
        if (pontoColetaRepository.existsByNome(dto.getNome())) {
            throw new IllegalArgumentException("Já existe um ponto de coleta com o nome: " + dto.getNome());
        }
        Bairro bairro = bairrosRepository.findById(dto.getBairro().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bairro não encontrado com o ID: " + dto.getBairro().getId()));
        
        List<Residuo> residuos = residuoRepository.findByNomeIn(dto.getTiposResiduosAceitos());
        if(residuos.size() != dto.getTiposResiduosAceitos().size()){
             throw new IllegalArgumentException("Um ou mais tipos de resíduos informados são inválidos.");
        }
        
        PontoColeta novoPontoColeta = toEntity(new PontoColeta(), dto, bairro, residuos);
        PontoColeta salvo = pontoColetaRepository.save(novoPontoColeta);

        auditoriaService.logPontoColetaActivity(salvo.getId(), null, toResponseDTO(salvo), "INSERT");
        
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
            PontoColetaResponseDTO estadoAntes = toResponseDTO(existente);

            pontoColetaRepository.findByNome(dto.getNome()).ifPresent(porNome -> {
                if (!porNome.getId().equals(id)) {
                    throw new IllegalArgumentException("Já existe outro ponto de coleta com o nome: " + dto.getNome());
                }
            });
            Bairro bairro = bairrosRepository.findById(dto.getBairro().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Bairro não encontrado com o ID: " + dto.getBairro().getId()));
            
            List<Residuo> residuos = residuoRepository.findByNomeIn(dto.getTiposResiduosAceitos());
            if(residuos.size() != dto.getTiposResiduosAceitos().size()){
                throw new IllegalArgumentException("Um ou mais tipos de resíduos informados são inválidos.");
            }

            PontoColeta atualizado = toEntity(existente, dto, bairro, residuos);
            pontoColetaRepository.save(atualizado);

            auditoriaService.logPontoColetaActivity(atualizado.getId(), estadoAntes, toResponseDTO(atualizado), "UPDATE");

            return toResponseDTO(atualizado);
        });
    }

    @Transactional
    public boolean deletarPontoColeta(Long id) {
        Optional<PontoColeta> pontoColetaOpt = pontoColetaRepository.findById(id);

        if (pontoColetaOpt.isPresent()) {
            PontoColeta pontoParaDeletar = pontoColetaOpt.get();

            PontoColetaResponseDTO estadoAntes = toResponseDTO(pontoParaDeletar);

            pontoColetaRepository.deleteById(id);

            auditoriaService.logPontoColetaActivity(id, estadoAntes, null, "DELETE");

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

        List<String> nomesResiduos = pontoColeta.getTiposResiduosAceitos().stream()
                .map(Residuo::getNome)
                .collect(Collectors.toList());

        return new PontoColetaResponseDTO(
            pontoColeta.getId(), 
            bairroDTO, 
            pontoColeta.getNome(), 
            pontoColeta.getResponsavel(), 
            pontoColeta.getTelefoneResponsavel(), 
            pontoColeta.getEmailResponsavel(), 
            pontoColeta.getEndereco(), 
            pontoColeta.getHorarioFuncionamento(), 
            nomesResiduos
        );
    }

    private PontoColeta toEntity(PontoColeta pontoColeta, PontoColetaCadastroDTO dto, Bairro bairro, List<Residuo> residuos) {
        pontoColeta.setBairro(bairro);
        pontoColeta.setNome(dto.getNome());
        pontoColeta.setResponsavel(dto.getResponsavel());
        pontoColeta.setTelefoneResponsavel(dto.getTelefoneResponsavel());
        pontoColeta.setEmailResponsavel(dto.getEmailResponsavel());
        pontoColeta.setEndereco(dto.getEndereco());
        pontoColeta.setHorarioFuncionamento(dto.getHorarioFuncionamento());
        pontoColeta.setTiposResiduosAceitos(residuos);
        return pontoColeta;
    }
}
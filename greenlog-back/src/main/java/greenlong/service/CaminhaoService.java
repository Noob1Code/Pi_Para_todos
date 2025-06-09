/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.service;

import greenlong.dto.CaminhaoDTO;
import greenlong.dto.CaminhaoRequestDTO;
import greenlong.model.Caminhao;
import greenlong.model.Residuo;
import greenlong.repository.CaminhaoRepository;
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
 * @brief Class CaminhaoService
 */

@Service
@RequiredArgsConstructor
public class CaminhaoService {

    private final CaminhaoRepository caminhaoRepository;
    private final ResiduoRepository residuoRepository;
    private final AuditoriaService auditoriaService;

    @Transactional
    public CaminhaoDTO criarCaminhao(CaminhaoRequestDTO dto) {
        if (caminhaoRepository.existsByPlacaIgnoreCase(dto.getPlaca())) {
            throw new IllegalArgumentException("A placa '" + dto.getPlaca() + "' já está cadastrada.");
        }

        List<Residuo> residuos = residuoRepository.findByNomeIn(dto.getResiduos());
        if (residuos.size() != dto.getResiduos().size()) {
            throw new IllegalArgumentException("Um ou mais tipos de resíduos informados são inválidos.");
        }

        Caminhao novoCaminhao = toEntity(new Caminhao(), dto, residuos);
        Caminhao caminhaoSalvo = caminhaoRepository.save(novoCaminhao);

        auditoriaService.logCaminhaoActivity(caminhaoSalvo.getId(), null, toResponseDTO(caminhaoSalvo), "INSERT");
        
        return toResponseDTO(caminhaoSalvo);
    }
    
    @Transactional(readOnly = true)
    public List<CaminhaoDTO> listarTodos() {
        return caminhaoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CaminhaoDTO> buscarPorId(Long id) {
        return caminhaoRepository.findById(id).map(this::toResponseDTO);
    }

    @Transactional
    public Optional<CaminhaoDTO> atualizarCaminhao(Long id, CaminhaoRequestDTO dto) {
        return caminhaoRepository.findById(id).map(caminhaoExistente -> {
            CaminhaoDTO estadoAntes = toResponseDTO(caminhaoExistente);

            caminhaoRepository.findByPlacaIgnoreCase(dto.getPlaca()).ifPresent(caminhaoComMesmaPlaca -> {
                if (!caminhaoComMesmaPlaca.getId().equals(id)) {
                    throw new IllegalArgumentException("A placa '" + dto.getPlaca() + "' já pertence a outro caminhão.");
                }
            });

            List<Residuo> residuos = residuoRepository.findByNomeIn(dto.getResiduos());
            if (residuos.size() != dto.getResiduos().size()) {
                throw new IllegalArgumentException("Um ou mais tipos de resíduos informados são inválidos.");
            }

            Caminhao caminhaoAtualizado = toEntity(caminhaoExistente, dto, residuos);
            caminhaoRepository.save(caminhaoAtualizado);
            
            auditoriaService.logCaminhaoActivity(caminhaoAtualizado.getId(), estadoAntes, toResponseDTO(caminhaoAtualizado), "UPDATE");
            
            return toResponseDTO(caminhaoAtualizado);
        });
    }

    @Transactional
    public boolean deletarCaminhao(Long id) {
        Optional<Caminhao> caminhaoOpt = caminhaoRepository.findById(id);

        if (caminhaoOpt.isPresent()) {
            Caminhao caminhaoParaDeletar = caminhaoOpt.get();
            CaminhaoDTO estadoAntes = toResponseDTO(caminhaoParaDeletar);

            caminhaoRepository.deleteById(id);
            
            auditoriaService.logCaminhaoActivity(id, estadoAntes, null, "DELETE");

            return true;
        }
        return false;
    }

    private CaminhaoDTO toResponseDTO(Caminhao caminhao) {
        List<String> nomesResiduos = caminhao.getResiduos().stream()
                .map(Residuo::getNome)
                .collect(Collectors.toList());

        return new CaminhaoDTO(
            caminhao.getId(), 
            caminhao.getPlaca(), 
            caminhao.getMotorista(), 
            caminhao.getCapacidade(), 
            nomesResiduos
        );
    }

    private Caminhao toEntity(Caminhao caminhao, CaminhaoRequestDTO dto, List<Residuo> residuos) {
        caminhao.setPlaca(dto.getPlaca());
        caminhao.setMotorista(dto.getMotorista());
        caminhao.setCapacidade(dto.getCapacidade());
        caminhao.setResiduos(residuos);
        return caminhao;
    }
}
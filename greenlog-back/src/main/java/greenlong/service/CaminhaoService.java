/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.service;

import greenlong.dto.CaminhaoDTO;
import greenlong.dto.CaminhaoRequestDTO;
import greenlong.model.Caminhao;
import greenlong.repository.CaminhaoRepository;
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

    @Transactional
    public CaminhaoDTO criarCaminhao(CaminhaoRequestDTO dto) {
        if (caminhaoRepository.existsByPlacaIgnoreCase(dto.getPlaca())) {
            throw new IllegalArgumentException("A placa '" + dto.getPlaca() + "' já está cadastrada.");
        }
        Caminhao novoCaminhao = toEntity(new Caminhao(), dto);
        Caminhao caminhaoSalvo = caminhaoRepository.save(novoCaminhao);
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
            caminhaoRepository.findByPlacaIgnoreCase(dto.getPlaca()).ifPresent(caminhaoComMesmaPlaca -> {
                if (!caminhaoComMesmaPlaca.getId().equals(id)) {
                    throw new IllegalArgumentException("A placa '" + dto.getPlaca() + "' já pertence a outro caminhão.");
                }
            });
            Caminhao caminhaoAtualizado = toEntity(caminhaoExistente, dto);
            caminhaoRepository.save(caminhaoAtualizado);
            return toResponseDTO(caminhaoAtualizado);
        });
    }

    @Transactional
    public boolean deletarCaminhao(Long id) {
        if (caminhaoRepository.existsById(id)) {
            caminhaoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private CaminhaoDTO toResponseDTO(Caminhao caminhao) {
        return new CaminhaoDTO(caminhao.getId(), caminhao.getPlaca(), caminhao.getMotorista(), caminhao.getCapacidade(), caminhao.getResiduos());
    }

    private Caminhao toEntity(Caminhao caminhao, CaminhaoRequestDTO dto) {
        caminhao.setPlaca(dto.getPlaca());
        caminhao.setMotorista(dto.getMotorista());
        caminhao.setCapacidade(dto.getCapacidade());
        caminhao.setResiduos(dto.getResiduos());
        return caminhao;
    }
}
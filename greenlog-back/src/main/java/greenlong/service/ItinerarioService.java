/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.service;


import greenlong.dto.ItinerarioRequestDTO;
import greenlong.dto.ItinerarioResponseDTO;
import greenlong.mapper.RotaMapper;
import greenlong.model.Itinerario;
import greenlong.model.Rota;
import greenlong.repository.ItinerarioRepository;
import greenlong.repository.RotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 12/06/2025
 * @brief Class ItinerarioService
 */

@Service
@RequiredArgsConstructor
public class ItinerarioService {

    private final ItinerarioRepository itinerarioRepository;
    private final RotaRepository rotaRepository;
    private final RotaMapper rotaMapper;

    @Transactional
    public ItinerarioResponseDTO criarItinerario(ItinerarioRequestDTO dto) {
        Rota rota = rotaRepository.findById(dto.getRotaId().getId())
                .orElseThrow(() -> new IllegalArgumentException("Rota com ID " + dto.getRotaId().getId() + " não encontrada."));

        itinerarioRepository.findByDataAndRota_Caminhao_Id(dto.getData(), rota.getCaminhao().getId())
                .ifPresent(i -> {
                    throw new IllegalStateException("Conflito: O caminhão (placa: " + rota.getCaminhao().getPlaca() + ") já está "
                            + "agendado para outra rota neste dia.");
                });

        Itinerario novoItinerario = new Itinerario();
        novoItinerario.setData(dto.getData());
        novoItinerario.setRota(rota);

        Itinerario itinerarioSalvo = itinerarioRepository.save(novoItinerario);
        return toResponseDTO(itinerarioSalvo);
    }
    
    @Transactional
    public ItinerarioResponseDTO atualizarItinerario(Long id, ItinerarioRequestDTO dto) {
        Itinerario itinerarioExistente = itinerarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Itinerário com ID " + id + " não encontrado."));

        Rota novaRota = rotaRepository.findById(dto.getRotaId().getId())
            .orElseThrow(() -> new IllegalArgumentException("Rota com ID " + dto.getRotaId().getId() + " não encontrada."));

        // VALIDAÇÃO DE CONFLITO: Verifica conflitos, mas ignora o próprio itinerário que estamos editando
        itinerarioRepository.findByDataAndRota_Caminhao_IdAndIdNot(dto.getData(), novaRota.getCaminhao().getId(), id)
            .ifPresent(i -> {
                throw new IllegalStateException("Conflito: O caminhão (placa: " + novaRota.getCaminhao().getPlaca() + ") já está "
                        + "agendado para outra rota neste dia.");
            });

        itinerarioExistente.setData(dto.getData());
        itinerarioExistente.setRota(novaRota);

        Itinerario itinerarioAtualizado = itinerarioRepository.save(itinerarioExistente);
        return toResponseDTO(itinerarioAtualizado);
    }

    @Transactional(readOnly = true)
    public List<ItinerarioResponseDTO> listarTodos() {
        return itinerarioRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItinerarioResponseDTO> listarPorDia(LocalDate data) {
        return itinerarioRepository.findByData(data).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItinerarioResponseDTO> listarPorCaminhao(Long caminhaoId) {
        return itinerarioRepository.findByRota_Caminhao_Id(caminhaoId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean deletarItinerario(Long id) {
        if (itinerarioRepository.existsById(id)) {
            itinerarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ItinerarioResponseDTO toResponseDTO(Itinerario itinerario) {
        var rotaDTO = rotaMapper.toResponseDTO(itinerario.getRota());
        return new ItinerarioResponseDTO(
                itinerario.getId(),
                itinerario.getData(),
                rotaDTO
        );
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.controller;

import greenlong.dto.ItinerarioRequestDTO;
import greenlong.dto.ItinerarioResponseDTO;
import greenlong.service.ItinerarioService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 12/06/2025
 * @brief Class ItinerarioController
 */

@RestController
@RequestMapping("/api/itinerarios")
@RequiredArgsConstructor
public class ItinerarioController {

    private final ItinerarioService itinerarioService;

    @PostMapping
    public ResponseEntity<ItinerarioResponseDTO> criarItinerario(@Valid @RequestBody ItinerarioRequestDTO dto) {
        ItinerarioResponseDTO novoItinerario = itinerarioService.criarItinerario(dto);
        return new ResponseEntity<>(novoItinerario, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItinerarioResponseDTO> atualizarItinerario(@PathVariable Long id, @Valid @RequestBody ItinerarioRequestDTO dto) {
        ItinerarioResponseDTO itinerarioAtualizado = itinerarioService.atualizarItinerario(id, dto);
        return ResponseEntity.ok(itinerarioAtualizado);
    }
    
    @GetMapping
    public ResponseEntity<List<ItinerarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(itinerarioService.listarTodos());
    }

    @GetMapping("/por-dia")
    public ResponseEntity<List<ItinerarioResponseDTO>> listarPorDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(itinerarioService.listarPorDia(data));
    }

    @GetMapping("/por-caminhao/{caminhaoId}")
    public ResponseEntity<List<ItinerarioResponseDTO>> listarPorCaminhao(@PathVariable Long caminhaoId) {
        return ResponseEntity.ok(itinerarioService.listarPorCaminhao(caminhaoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarItinerario(@PathVariable Long id) {
        if (itinerarioService.deletarItinerario(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
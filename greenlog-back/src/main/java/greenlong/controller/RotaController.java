/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package greenlong.controller;

import greenlong.dto.CalculoRotaResponseDTO;
import greenlong.dto.RotaRequestDTO;
import greenlong.dto.RotaResponseDTO;
import greenlong.dto.RotaUpdateRequestDTO;
import greenlong.service.RotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
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
 * @data 06/06/2025
 * @brief Class RotaController
 */
@RestController
@RequestMapping("/api/rotas")
@RequiredArgsConstructor
public class RotaController {

    private final RotaService rotaService;

    @PostMapping
    public ResponseEntity<RotaResponseDTO> criarRota(@Valid @RequestBody RotaRequestDTO dto) {
        RotaResponseDTO novaRota = rotaService.criarRota(dto);
        return new ResponseEntity<>(novaRota, HttpStatus.CREATED);
    }

    @GetMapping("/calcular")
    public ResponseEntity<?> calcularRotaParaVisualizacao(@RequestParam Long destinoId) {
        CalculoRotaResponseDTO resposta = rotaService.calcularRota(destinoId);
        if (resposta.getDistanciaTotal() == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Não foi possível encontrar um caminho até o destino."));
        }
        return ResponseEntity.ok(resposta);
    }

    @GetMapping
    public ResponseEntity<List<RotaResponseDTO>> listarTodasRotas() {
        return ResponseEntity.ok(rotaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RotaResponseDTO> buscarRotaPorId(@PathVariable Long id) {
        return rotaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RotaResponseDTO> atualizarRota(@PathVariable Long id, @Valid @RequestBody RotaUpdateRequestDTO dto) {
        RotaResponseDTO rotaAtualizada = rotaService.atualizarRota(id, dto);
        return ResponseEntity.ok(rotaAtualizada);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRota(@PathVariable Long id) {
        rotaService.deletarRota(id);
        return ResponseEntity.noContent().build();
    }
}

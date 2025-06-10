/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package greenlong.controller;

import greenlong.dto.CalculoRotaResponseDTO;
import greenlong.dto.RotaResponseDTO;
import greenlong.dto.RotaRequestDTO;
import greenlong.service.RotaService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

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
    public ResponseEntity<?> criarRota(@Valid @RequestBody RotaRequestDTO dto) {
        try {
            RotaResponseDTO novaRota = rotaService.criarRota(dto);
            return new ResponseEntity<>(novaRota, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Retorna 404 se um dos IDs (caminhao, ponto) não for encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        }
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRota(@PathVariable Long id) {
        if (rotaService.deletarRota(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarRota(@PathVariable Long id, @Valid @RequestBody RotaRequestDTO dto) {
        try {
            RotaResponseDTO rotaAtualizada = rotaService.atualizarRota(id, dto);
            return ResponseEntity.ok(rotaAtualizada);
        } catch (IllegalArgumentException e) {
            // Retorna 404 Not Found se a rota ou algum dos IDs relacionados não existir
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/calcular")
    public ResponseEntity<?> calcularRota(
            @RequestParam Long caminhaoId,
            @RequestParam Long destinoId,
            @RequestParam String tipoResiduo) {
        try {
            CalculoRotaResponseDTO resposta = rotaService.calcularRota(caminhaoId, destinoId, tipoResiduo);
            return ResponseEntity.ok(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        }
    }
}

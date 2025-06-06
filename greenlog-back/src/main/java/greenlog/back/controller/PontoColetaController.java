/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlog.back.controller;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 04/06/2025
 * @brief Class PontoColetaController
 */

import greenlog.back.dto.PontoColetaCadastroDTO;
import greenlog.back.dto.PontoColetaResponseDTO;
import greenlog.back.service.PontoColetaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pontos")
public class PontoColetaController {

    private final PontoColetaService pontoColetaService;

    @Autowired
    public PontoColetaController(PontoColetaService pontoColetaService) {
        this.pontoColetaService = pontoColetaService;
    }

    @PostMapping
    public ResponseEntity<?> criarPontoColeta(@Valid @RequestBody PontoColetaCadastroDTO dto) {
        try {
            PontoColetaResponseDTO response = pontoColetaService.criarPontoColeta(dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<PontoColetaResponseDTO>> listarTodosPontosColeta() {
        List<PontoColetaResponseDTO> lista = pontoColetaService.listarTodosPontosColeta();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoColetaResponseDTO> buscarPontoColetaPorId(@PathVariable Long id) {
        return pontoColetaService.buscarPontoColetaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPontoColeta(@PathVariable Long id, @Valid @RequestBody PontoColetaCadastroDTO dto) {
         try {
            return pontoColetaService.atualizarPontoColeta(id, dto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPontoColeta(@PathVariable Long id) {
        if (pontoColetaService.deletarPontoColeta(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Adicione um ExceptionHandler similar ao do UsuarioController para erros de validação de DTO
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) { ... }
}

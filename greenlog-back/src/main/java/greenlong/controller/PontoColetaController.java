/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.controller;

import greenlong.dto.PontoColetaCadastroDTO;
import greenlong.dto.PontoColetaResponseDTO;
import greenlong.service.PontoColetaService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
 * @data 06/06/2025
 * @brief Class PontoColetaController
 */

@RestController
@RequestMapping("/api/pontos")
@RequiredArgsConstructor
public class PontoColetaController {

    private final PontoColetaService pontoColetaService;

    @PostMapping
    public ResponseEntity<PontoColetaResponseDTO> criarPontoColeta(@Valid @RequestBody PontoColetaCadastroDTO dto) {
        PontoColetaResponseDTO response = pontoColetaService.criarPontoColeta(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PontoColetaResponseDTO>> listarTodosPontosColeta() {
        return ResponseEntity.ok(pontoColetaService.listarTodosPontosColeta());
    }
    
    @GetMapping("/compativeis")
    public ResponseEntity<List<PontoColetaResponseDTO>> listarPontosCompativeis(@RequestParam Long caminhaoId) {
        List<PontoColetaResponseDTO> pontosCompativeis = pontoColetaService.listarPontosCompativeisPorCaminhao(caminhaoId);
        if (pontosCompativeis.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pontosCompativeis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoColetaResponseDTO> buscarPontoColetaPorId(@PathVariable Long id) {
        return pontoColetaService.buscarPontoColetaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PontoColetaResponseDTO> atualizarPontoColeta(@PathVariable Long id, @Valid @RequestBody PontoColetaCadastroDTO dto) {
        return pontoColetaService.atualizarPontoColeta(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPontoColeta(@PathVariable Long id) {
        if (pontoColetaService.deletarPontoColeta(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
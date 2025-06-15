/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.controller;

import greenlong.dto.CaminhaoDTO;
import greenlong.dto.CaminhaoRequestDTO;
import greenlong.service.CaminhaoService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class CaminhaoController
 */

@RestController
@RequestMapping("/api/caminhoes")
@RequiredArgsConstructor
public class CaminhaoController {

    private final CaminhaoService caminhaoService;

        @PostMapping
    public ResponseEntity<CaminhaoDTO> criarCaminhao(@Valid @RequestBody CaminhaoRequestDTO dto) {
        CaminhaoDTO novoCaminhao = caminhaoService.criarCaminhao(dto);
        return new ResponseEntity<>(novoCaminhao, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CaminhaoDTO>> listarTodosCaminhoes() {
        return ResponseEntity.ok(caminhaoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaminhaoDTO> buscarCaminhaoPorId(@PathVariable Long id) {
        return caminhaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CaminhaoDTO> atualizarCaminhao(@PathVariable Long id, @Valid @RequestBody CaminhaoRequestDTO dto) {
        return caminhaoService.atualizarCaminhao(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCaminhao(@PathVariable Long id) {
        caminhaoService.deletarCaminhao(id);
        return ResponseEntity.noContent().build();
    }
}
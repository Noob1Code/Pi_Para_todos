/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.controller;

import greenlong.dto.ConexaoDTO;
import greenlong.service.ConexaoService;
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
 * @brief Class ConexaoController
 */

@RestController
@RequestMapping("/api/conexoes")
@RequiredArgsConstructor
public class ConexaoController {

    private final ConexaoService conexaoService;

    @PostMapping
    public ResponseEntity<ConexaoDTO> criarConexao(@Valid @RequestBody ConexaoDTO dto) {
        ConexaoDTO novaConexao = conexaoService.criarConexao(dto);
        return new ResponseEntity<>(novaConexao, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ConexaoDTO>> listarTodasConexoes() {
        return ResponseEntity.ok(conexaoService.listarTodas());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConexaoDTO> atualizarConexao(@PathVariable Long id, @Valid @RequestBody ConexaoDTO dto) {
        return conexaoService.atualizarConexao(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConexao(@PathVariable Long id) {
        if (conexaoService.deletarConexao(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

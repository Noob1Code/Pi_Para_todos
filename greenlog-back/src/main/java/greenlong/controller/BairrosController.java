package greenlong.controller;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import greenlong.dto.BairroDTO;
import greenlong.dto.BairroRequestDTO;
import greenlong.service.BairroService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
 * @brief Class BairrosController
 */

@RestController
@RequestMapping("/api/bairros")
@RequiredArgsConstructor
public class BairrosController {

    @Autowired
    private BairroService bairroService;

    @GetMapping
    public ResponseEntity<List<BairroDTO>> listarTodos() {
        return ResponseEntity.ok(bairroService.listarTodos());
    }
    
    @PostMapping
    public ResponseEntity<BairroDTO> criar(@RequestBody BairroRequestDTO bairroRequest) {
        BairroDTO bairroSalvo = bairroService.criarBairro(bairroRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(bairroSalvo);
    }
    
     @PutMapping("/{id}")
    public ResponseEntity<BairroDTO> atualizarBairro(@PathVariable Long id, @Valid @RequestBody BairroRequestDTO bairroRequest) {
        return bairroService.atualizarBairro(id, bairroRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); 
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarBairro(@PathVariable Long id) {
        try {
            if (bairroService.deletarBairro(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}

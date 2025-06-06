/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package greenlog.back.controller;

import greenlog.back.model.Bairro;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import greenlog.back.repository.BairrosRepository;

/**
 *
 * @author Cansei2
 */
@RestController
@RequestMapping("/api/bairros")
public class BairrosController {

    private final BairrosRepository bairroRepository;

    public BairrosController(BairrosRepository cadastroDestinoRepository) {
        this.bairroRepository = cadastroDestinoRepository;
    }

    @GetMapping
    public List<Bairro> get() {
        return bairroRepository.findAll();
    }

    @PostMapping
    public Bairro post(@RequestBody Bairro bairro) {
        return bairroRepository.save(bairro);
    }

    @PutMapping("/{id}")
    public void atualizar(@PathVariable int id, @RequestBody Bairro rotaAtualizada) {
        Bairro rotaExistente = bairroRepository.findById(id).orElse(null);
        if (rotaExistente != null) {
            rotaExistente.setNome(rotaAtualizada.getNome());
            bairroRepository.save(rotaExistente);
        }

    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable int id) {
        bairroRepository.deleteById(id);
    }
    
}

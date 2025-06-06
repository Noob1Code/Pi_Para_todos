/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlog.back.controller;

import greenlog.back.model.Caminhao;
import greenlog.back.repository.CaminhaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *
 * @author Gabriel Braga <ninjagamer9795286@gmail.com>
 * @date 04/06/2025
 * @brief Class CaminhaoController
 */
@RestController
@RequestMapping("/api/caminhoes")
//@CrossOrigin(origins = "*") // permite acesso do Angular
public class CaminhaoController {

    @Autowired
    private CaminhaoRepository caminhaoRepository;

    @GetMapping
    public List<Caminhao> listar() {
        return caminhaoRepository.findAll();
    }

    @PostMapping
    public Caminhao salvar(@RequestBody Caminhao caminhao) {
        if (caminhaoRepository.existsByPlacaIgnoreCase(caminhao.getPlaca())) {
            throw new RuntimeException("Placa já cadastrada");
        }
        return caminhaoRepository.save(caminhao);
    }

    @PutMapping("/{id}")
    public Caminhao atualizar(@PathVariable Long id, @RequestBody Caminhao caminhao) {
        caminhao.setId(id);
        return caminhaoRepository.save(caminhao);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        caminhaoRepository.deleteById(id);
    }
}

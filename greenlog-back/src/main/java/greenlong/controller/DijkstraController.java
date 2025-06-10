/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package greenlong.controller;

import greenlong.dto.CaminhoDTO;
import greenlong.model.Bairro;
import greenlong.repository.BairrosRepository;
import greenlong.service.DijkstraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Cansei2
 */
@RestController
@RequestMapping("/api/caminhos")
@RequiredArgsConstructor
public class DijkstraController {

    private final DijkstraService dijkstraService;
    private final BairrosRepository bairroRepository;

    @GetMapping("/menor-caminho")
    public CaminhoDTO menorCaminho(@RequestParam Long destinoId) {
        // Origem fixa
        Bairro origem = bairroRepository.findByNomeIgnoreCase("Centro")
                .orElseThrow(() -> new RuntimeException("Bairro 'Centro' não encontrado"));
        return dijkstraService.calcularMenorCaminho(origem.getId(), destinoId);
    }
}

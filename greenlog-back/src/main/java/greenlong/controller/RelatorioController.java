/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.controller;

import greenlong.dto.ItinerariosPorDiaDTO;
import greenlong.service.RelatorioService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 14/06/2025
 * @brief Class RelatorioController
 */

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/itinerarios-por-dia")
    public ResponseEntity<List<ItinerariosPorDiaDTO>> getItinerariosPorDia() {
        List<ItinerariosPorDiaDTO> resultado = relatorioService.getItinerariosPorDia();
        return ResponseEntity.ok(resultado);
    }
}

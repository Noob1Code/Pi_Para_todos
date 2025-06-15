/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 15/06/2025
 * @brief Class ExtremosAnualComDetalhesMensaisDTO
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtremosAnualComDetalhesMensaisDTO {
    private CaminhaoDetalhesMensaisDTO maiorDistanciaAnual;
    private CaminhaoDetalhesMensaisDTO menorDistanciaAnual;
}

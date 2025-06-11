/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package greenlong.dto;

import greenlong.model.Bairro;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Cansei2
 */
@Data
@AllArgsConstructor
public class CalculoRotaResponseDTO {
    private List<Bairro> bairros;
    private List<ConexaoDTO> arestas;
    private double distanciaTotal;
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package greenlong.dto;

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
    private String caminhaoPlaca;
    private String destinoNome;
    private String tipoResiduo;
    private List<String> bairrosPercorridos;
    private List<ConexaoDTO> arestasPercorridas;
    private double distanciaTotal;
}

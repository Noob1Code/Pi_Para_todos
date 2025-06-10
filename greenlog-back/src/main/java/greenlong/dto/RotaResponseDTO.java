/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.dto;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class RotaDTO
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RotaResponseDTO {
    private Long id;
    private String caminhaoNome;
    private String destinoNome;
    private String tipoResiduo;
    private List<String> bairrosPercorridos;
    private List<ConexaoDTO> arestasPercorridas;
    private double distanciaTotal;
}

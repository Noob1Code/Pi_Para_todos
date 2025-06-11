/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.dto;

import greenlong.model.Bairro;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 10/06/2025
 * @brief Class RotaUpdateRequestDTO
 */

@Data
public class RotaUpdateRequestDTO {

    private Long id;
    private CaminhaoDTO caminhao;
    private Bairro destino;
    private String tipoResiduo;
    private List<String> bairrosPercorridos;
    private List<ConexaoDTO> arestasPercorridas;
    private double distanciaTotal;
}
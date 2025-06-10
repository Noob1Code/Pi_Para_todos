/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class RotaRequestDTO
 */

@Data
public class RotaRequestDTO {
    private Long caminhaoId;
    private Long destinoId;
    private String tipoResiduo;
    private List<String> bairrosPercorridos;
    private List<Long> conexaoIds;
    private double distanciaTotal;
}
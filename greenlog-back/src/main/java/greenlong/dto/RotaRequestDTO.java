/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class RotaRequestDTO
 */

@Data
public class RotaRequestDTO {

    @NotNull(message = "O ID do caminhão é obrigatório")
    private Long caminhaoId;

    @NotNull(message = "O ID do ponto de coleta de origem é obrigatório")
    private Long origemId;

    @NotNull(message = "O ID do ponto de coleta de destino é obrigatório")
    private Long destinoId;

    @NotNull(message = "A data é obrigatória")
    private Date data;
}
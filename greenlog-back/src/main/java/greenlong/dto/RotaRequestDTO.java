/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class RotaRequestDTO
 */

@Data
public class RotaRequestDTO {
    @Data
    public static class ObjetoId {
        @NotNull(message = "O ID é obrigatório dentro do objeto.")
        private Long id;
    }

    @NotNull(message = "O objeto do caminhão é obrigatório")
    private ObjetoId caminhaoId;

    @NotNull(message = "O objeto do bairro de destino é obrigatório")
    private ObjetoId destinoId;


    @NotBlank(message = "O tipo de resíduo é obrigatório")
    private String tipoResiduo;
}
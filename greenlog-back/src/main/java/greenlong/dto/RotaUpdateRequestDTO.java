
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.dto;

import greenlong.model.Bairro;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 10/06/2025
 * @brief Class RotaUpdateRequestDTO
 */

@Data
public class RotaUpdateRequestDTO {

    @NotNull(message = "O objeto do caminhão é obrigatório na atualização.")
    @Valid 
    private CaminhaoDTO caminhao;

    @NotNull(message = "O objeto do bairro de destino é obrigatório na atualização.")
    @Valid
    private Bairro destino;

    @NotBlank(message = "O tipo de resíduo é obrigatório na atualização.")
    private String tipoResiduo;
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class BairroRequestDTO
 */

@Data
public class BairroRequestDTO {

    @NotBlank(message = "O nome do bairro é obrigatório")
    @Size(max = 100, message = "O nome do bairro não pode exceder 100 caracteres")
    private String nome;
}
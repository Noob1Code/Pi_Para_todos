/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class ConexaoDTO
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConexaoDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BairroDTO { 
        @NotNull(message = "O ID é obrigatório")
        private Long id;
        private String nome; 
    }

     private Long id;

    @JsonProperty("nome")
    @NotBlank(message = "O nome da rua é obrigatório")
    private String rua;

    @NotNull(message = "A origem é obrigatória")
    @Valid
    private BairroDTO origem; 

    @NotNull(message = "O destino é obrigatório")
    @Valid
    private BairroDTO destino; 
    
    @JsonProperty("distancia")
    private Integer quilometros;
}

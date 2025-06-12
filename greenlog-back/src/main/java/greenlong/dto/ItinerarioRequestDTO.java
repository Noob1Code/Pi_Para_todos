/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 12/06/2025
 * @brief Class ItinerarioRequestDTO
 */

@Data
public class ItinerarioRequestDTO {

    @Data
    public static class RotaIdDTO {
        @NotNull(message = "O ID da Rota é obrigatório.")
        private Long id;
    }

    @NotNull(message = "A data é obrigatória.")
    private LocalDate data;

    @NotNull(message = "O objeto da rota é obrigatório.")
    @Valid
    private RotaIdDTO rotaId;
}
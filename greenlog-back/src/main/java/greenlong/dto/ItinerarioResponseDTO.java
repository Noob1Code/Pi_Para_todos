/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 12/06/2025
 * @brief Class ItinerarioResponseDTO
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItinerarioResponseDTO {
    private Long id;
    private LocalDate data;
    private RotaResponseDTO rota;
}

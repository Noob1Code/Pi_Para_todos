package greenlong.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class CaminhaoRequestDTO
 */

@Data
public class CaminhaoRequestDTO {

    @NotBlank(message = "A placa é obrigatória")
    @Size(min = 7, max = 10, message = "A placa deve ter entre 7 e 10 caracteres")
    private String placa;

    @NotBlank(message = "O nome do motorista é obrigatório")
    @Size(max = 150, message = "O nome do motorista não pode exceder 150 caracteres")
    private String motorista;

    @NotNull(message = "A capacidade é obrigatória")
    private Integer capacidade;

    @NotEmpty(message = "O caminhão deve coletar pelo menos um tipo de resíduo.")
    private List<String> residuos;
}
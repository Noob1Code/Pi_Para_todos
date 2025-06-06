package greenlog.back.dto;
/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 02/06/2025
 * @brief Class UsuarioService
 */
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "Username é obrigatório")
    private String username;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;
}
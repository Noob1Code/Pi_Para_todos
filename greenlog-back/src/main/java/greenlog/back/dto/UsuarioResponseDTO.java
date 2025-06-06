package greenlog.back.dto;
/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 02/06/2025
 * @brief Class UsuarioService
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String username;
    private String nome;
    private String cpf;
}
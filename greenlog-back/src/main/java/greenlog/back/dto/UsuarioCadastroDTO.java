package greenlog.back.dto;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 02/06/2025
 * @brief Class Usuario
 */

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioCadastroDTO {

    @NotBlank(message = "Username é obrigatório")
    @Size(min = 3, max = 50, message = "Username deve ter entre 3 e 50 caracteres")
    private String username;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos.") 
    private String cpf;

    @NotNull(message = "Idade é obrigatória")
    @Min(value = 1, message = "Idade deve ser no mínimo 1")
    private Integer idade;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 dígitos numéricos.")
    private String telefone;

    @NotBlank(message = "Gênero é obrigatório")
    private String genero;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String senha;
}
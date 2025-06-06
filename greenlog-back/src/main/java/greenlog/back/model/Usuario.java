package greenlog.back.model;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 02/06/2025
 * @brief Class Usuario
 */

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Username é obrigatório")
    @Size(min = 3, max = 50, message = "Username deve ter entre 3 e 50 caracteres")
    @Column(nullable = false, unique = true, length = 50) // USERNAME PARA LOGIN
    private String username;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos.") // << ALTERADO AQUI
    @Column(nullable = false, unique = true, length = 11) // << ALTERADO AQUI (length)
    private String cpf;

    @NotNull(message = "Idade é obrigatória")
    @Min(value = 1, message = "Idade deve ser no mínimo 1")
    @Column(nullable = false)
    private Integer idade;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter de 10 a 11 dígitos.") // << ALTERADO AQUI
    @Column(nullable = false, length = 11) // << ALTERADO AQUI (length para 11, se for só número)
    private String telefone;

    @NotBlank(message = "Gênero é obrigatório")
    @Column(nullable = false, length = 20)
    private String genero;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    @Column(nullable = false)
    private String senha; // Lembre-se de HASHEAR esta senha
}
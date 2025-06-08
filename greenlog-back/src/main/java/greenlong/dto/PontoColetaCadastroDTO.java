/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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
 * @brief Class PontoColetaCadastroDTO
 */

@Data
public class PontoColetaCadastroDTO {

    @Data
    public static class BairroIdDTO {
        @NotNull(message = "O ID do bairro é obrigatório")
        private Long id;
    }

    @NotNull(message = "O objeto Bairro é obrigatório")
    @Valid 
    private BairroIdDTO bairro;

    @NotBlank(message = "Nome do ponto de coleta é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Nome do responsável é obrigatório")
    private String responsavel;

    private String telefoneResponsavel;

    @Email(message = "Email do responsável deve ser um email válido")
    private String emailResponsavel;

    private String endereco;

    private String horarioFuncionamento;

    @NotEmpty(message = "Pelo menos um tipo de resíduo deve ser informado")
    private List<@NotBlank(message = "Tipo de resíduo não pode ser vazio") String> tiposResiduosAceitos;
}
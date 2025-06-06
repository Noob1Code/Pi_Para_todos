/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlog.back.dto;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 04/06/2025
 * @brief Class PontoColetaCadastroDTO
 */

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class PontoColetaCadastroDTO {

    // NOVO: Uma classe interna para representar o objeto bairro que vem do frontend
    @Data
    public static class BairroIdDTO {
        @NotNull(message = "O ID do bairro dentro do objeto bairro é obrigatório")
        private Long id;
    }

    // ALTERADO: A propriedade bairroId foi trocada por um objeto que contém o ID.
    @NotNull(message = "O objeto Bairro é obrigatório")
    @Valid // @Valid garante que as validações dentro de BairroIdDTO (como @NotNull) sejam checadas
    private BairroIdDTO bairro;

    @NotBlank(message = "Nome do ponto de coleta é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    // ... o resto da classe continua igual ...
    @NotBlank(message = "Nome do responsável é obrigatório")
    @Size(min = 3, max = 100, message = "Nome do responsável deve ter entre 3 e 100 caracteres")
    private String responsavel;

    @Size(max = 20, message = "Telefone do responsável deve ter no máximo 20 caracteres")
    private String telefoneResponsavel;

    @Email(message = "Email do responsável deve ser válido")
    @Size(max = 100, message = "Email do responsável deve ter no máximo 100 caracteres")
    private String emailResponsavel;

    @Size(max = 255, message = "Endereço deve ter no máximo 255 caracteres")
    private String endereco;

    @Size(max = 100, message = "Horário de funcionamento deve ter no máximo 100 caracteres")
    private String horarioFuncionamento;

    @NotEmpty(message = "Pelo menos um tipo de resíduo deve ser informado")
    private List<@NotBlank(message = "Tipo de resíduo não pode ser vazio") String> tiposResiduosAceitos;
}
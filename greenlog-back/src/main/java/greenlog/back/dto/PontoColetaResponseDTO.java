/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlog.back.dto;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 04/06/2025
 * @brief Class PontoColetaResponseDTO
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PontoColetaResponseDTO {

    // NOVO: Classe interna para representar o bairro na resposta
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BairroDTO {
        private Integer id; // O ID do bairro na sua entidade é Integer
        private String nome;
    }

    private Long id;
    // ALTERADO: a propriedade bairroId foi trocada pelo objeto BairroDTO
    private BairroDTO bairro;
    private String nome;
    private String responsavel;
    private String telefoneResponsavel;
    private String emailResponsavel;
    private String endereco;
    private String horarioFuncionamento;
    private List<String> tiposResiduosAceitos;
}
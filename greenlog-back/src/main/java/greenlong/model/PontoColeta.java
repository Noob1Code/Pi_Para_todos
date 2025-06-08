package greenlong.model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class PontoColeta
 */

@Entity
@Table(name = "pontos_coleta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PontoColeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bairro_id")
    private Bairro bairro;

    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String responsavel;

    @Column(length = 20)
    private String telefoneResponsavel;

    @Column(length = 100)
    private String emailResponsavel;

    @Column(length = 255)
    private String endereco;

    @Column(length = 100)
    private String horarioFuncionamento;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ponto_coleta_tipos_residuos", joinColumns = @JoinColumn(name = "ponto_coleta_id"))
    @Column(name = "tipo_residuo")
    private List<String> tiposResiduosAceitos;
}

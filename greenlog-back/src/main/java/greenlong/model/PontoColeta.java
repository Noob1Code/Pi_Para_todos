package greenlong.model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "ponto_coleta_residuos",
        joinColumns = @JoinColumn(name = "ponto_coleta_id"),
        inverseJoinColumns = @JoinColumn(name = "residuo_id")
    )
    private List<Residuo> tiposResiduosAceitos;
}

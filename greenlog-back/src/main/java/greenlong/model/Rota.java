/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;
/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class Rota
 */

@Entity
@Table(name = "rotas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Caminhao caminhao;

    @ManyToOne
    private Bairro destino;

    private String tipoResiduo;

    private double distanciaTotal;

    @ElementCollection
    private List<String> bairrosPercorridos;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rota_conexao",
        joinColumns = @JoinColumn(name = "rota_id"),
        inverseJoinColumns = @JoinColumn(name = "conexao_id")
    )
    private List<Conexao> arestasPercorridas;
}
package greenlong.model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class Caminhao
 */

@Entity
@Table(name = "caminhao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Caminhao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @Column(nullable = false, length = 150)
    private String motorista;

    @Column
    private Integer capacidade;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "caminhao_residuos", joinColumns = @JoinColumn(name = "caminhao_id"))
    @Column(name = "residuo", nullable = false)
    private List<String> residuos;
}
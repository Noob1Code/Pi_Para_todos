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
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "caminhao_residuos",
        joinColumns = @JoinColumn(name = "caminhao_id"),
        inverseJoinColumns = @JoinColumn(name = "residuo_id")
    )
    private List<Residuo> residuos;
}
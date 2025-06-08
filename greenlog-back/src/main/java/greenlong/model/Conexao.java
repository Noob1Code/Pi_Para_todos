/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class Conexao
 */

@Entity
@Table(name = "conexao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conexao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String rua;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origem_bairro_id") 
    private Bairro origem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_bairro_id") 
    private Bairro destino;
    
    private Integer quilometros;
}

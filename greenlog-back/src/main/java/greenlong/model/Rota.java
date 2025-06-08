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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caminhao_id")
    private Caminhao caminhao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pontos_coleta_origem_id")
    private PontoColeta pontoColetaOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pontos_coleta_destino_id")
    private PontoColeta pontoColetaDestino;

    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
}
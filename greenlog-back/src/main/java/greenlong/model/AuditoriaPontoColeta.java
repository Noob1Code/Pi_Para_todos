/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 09/06/2025
 * @brief Class AuditoriaPontoColeta
 */

@Entity
@Table(name = "auditoria_ponto_coleta")
@Data
@NoArgsConstructor
public class AuditoriaPontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_alteracao", nullable = false)
    private LocalDateTime data;

    @Column(name = "como_era_antes", columnDefinition = "TEXT")
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String comoEraAntes;

    @Column(name = "como_ficou", columnDefinition = "TEXT")
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String comoFicou;

    @Column(name = "acao", nullable = false, length = 10)
    private String acao;

    @Column(name = "ponto_coleta_id")
    private Long pontoColetaId;

    public AuditoriaPontoColeta(Long pontoColetaId, String antes, String depois, String acao) {
        this.pontoColetaId = pontoColetaId;
        this.data = LocalDateTime.now();
        this.comoEraAntes = antes;
        this.comoFicou = depois;
        this.acao = acao;
    }
}
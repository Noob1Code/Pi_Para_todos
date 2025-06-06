/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package greenlog.back.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Date;

/**
 *
 * @author Cansei2
 */
@Entity
@Table(name = "rotas")
public class Rota {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rota_seq")
    @SequenceGenerator(name = "rota_seq", sequenceName = "rotas_SEQ", allocationSize = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "caminhao_id", referencedColumnName = "id") // chave estrangeira
    private Caminhao caminhao;

    private Date data;
    
    @ManyToOne
    @JsonProperty("origem")
    @JoinColumn(name = "pontos_coleta_origem_id", referencedColumnName = "id") // chave estrangeira
    private PontoColeta pontoColetaOrigem;
    
    @ManyToOne
    @JsonProperty("destino")
    @JoinColumn(name = "pontos_coleta_destino_id", referencedColumnName = "id") // chave estrangeira
    private PontoColeta pontoColetaDestino;

    public Rota() {
    }

    public Rota(Integer id, Caminhao caminhao, Date data, PontoColeta pontoColetaDestino, PontoColeta pontoColetaOrigem) {
        this.id = id;
        this.caminhao = caminhao;
        this.data = data;
        this.pontoColetaOrigem = pontoColetaOrigem;
        this.pontoColetaDestino = pontoColetaDestino;
    }

    public Caminhao getCaminhao() {
        return caminhao;
    }

    public void setCaminhao(Caminhao caminhao) {
        this.caminhao = caminhao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PontoColeta getPontoColetaOrigem() {
        return pontoColetaOrigem;
    }

    public void setPontoColetaOrigem(PontoColeta pontoColetaOrigem) {
        this.pontoColetaOrigem = pontoColetaOrigem;
    }

    public PontoColeta getPontoColetaDestino() {
        return pontoColetaDestino;
    }

    public void setPontoColetaDestino(PontoColeta pontoColetaDestino) {
        this.pontoColetaDestino = pontoColetaDestino;
    }
    
    
}

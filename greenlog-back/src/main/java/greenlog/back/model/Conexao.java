/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package greenlog.back.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Date;


/**
 *
 * @author Cansei2
 */
@Entity
@Table(name = "conexao")
public class Conexao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conexao_seq")
    @SequenceGenerator(name = "conexao_seq", sequenceName = "conexao_SEQ", allocationSize = 1)
    private Integer id;
    private String rua;
    private String origem;
    private String destino;
    private Integer quilometros;

    public Conexao(){
    }

    public Conexao(Integer id, String nome, String origem, String destino, Integer quilometros) {
        this.id = id;
        this.rua = nome;
        this.origem = origem;
        this.destino = destino;
        this.quilometros = quilometros;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Integer getQuilometros() {
        return quilometros;
    }

    public void setQuilometros(Integer quilometros) {
        this.quilometros = quilometros;
    }
    
    
}

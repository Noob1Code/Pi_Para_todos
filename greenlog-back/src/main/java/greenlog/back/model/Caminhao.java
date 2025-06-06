/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package greenlog.back.model;

import jakarta.persistence.*;
/**
 *
 * @author Gabriel Braga <ninjagamer9795286@gmail.com>
 * @date 04/06/2025
 * @brief Class Caminhao
 */
@Entity
@Table(name = "caminhao")
public class Caminhao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caminhao_seq")
    @SequenceGenerator(name = "caminhao_seq", sequenceName = "caminhao_SEQ", allocationSize = 1)
    private Long id;

    private String placa;
    private String motorista;
    private Integer capacidade;

    private String residuos;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getMotorista() { return motorista; }
    public void setMotorista(String motorista) { this.motorista = motorista; }

    public Integer getCapacidade() { return capacidade; }
    public void setCapacidade(Integer capacidade) { this.capacidade = capacidade; }

    public String getResiduos() { return residuos; }
    public void setResiduos(String residuos) { this.residuos = residuos; }
}


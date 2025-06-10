package greenlong.dto;

import greenlong.model.Bairro;
import greenlong.model.Conexao;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Cansei2
 */

@Data
@AllArgsConstructor
public class CaminhoDTO {
    private List<Bairro> bairros;
    private List<Conexao> arestas;
    private double distanciaTotal;
}



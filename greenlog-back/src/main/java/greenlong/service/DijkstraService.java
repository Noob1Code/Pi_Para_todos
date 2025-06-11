/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package greenlong.service;

import greenlong.dto.CaminhoDTO;
import greenlong.model.Bairro;
import greenlong.model.Conexao;
import greenlong.repository.BairrosRepository;
import greenlong.repository.ConexaoRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author Cansei2
 */
@Service
@RequiredArgsConstructor
public class DijkstraService {

    private final ConexaoRepository conexaoRepository;
    private final BairrosRepository bairroRepository;

    public CaminhoDTO calcularMenorCaminho(Long origemId, Long destinoId) {
        Map<Long, Bairro> bairroMap = bairroRepository.findAll().stream()
                .collect(Collectors.toMap(Bairro::getId, Function.identity()));

        Map<Long, List<Conexao>> adjacencia = new HashMap<>();
        for (Conexao conexao : conexaoRepository.findAll()) {
            adjacencia.computeIfAbsent(conexao.getOrigem().getId(), k -> new ArrayList<>()).add(conexao);
            adjacencia.computeIfAbsent(conexao.getDestino().getId(), k -> new ArrayList<>()).add(conexao);
        }

        Map<Long, Double> distancias = new HashMap<>();
        Map<Long, Conexao> predecessores = new HashMap<>();
        
        for (Long bairroId : bairroMap.keySet()) {
            distancias.put(bairroId, Double.MAX_VALUE);
        }
        distancias.put(origemId, 0.0);

        PriorityQueue<Long> fila = new PriorityQueue<>(Comparator.comparing(distancias::get));
        fila.add(origemId);

        while (!fila.isEmpty()) {
            Long bairroAtualId = fila.poll();

            if (bairroAtualId.equals(destinoId)) break;
            
            if (!adjacencia.containsKey(bairroAtualId)) continue;

            for (Conexao conexao : adjacencia.get(bairroAtualId)) {
                Long vizinhoId = conexao.getOrigem().getId().equals(bairroAtualId) ? conexao.getDestino().getId() : conexao.getOrigem().getId();
                double novaDist = distancias.get(bairroAtualId) + conexao.getQuilometros();
                
                if (novaDist < distancias.get(vizinhoId)) {
                    distancias.put(vizinhoId, novaDist);
                    predecessores.put(vizinhoId, conexao);
                    fila.remove(vizinhoId);
                    fila.add(vizinhoId);
                }
            }
        }
        
        List<Bairro> caminhoBairros = new ArrayList<>();
        List<Conexao> arestasUsadas = new ArrayList<>();
        Long passoId = destinoId;

        if (predecessores.get(passoId) == null && !passoId.equals(origemId)) {
            return new CaminhoDTO(Collections.emptyList(), Collections.emptyList(), -1);
        }

        while (passoId != null && !passoId.equals(origemId)) {
            caminhoBairros.add(0, bairroMap.get(passoId));
            Conexao aresta = predecessores.get(passoId);
            if(aresta != null) {
                arestasUsadas.add(0, aresta);
                passoId = aresta.getOrigem().getId().equals(passoId) ? aresta.getDestino().getId() : aresta.getOrigem().getId();
            } else {
                break;
            }
        }
        
        if(bairroMap.containsKey(origemId)){
            caminhoBairros.add(0, bairroMap.get(origemId));
        }

        double distanciaFinal = distancias.get(destinoId);
        if(distanciaFinal == Double.MAX_VALUE) distanciaFinal = -1;
        
        return new CaminhoDTO(caminhoBairros, arestasUsadas, distanciaFinal);
    }
}

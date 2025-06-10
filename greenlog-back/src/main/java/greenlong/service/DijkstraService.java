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
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author Cansei2
 */
@Service
@RequiredArgsConstructor
public class DijkstraService {

    private final BairrosRepository bairroRepository;
    private final ConexaoRepository conexaoRepository;

    public CaminhoDTO calcularMenorCaminho(Long origemId, Long destinoId) {
        List<Bairro> bairros = bairroRepository.findAll();
        List<Conexao> arestas = conexaoRepository.findAll();

        Map<Long, Double> distancias = new HashMap<>();
        Map<Long, Long> predecessores = new HashMap<>();
        Set<Long> visitados = new HashSet<>();

        for (Bairro b : bairros) {
            distancias.put(b.getId(), Double.MAX_VALUE);
        }
        distancias.put(origemId, 0.0);

        PriorityQueue<Long> fila = new PriorityQueue<>(Comparator.comparing(distancias::get));
        fila.add(origemId);

        while (!fila.isEmpty()) {
            Long atual = fila.poll();

            if (!visitados.add(atual)) {
                continue;
            }

            for (Conexao conexao : arestas) {
                Long vizinho = null;
                if (conexao.getOrigem().getId().equals(atual)) {
                    vizinho = conexao.getDestino().getId();
                } else if (conexao.getDestino().getId().equals(atual)) {
                    vizinho = conexao.getOrigem().getId();
                }

                if (vizinho != null && !visitados.contains(vizinho)) {
                    double novaDist = distancias.get(atual) + conexao.getQuilometros();
                    if (novaDist < distancias.get(vizinho)) {
                        distancias.put(vizinho, novaDist);
                        predecessores.put(vizinho, atual);
                        fila.add(vizinho);
                    }
                }
            }
        }

        // Reconstrução do caminho
        List<Bairro> caminho = new ArrayList<>();
        List<Conexao> arestasUsadas = new ArrayList<>();
        double[] distanciaTotal = {0.0}; 

        Long atual = destinoId;
        while (predecessores.containsKey(atual)) {
            Long anterior = predecessores.get(atual);

            final Long atualFinal = atual; // 👈 CORREÇÃO
            Bairro b = bairros.stream().filter(x -> x.getId().equals(atualFinal)).findFirst().orElse(null);

            if (b != null) {
                caminho.add(0, b);
            }

            final Long anteriorFinal = anterior; // 👈 CORREÇÃO
            Optional<Conexao> aresta = arestas.stream().filter(a
                    -> (a.getOrigem().getId().equals(anteriorFinal) && a.getDestino().getId().equals(atualFinal))
                    || (a.getOrigem().getId().equals(atualFinal) && a.getDestino().getId().equals(anteriorFinal))
            ).findFirst();

            aresta.ifPresent(a -> {
                arestasUsadas.add(0, a);
                distanciaTotal[0] += a.getQuilometros(); // 👈 usa o índice
            });

            atual = anterior;
        }

        Bairro origem = bairros.stream().filter(x -> x.getId().equals(origemId)).findFirst().orElse(null);
        if (origem != null) {
            caminho.add(0, origem);
        }

        return new CaminhoDTO(caminho, arestasUsadas, distanciaTotal[0]);
    }
}

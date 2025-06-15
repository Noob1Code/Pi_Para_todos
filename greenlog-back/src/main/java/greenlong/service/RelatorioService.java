/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.service;

import greenlong.dto.CaminhaoDTO;
import greenlong.dto.CaminhaoDistanciaDTO;
import greenlong.dto.DistanciaExtremosDTO;
import greenlong.dto.ItinerariosPorDiaDTO;
import greenlong.repository.CaminhaoRepository;
import greenlong.repository.ItinerarioRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 14/06/2025
 * @brief Class RelatorioService
 */

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final ItinerarioRepository itinerarioRepository;
    private final CaminhaoRepository caminhaoRepository;

    @Transactional(readOnly = true)
    public List<ItinerariosPorDiaDTO> getItinerariosPorDia() {
        return itinerarioRepository.countItinerariosPorDiaDaSemana().stream()
                .map(proj -> new ItinerariosPorDiaDTO(proj.getDiaDaSemana(), proj.getQuantidadeDeItinerarios()))
                .collect(Collectors.toList());
    }
    
     /**
     * Busca os caminhões com a maior e a menor distância percorrida em um ano.
     * @param ano O ano para o qual o relatório será gerado.
     * @return Um DTO contendo os dados do caminhão com maior e menor distância.
     */
    @Transactional(readOnly = true)
    public DistanciaExtremosDTO getExtremosDistanciaPorAno(Integer ano) {
        // 1. Busca os dados brutos do banco
        List<CaminhaoRepository.CaminhaoDistanciaProjection> projecoes = caminhaoRepository.findDistanciasAnuaisPorCaminhao(ano);

        // Se não houver dados para o ano, retorna nulo
        if (projecoes.isEmpty()) {
            return null;
        }

        // 2. Converte as projeções para uma lista de DTOs
        List<CaminhaoDistanciaDTO> distanciasPorCaminhao = new ArrayList<>();
        for (CaminhaoRepository.CaminhaoDistanciaProjection p : projecoes) {
            CaminhaoDTO caminhaoDTO = new CaminhaoDTO(p.getId(), p.getPlaca(), p.getMotorista(), p.getCapacidade(), null);
            distanciasPorCaminhao.add(new CaminhaoDistanciaDTO(caminhaoDTO, p.getQuilometrosPercorridos()));
        }

        // 3. Encontra o máximo e o mínimo na lista
        CaminhaoDistanciaDTO maiorDistancia = distanciasPorCaminhao.stream()
                .max(Comparator.comparing(CaminhaoDistanciaDTO::getQuilometrosPercorridos))
                .orElse(null);

        CaminhaoDistanciaDTO menorDistancia = distanciasPorCaminhao.stream()
                .min(Comparator.comparing(CaminhaoDistanciaDTO::getQuilometrosPercorridos))
                .orElse(null);

        return new DistanciaExtremosDTO(maiorDistancia, menorDistancia);
    }

}

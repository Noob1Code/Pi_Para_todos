/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.service;


import greenlong.dto.CaminhaoDetalhesMensaisDTO;
import greenlong.dto.ExtremosAnualComDetalhesMensaisDTO;
import greenlong.dto.ItinerariosPorDiaDTO;
import greenlong.repository.CaminhaoRepository;
import greenlong.repository.CaminhaoRepository.CaminhaoTotalAnualProjection;
import greenlong.repository.CaminhaoRepository.DistanciaMensalProjection;
import greenlong.repository.ItinerarioRepository;
import java.util.Arrays;
import java.util.Comparator;

import java.util.List;
import java.util.Optional;
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
    
    @Transactional(readOnly = true)
    public ExtremosAnualComDetalhesMensaisDTO getDetalhesMensaisParaExtremosAnuais(Integer ano) {
        // 1. Encontra os totais anuais de todos os caminhões
        List<CaminhaoTotalAnualProjection> totaisAnuais = caminhaoRepository.findTotaisAnuaisPorCaminhao(ano);
        if (totaisAnuais.isEmpty()) {
            return null;
        }

        // 2. Identifica os caminhões com o maior e menor total anual
        Optional<CaminhaoTotalAnualProjection> maiorOpt = totaisAnuais.stream()
                .max(Comparator.comparing(CaminhaoTotalAnualProjection::getTotalAnual));

        // Para o menor, filtramos para incluir apenas caminhões que rodaram
        Optional<CaminhaoTotalAnualProjection> menorOpt = totaisAnuais.stream()
                .filter(c -> c.getTotalAnual() > 0)
                .min(Comparator.comparing(CaminhaoTotalAnualProjection::getTotalAnual));

        // 3. Busca os detalhes mensais para esses dois caminhões
        CaminhaoDetalhesMensaisDTO maiorDTO = maiorOpt
                .map(proj -> criarDetalhesDTO(ano, proj))
                .orElse(null);

        CaminhaoDetalhesMensaisDTO menorDTO = menorOpt
                .map(proj -> criarDetalhesDTO(ano, proj))
                .orElse(null);
        
        // Se o maior e o menor forem o mesmo caminhão (caso de apenas 1 caminhão ter rodado)
        if(maiorDTO != null && menorDTO != null && maiorDTO.getPlaca().equals(menorDTO.getPlaca())){
            menorDTO = null; // Evita duplicidade na resposta
        }

        return new ExtremosAnualComDetalhesMensaisDTO(maiorDTO, menorDTO);
    }

    private CaminhaoDetalhesMensaisDTO criarDetalhesDTO(Integer ano, CaminhaoTotalAnualProjection proj) {
        // Busca o histórico mensal do caminhão
        List<DistanciaMensalProjection> distancias = caminhaoRepository.findDistanciasMensaisParaCaminhao(ano, proj.getId());
        
        // Formata os dados em uma lista de 12 meses
        Double[] mensais = new Double[12];
        Arrays.fill(mensais, 0.0);
        for (DistanciaMensalProjection d : distancias) {
            mensais[d.getMes() - 1] = d.getDistanciaMensal(); // Mês 1 vai para o índice 0
        }

        return new CaminhaoDetalhesMensaisDTO(
                proj.getPlaca(),
                proj.getMotorista(),
                proj.getTotalAnual(),
                Arrays.asList(mensais)
        );
    }
}

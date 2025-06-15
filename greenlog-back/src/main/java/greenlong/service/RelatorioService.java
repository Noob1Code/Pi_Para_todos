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
        List<CaminhaoTotalAnualProjection> totaisAnuais = caminhaoRepository.findTotaisAnuaisPorCaminhao(ano);
        if (totaisAnuais.isEmpty()) {
            return null;
        }

        Optional<CaminhaoTotalAnualProjection> maiorOpt = totaisAnuais.stream()
                .max(Comparator.comparing(CaminhaoTotalAnualProjection::getTotalAnual));

        Optional<CaminhaoTotalAnualProjection> menorOpt = totaisAnuais.stream()
                .filter(c -> c.getTotalAnual() > 0)
                .min(Comparator.comparing(CaminhaoTotalAnualProjection::getTotalAnual));

        CaminhaoDetalhesMensaisDTO maiorDTO = maiorOpt
                .map(proj -> criarDetalhesDTO(ano, proj))
                .orElse(null);

        CaminhaoDetalhesMensaisDTO menorDTO = menorOpt
                .map(proj -> criarDetalhesDTO(ano, proj))
                .orElse(null);

        if(maiorDTO != null && menorDTO != null && maiorDTO.getPlaca().equals(menorDTO.getPlaca())){
            menorDTO = null;
        }

        return new ExtremosAnualComDetalhesMensaisDTO(maiorDTO, menorDTO);
    }

    private CaminhaoDetalhesMensaisDTO criarDetalhesDTO(Integer ano, CaminhaoTotalAnualProjection proj) {
        List<DistanciaMensalProjection> distancias = caminhaoRepository.findDistanciasMensaisParaCaminhao(ano, proj.getId());

        Double[] mensais = new Double[12];
        Arrays.fill(mensais, 0.0);
        for (DistanciaMensalProjection d : distancias) {
            mensais[d.getMes() - 1] = d.getDistanciaMensal();
        }

        return new CaminhaoDetalhesMensaisDTO(
                proj.getPlaca(),
                proj.getMotorista(),
                proj.getTotalAnual(),
                Arrays.asList(mensais)
        );
    }
}

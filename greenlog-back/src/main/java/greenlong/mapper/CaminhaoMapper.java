/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.mapper;

import greenlong.dto.CaminhaoDTO;
import greenlong.model.Caminhao;
import greenlong.model.Residuo;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 12/06/2025
 * @brief Class CaminhaoMapper
 */

@Component
public class CaminhaoMapper {

    public CaminhaoDTO toResponseDTO(Caminhao caminhao) {
        if (caminhao == null) {
            return null;
        }

        List<String> nomesResiduos = caminhao.getResiduos().stream()
                .map(Residuo::getNome)
                .collect(Collectors.toList());

        return new CaminhaoDTO(
            caminhao.getId(),
            caminhao.getPlaca(),
            caminhao.getMotorista(),
            caminhao.getCapacidade(),
            nomesResiduos
        );
    }
}

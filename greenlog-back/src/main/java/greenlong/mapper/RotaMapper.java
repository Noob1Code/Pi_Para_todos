package greenlong.mapper;

import greenlong.dto.BairroDTO;
import greenlong.dto.ConexaoDTO;
import greenlong.dto.RotaResponseDTO;
import greenlong.model.Bairro;
import greenlong.model.Conexao;
import greenlong.model.Rota;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 12/06/2025
 * @brief Mapper para converter a entidade Rota em seu DTO de resposta.
 */
@Component
@RequiredArgsConstructor
public class RotaMapper {

    private final CaminhaoMapper caminhaoMapper;

    public RotaResponseDTO toResponseDTO(Rota rota) {
        if (rota == null) {
            return null;
        }
        
        var caminhaoDTO = caminhaoMapper.toResponseDTO(rota.getCaminhao());

        // 2. CONVERSÃO DO BAIRRO DE DESTINO PARA O DTO CORRETO
        BairroDTO destinoDTO = null;
        if (rota.getDestino() != null) {
            Bairro destino = rota.getDestino();
            // Cria uma instância do BairroDTO principal
            destinoDTO = new BairroDTO(destino.getId(), destino.getNome()); 
        }

        List<ConexaoDTO> arestasDTO = rota.getArestasPercorridas().stream()
                .map(this::toConexaoDTO)
                .collect(Collectors.toList());

        return new RotaResponseDTO(
                rota.getId(),
                caminhaoDTO,
                destinoDTO, // 3. AGORA OS TIPOS SÃO COMPATÍVEIS
                rota.getTipoResiduo(),
                rota.getBairrosPercorridos(),
                arestasDTO,
                rota.getDistanciaTotal()
        );
    }

    private ConexaoDTO toConexaoDTO(Conexao con) {
        // Aqui, usamos o BairroDTO aninhado, pois ele pertence à ConexaoDTO
        ConexaoDTO.BairroDTO origemDTO = (con.getOrigem() != null) 
            ? new ConexaoDTO.BairroDTO(con.getOrigem().getId(), con.getOrigem().getNome()) 
            : null;

        ConexaoDTO.BairroDTO destinoDTO = (con.getDestino() != null) 
            ? new ConexaoDTO.BairroDTO(con.getDestino().getId(), con.getDestino().getNome()) 
            : null;
            
        return new ConexaoDTO(con.getId(), con.getRua(), origemDTO, destinoDTO, con.getQuilometros());
    }
}

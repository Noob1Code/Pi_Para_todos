package greenlong.service;

import greenlong.dto.CaminhaoDTO;
import greenlong.dto.CaminhaoRequestDTO;
import greenlong.mapper.CaminhaoMapper;
import greenlong.model.Caminhao;
import greenlong.model.Residuo;
import greenlong.repository.CaminhaoRepository;
import greenlong.repository.ItinerarioRepository;
import greenlong.repository.ResiduoRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class CaminhaoService refatorada para usar Mapper e com validação de itinerários.
 */

@Service
@RequiredArgsConstructor
public class CaminhaoService {

    private final CaminhaoRepository caminhaoRepository;
    private final ResiduoRepository residuoRepository;
    private final AuditoriaService auditoriaService;
    private final ItinerarioRepository itinerarioRepository;
    private final CaminhaoMapper caminhaoMapper;

    @Transactional
    public CaminhaoDTO criarCaminhao(CaminhaoRequestDTO dto) {
        if (caminhaoRepository.existsByPlacaIgnoreCase(dto.getPlaca())) {
            throw new IllegalArgumentException("A placa '" + dto.getPlaca() + "' já está cadastrada.");
        }

        List<Residuo> residuos = residuoRepository.findByNomeIn(dto.getResiduos());
        if (residuos.size() != dto.getResiduos().size()) {
            throw new IllegalArgumentException("Um ou mais tipos de resíduos informados são inválidos.");
        }

        Caminhao novoCaminhao = toEntity(new Caminhao(), dto, residuos);
        Caminhao caminhaoSalvo = caminhaoRepository.save(novoCaminhao);

        CaminhaoDTO caminhaoDTO = caminhaoMapper.toResponseDTO(caminhaoSalvo);

        String usuarioLogado = getUsuarioLogado();
        auditoriaService.logCaminhaoActivity(caminhaoSalvo.getId(), null, caminhaoDTO, "INSERT", usuarioLogado);
        
        return caminhaoDTO;
    }
    
    @Transactional(readOnly = true)
    public List<CaminhaoDTO> listarTodos() {
        return caminhaoRepository.findAll().stream()
                .map(caminhaoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CaminhaoDTO> buscarPorId(Long id) {
        return caminhaoRepository.findById(id).map(caminhaoMapper::toResponseDTO);
    }

    @Transactional
    public Optional<CaminhaoDTO> atualizarCaminhao(Long id, CaminhaoRequestDTO dto) {
        return caminhaoRepository.findById(id).map(caminhaoExistente -> {
            CaminhaoDTO estadoAntes = caminhaoMapper.toResponseDTO(caminhaoExistente);

            caminhaoRepository.findByPlacaIgnoreCase(dto.getPlaca()).ifPresent(caminhaoComMesmaPlaca -> {
                if (!caminhaoComMesmaPlaca.getId().equals(id)) {
                    throw new IllegalArgumentException("A placa '" + dto.getPlaca() + "' já pertence a outro caminhão.");
                }
            });

            List<Residuo> residuos = residuoRepository.findByNomeIn(dto.getResiduos());
            if (residuos.size() != dto.getResiduos().size()) {
                throw new IllegalArgumentException("Um ou mais tipos de resíduos informados são inválidos.");
            }

            Caminhao caminhaoAtualizado = toEntity(caminhaoExistente, dto, residuos);
            caminhaoRepository.save(caminhaoAtualizado);
            
            CaminhaoDTO estadoDepois = caminhaoMapper.toResponseDTO(caminhaoAtualizado);
            
            String usuarioLogado = getUsuarioLogado();
            auditoriaService.logCaminhaoActivity(caminhaoAtualizado.getId(), estadoAntes, estadoDepois, "UPDATE", usuarioLogado);
            
            return estadoDepois;
        });
    }

    @Transactional
    public boolean deletarCaminhao(Long id) {
        Optional<Caminhao> caminhaoOpt = caminhaoRepository.findById(id);

        if (caminhaoOpt.isPresent()) {
            
            // VALIDAÇÃO DE INTEGRIDADE ADICIONADA
            if (itinerarioRepository.existsByRota_Caminhao_Id(id)) {
                throw new DataIntegrityViolationException("Este caminhão não pode ser excluído, pois possui itinerários agendados.");
            }
            
            Caminhao caminhaoParaDeletar = caminhaoOpt.get();
            CaminhaoDTO estadoAntes = caminhaoMapper.toResponseDTO(caminhaoParaDeletar);

            caminhaoRepository.deleteById(id);
            
            String usuarioLogado = getUsuarioLogado();
            auditoriaService.logCaminhaoActivity(id, estadoAntes, null, "DELETE", usuarioLogado);

            return true;
        }
        return false;
    }

    private Caminhao toEntity(Caminhao caminhao, CaminhaoRequestDTO dto, List<Residuo> residuos) {
        caminhao.setPlaca(dto.getPlaca());
        caminhao.setMotorista(dto.getMotorista());
        caminhao.setCapacidade(dto.getCapacidade());
        caminhao.setResiduos(residuos);
        return caminhao;
    }
    
    private String getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return "SISTEMA"; 
        }
        return authentication.getName(); 
    }
}
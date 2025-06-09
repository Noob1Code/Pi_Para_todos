/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import greenlong.model.AuditoriaCaminhao;
import greenlong.model.AuditoriaPontoColeta;
import greenlong.repository.AuditoriaCaminhaoRepository;
import greenlong.repository.AuditoriaPontoColetaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 09/06/2025
 * @brief Class AuditoriaService
 */

@Service
@Slf4j
@RequiredArgsConstructor 
public class AuditoriaService {

    private final AuditoriaCaminhaoRepository auditoriaCaminhaoRepository;
    private final AuditoriaPontoColetaRepository auditoriaPontoColetaRepository;
    private final ObjectMapper objectMapper;

    private String toJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("FALHA AO SERIALIZAR OBJETO PARA JSON: {}", e.getMessage());
            return "{\"error\":\"Falha ao serializar objeto\"}";
        }
    }

    @Async
    public void logCaminhaoActivity(Long caminhaoId, Object antes, Object depois, String acao) {
        String antesJson = toJson(antes);
        String depoisJson = toJson(depois);

        log.info("DIAGNÓSTICO DE AUDITORIA (CAMINHÃO): ANTES=[{}], DEPOIS=[{}]", antesJson, depoisJson);

        AuditoriaCaminhao logEntry = new AuditoriaCaminhao(caminhaoId, antesJson, depoisJson, acao);
        auditoriaCaminhaoRepository.save(logEntry);
    }

    @Async
    public void logPontoColetaActivity(Long pontoColetaId, Object antes, Object depois, String acao) {
        String antesJson = toJson(antes);
        String depoisJson = toJson(depois);

        log.info("DIAGNÓSTICO DE AUDITORIA (PONTO COLETA): ANTES=[{}], DEPOIS=[{}]", antesJson, depoisJson);
        
        AuditoriaPontoColeta logEntry = new AuditoriaPontoColeta(pontoColetaId, antesJson, depoisJson, acao);
        auditoriaPontoColetaRepository.save(logEntry);
    }
}
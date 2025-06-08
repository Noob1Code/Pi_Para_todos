/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.auditoria;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 07/06/2025
 * @brief Class AuditoriaConfig
 */

@Component
@RequiredArgsConstructor
public class AuditoriaConfig {

    private final JdbcTemplate jdbcTemplate;
    private final AuditoriaServices auditoriaServices;

    @PostConstruct
    public void init() {
        criarTabelasDeLog();

        auditoriaServices.criarFuncoesETriggersCaminhao();
        auditoriaServices.criarFuncoesETriggersPontosColeta();
        auditoriaServices.criarTriggerAtualizaResiduosLog();
        auditoriaServices.criarFuncoesETriggersRotas();
        auditoriaServices.criarFuncoesETriggersCaminhaoResiduos();
    }

    private void criarTabelasDeLog() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS caminhao_log (
              log_id    SERIAL PRIMARY KEY,
              id        BIGINT,
              capacidade INTEGER,
              motorista  VARCHAR(150),
              placa      VARCHAR(50),
              residuos_data JSONB,  -- Alterado para JSONB
              operacao   VARCHAR(10) NOT NULL,
              data_hora  TIMESTAMP   NOT NULL,
              old_data   JSONB,
              new_data   JSONB
            );
        """);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS pontos_coleta_log (
              log_id               SERIAL PRIMARY KEY,
              id                   BIGINT,
              bairro_id            INTEGER,
              bairro_data          JSONB,
              email_responsavel    VARCHAR(100),
              endereco             VARCHAR(255),
              horario_funcionamento VARCHAR(100),
              nome                 VARCHAR(100),
              responsavel          VARCHAR(100),
              telefone_responsavel VARCHAR(20),
              operacao             VARCHAR(10)  NOT NULL,
              data_hora            TIMESTAMP    NOT NULL,
              old_data             JSONB,
              new_data             JSONB,
              residuos_old         JSONB,
              residuos_new         JSONB
            );
        """);
        
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS rotas_log (
              log_id       SERIAL PRIMARY KEY,
              id           INTEGER,
              data         TIMESTAMP,
              caminhao_id  BIGINT,
              pontos_coleta_destino_id BIGINT,
              pontos_coleta_origem_id  BIGINT,
              operacao     VARCHAR(10)  NOT NULL,
              data_hora    TIMESTAMP    NOT NULL,
              old_data     JSONB,
              new_data     JSONB
            );
        """);
    }
}
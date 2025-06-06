/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlog.back.config;

import greenlog.back.service.AuditoriaServices;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 05/06/2025
 * @brief Class AuditoriaServices
 */

@Component
public class AuditoriaConfig {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final AuditoriaServices auditoriaServices;

    @Autowired
    public AuditoriaConfig(AuditoriaServices auditoriaServices) {
        this.auditoriaServices = auditoriaServices;
    }

    @PostConstruct
    public void init() {
        criarTabelasDeLog();
        auditoriaServices.criarFuncoesETriggersRotas();
        auditoriaServices.criarFuncoesETriggersPontosColeta();
        auditoriaServices.criarTriggerAtualizaResiduosLog();
        auditoriaServices.criarFuncoesETriggersCaminhao();
    }

    private void criarTabelasDeLog() {
        // rotas_log
        jdbcTemplate.execute(
            "CREATE TABLE IF NOT EXISTS rotas_log ( " +
            "  log_id       SERIAL PRIMARY KEY, " +
            "  id           INTEGER, " +
            "  data         TIMESTAMP, " +
            "  caminhao_id  INTEGER, " +
            "  pontos_coleta_destino_id INTEGER, " +
            "  pontos_coleta_origem_id  INTEGER, " +
            "  operacao     VARCHAR(10)  NOT NULL, " +
            "  data_hora    TIMESTAMP    NOT NULL, " +
            "  old_data     JSONB, " +
            "  new_data     JSONB " +
            ");"
        );
        jdbcTemplate.execute("ALTER TABLE rotas_log ADD COLUMN IF NOT EXISTS old_data JSONB;");
        jdbcTemplate.execute("ALTER TABLE rotas_log ADD COLUMN IF NOT EXISTS new_data JSONB;");

        // pontos_coleta_log
        jdbcTemplate.execute(
            "CREATE TABLE IF NOT EXISTS pontos_coleta_log ( " +
            "  log_id               SERIAL PRIMARY KEY, " +
            "  id                   INTEGER, " +
            "  bairro_id            INTEGER, " +
            "  bairro_data          JSONB, " +
            "  email_responsavel    VARCHAR(255), " +
            "  endereco             VARCHAR(255), " +
            "  horario_funcionamento VARCHAR(100), " +
            "  nome                 VARCHAR(100), " +
            "  responsavel          VARCHAR(100), " +
            "  telefone_responsavel VARCHAR(50), " +
            "  operacao             VARCHAR(10)  NOT NULL, " +
            "  data_hora            TIMESTAMP    NOT NULL, " +
            "  old_data             JSONB, " +
            "  new_data             JSONB, " +
            "  residuos_old         JSONB, " +
            "  residuos_new         JSONB " +
            ");"
        );

        jdbcTemplate.execute("ALTER TABLE pontos_coleta_log ADD COLUMN IF NOT EXISTS bairro_data JSONB;");
        jdbcTemplate.execute("ALTER TABLE pontos_coleta_log ADD COLUMN IF NOT EXISTS old_data JSONB;");
        jdbcTemplate.execute("ALTER TABLE pontos_coleta_log ADD COLUMN IF NOT EXISTS new_data JSONB;");
        jdbcTemplate.execute("ALTER TABLE pontos_coleta_log ADD COLUMN IF NOT EXISTS residuos_old JSONB;");
        jdbcTemplate.execute("ALTER TABLE pontos_coleta_log ADD COLUMN IF NOT EXISTS residuos_new JSONB;");

        // caminhao_log
        jdbcTemplate.execute(
            "CREATE TABLE IF NOT EXISTS caminhao_log ( " +
            "  log_id    SERIAL PRIMARY KEY, " +
            "  id        INTEGER, " +
            "  capacidade INTEGER, " +
            "  motorista  VARCHAR(100), " +
            "  placa      VARCHAR(50), " +
            "  residuos   VARCHAR(100), " +
            "  operacao   VARCHAR(10) NOT NULL, " +
            "  data_hora  TIMESTAMP   NOT NULL, " +
            "  old_data   JSONB, " +
            "  new_data   JSONB " +
            ");"
        );
        jdbcTemplate.execute("ALTER TABLE caminhao_log ADD COLUMN IF NOT EXISTS old_data JSONB;");
        jdbcTemplate.execute("ALTER TABLE caminhao_log ADD COLUMN IF NOT EXISTS new_data JSONB;");
    }
}
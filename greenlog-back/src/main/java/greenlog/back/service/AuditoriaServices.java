/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlog.back.service;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 05/06/2025
 * @brief Class AuditoriaServices
 */

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaServices {

    private final JdbcTemplate jdbcTemplate;

    public AuditoriaServices(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void criarFuncoesETriggersRotas() {
        String funcRotas = ""
            + "CREATE OR REPLACE FUNCTION fn_audit_rotas() RETURNS TRIGGER AS $$\n"
            + "DECLARE\n"
            + "  old_json JSONB;\n"
            + "  new_json JSONB;\n"
            + "BEGIN\n"
            + "  IF (TG_OP = 'INSERT') THEN\n"
            + "    new_json := to_jsonb(NEW);\n"
            + "    INSERT INTO rotas_log (id, data, caminhao_id, pontos_coleta_destino_id, pontos_coleta_origem_id,\n"
            + "                            operacao, data_hora, old_data, new_data)\n"
            + "    VALUES (NEW.id, NEW.data, NEW.caminhao_id, NEW.pontos_coleta_destino_id, NEW.pontos_coleta_origem_id,\n"
            + "            TG_OP, now(), NULL, new_json);\n"
            + "  ELSIF (TG_OP = 'UPDATE') THEN\n"
            + "    old_json := to_jsonb(OLD);\n"
            + "    new_json := to_jsonb(NEW);\n"
            + "    INSERT INTO rotas_log (id, data, caminhao_id, pontos_coleta_destino_id, pontos_coleta_origem_id,\n"
            + "                            operacao, data_hora, old_data, new_data)\n"
            + "    VALUES (NEW.id, NEW.data, NEW.caminhao_id, NEW.pontos_coleta_destino_id, NEW.pontos_coleta_origem_id,\n"
            + "            TG_OP, now(), old_json, new_json);\n"
            + "  ELSIF (TG_OP = 'DELETE') THEN\n"
            + "    old_json := to_jsonb(OLD);\n"
            + "    INSERT INTO rotas_log (id, data, caminhao_id, pontos_coleta_destino_id, pontos_coleta_origem_id,\n"
            + "                            operacao, data_hora, old_data, new_data)\n"
            + "    VALUES (OLD.id, OLD.data, OLD.caminhao_id, OLD.pontos_coleta_destino_id, OLD.pontos_coleta_origem_id,\n"
            + "            TG_OP, now(), old_json, NULL);\n"
            + "  END IF;\n"
            + "  RETURN NULL;\n"
            + "END;\n"
            + "$$ LANGUAGE plpgsql;";
        jdbcTemplate.execute(funcRotas);

        jdbcTemplate.execute("DROP TRIGGER IF EXISTS trg_rotas_audit ON rotas;");
        String trgRotas = ""
            + "CREATE TRIGGER trg_rotas_audit\n"
            + "  AFTER INSERT OR UPDATE OR DELETE ON rotas\n"
            + "  FOR EACH ROW EXECUTE FUNCTION fn_audit_rotas();";
        jdbcTemplate.execute(trgRotas);
    }

    public void criarFuncoesETriggersPontosColeta() {
        String funcPontos = ""
            + "CREATE OR REPLACE FUNCTION fn_audit_pontos_coleta() RETURNS TRIGGER AS $$\n"
            + "DECLARE\n"
            + "  old_json       JSONB;\n"
            + "  new_json       JSONB;\n"
            + "  residuos_old   JSONB;\n"
            + "  residuos_new   JSONB;\n"
            + "  bairro_data_old JSONB;\n" 
            + "  bairro_data_new JSONB;\n" 
            + "BEGIN\n"
            + "  IF (TG_OP = 'INSERT') THEN\n"
            + "    new_json := to_jsonb(NEW);\n"
            + "    SELECT to_jsonb(b) INTO bairro_data_new FROM bairro b WHERE b.id = NEW.bairro_id;\n" // Busca dados do bairro
            + "    SELECT coalesce(to_jsonb(array_agg(tipo_residuo)), '[]'::jsonb) INTO residuos_new FROM ponto_coleta_tipos_residuos WHERE ponto_coleta_id = NEW.id;\n"
            + "    INSERT INTO pontos_coleta_log (id, bairro_id, bairro_data, email_responsavel, endereco, horario_funcionamento, nome, responsavel, telefone_responsavel, operacao, data_hora, old_data, new_data, residuos_old, residuos_new)\n"
            + "    VALUES (NEW.id, NEW.bairro_id, bairro_data_new, NEW.email_responsavel, NEW.endereco, NEW.horario_funcionamento, NEW.nome, NEW.responsavel, NEW.telefone_responsavel, TG_OP, now(), NULL, new_json, NULL, residuos_new);\n"
            + "  ELSIF (TG_OP = 'UPDATE') THEN\n"
            + "    old_json := to_jsonb(OLD);\n"
            + "    new_json := to_jsonb(NEW);\n"
            + "    SELECT to_jsonb(b) INTO bairro_data_old FROM bairro b WHERE b.id = OLD.bairro_id;\n" // Busca dados do bairro antigo
            + "    SELECT to_jsonb(b) INTO bairro_data_new FROM bairro b WHERE b.id = NEW.bairro_id;\n" // Busca dados do bairro novo
            + "    SELECT coalesce(to_jsonb(array_agg(tipo_residuo)), '[]'::jsonb) INTO residuos_old FROM ponto_coleta_tipos_residuos WHERE ponto_coleta_id = OLD.id;\n"
            + "    SELECT coalesce(to_jsonb(array_agg(tipo_residuo)), '[]'::jsonb) INTO residuos_new FROM ponto_coleta_tipos_residuos WHERE ponto_coleta_id = NEW.id;\n"
            + "    INSERT INTO pontos_coleta_log (id, bairro_id, bairro_data, email_responsavel, endereco, horario_funcionamento, nome, responsavel, telefone_responsavel, operacao, data_hora, old_data, new_data, residuos_old, residuos_new)\n"
            + "    VALUES (NEW.id, NEW.bairro_id, bairro_data_new, NEW.email_responsavel, NEW.endereco, NEW.horario_funcionamento, NEW.nome, NEW.responsavel, NEW.telefone_responsavel, TG_OP, now(), old_json, new_json, residuos_old, residuos_new);\n"
            + "  ELSIF (TG_OP = 'DELETE') THEN\n"
            + "    old_json := to_jsonb(OLD);\n"
            + "    SELECT to_jsonb(b) INTO bairro_data_old FROM bairro b WHERE b.id = OLD.bairro_id;\n" // Busca dados do bairro
            + "    SELECT coalesce(to_jsonb(array_agg(tipo_residuo)), '[]'::jsonb) INTO residuos_old FROM ponto_coleta_tipos_residuos WHERE ponto_coleta_id = OLD.id;\n"
            + "    INSERT INTO pontos_coleta_log (id, bairro_id, bairro_data, email_responsavel, endereco, horario_funcionamento, nome, responsavel, telefone_responsavel, operacao, data_hora, old_data, new_data, residuos_old, residuos_new)\n"
            + "    VALUES (OLD.id, OLD.bairro_id, bairro_data_old, OLD.email_responsavel, OLD.endereco, OLD.horario_funcionamento, OLD.nome, OLD.responsavel, OLD.telefone_responsavel, TG_OP, now(), old_json, NULL, residuos_old, NULL);\n"
            + "  END IF;\n"
            + "  RETURN NULL;\n"
            + "END;\n"
            + "$$ LANGUAGE plpgsql;";
        jdbcTemplate.execute(funcPontos);

        jdbcTemplate.execute("DROP TRIGGER IF EXISTS trg_pontos_coleta_audit ON pontos_coleta;");
        String trgPontos = ""
            + "CREATE TRIGGER trg_pontos_coleta_audit\n"
            + "  AFTER INSERT OR UPDATE OR DELETE ON pontos_coleta\n"
            + "  FOR EACH ROW EXECUTE FUNCTION fn_audit_pontos_coleta();";
        jdbcTemplate.execute(trgPontos);
    }

    public void criarTriggerAtualizaResiduosLog() {
        String funcAtualiza = ""
            + "CREATE OR REPLACE FUNCTION fn_atualiza_residuos_log() RETURNS TRIGGER AS $$\n"
            + "DECLARE\n"
            + "  todos_residuos JSONB;\n"
            + "BEGIN\n"
            + "  SELECT coalesce(to_jsonb(array_agg(tipo_residuo)), '[]'::jsonb)\n"
            + "    INTO todos_residuos\n"
            + "    FROM ponto_coleta_tipos_residuos\n"
            + "   WHERE ponto_coleta_id = NEW.ponto_coleta_id;\n"
            + "  UPDATE pontos_coleta_log\n"
            + "     SET residuos_new = todos_residuos\n"
            + "   WHERE id = NEW.ponto_coleta_id\n"
            + "     AND data_hora = (\n"
            + "       SELECT max(data_hora)\n"
            + "         FROM pontos_coleta_log\n"
            + "        WHERE id = NEW.ponto_coleta_id\n"
            + "     );\n"
            + "  RETURN NULL;\n"
            + "END;\n"
            + "$$ LANGUAGE plpgsql;";
        jdbcTemplate.execute(funcAtualiza);

        jdbcTemplate.execute("DROP TRIGGER IF EXISTS trg_residuo_ponto_coleta_insert ON ponto_coleta_tipos_residuos;");
        String trgAtualiza = ""
            + "CREATE TRIGGER trg_residuo_ponto_coleta_insert\n"
            + "  AFTER INSERT ON ponto_coleta_tipos_residuos\n"
            + "  FOR EACH ROW EXECUTE FUNCTION fn_atualiza_residuos_log();";
        jdbcTemplate.execute(trgAtualiza);
    }

    public void criarFuncoesETriggersCaminhao() {
        String funcCaminhao = ""
            + "CREATE OR REPLACE FUNCTION fn_audit_caminhao() RETURNS TRIGGER AS $$\n"
            + "DECLARE\n"
            + "  old_json JSONB;\n"
            + "  new_json JSONB;\n"
            + "BEGIN\n"
            + "  IF (TG_OP = 'INSERT') THEN\n"
            + "    new_json := to_jsonb(NEW);\n"
            + "    INSERT INTO caminhao_log (id, capacidade, motorista, placa, residuos, operacao, data_hora, old_data, new_data)\n"
            + "    VALUES (NEW.id, NEW.capacidade, NEW.motorista, NEW.placa, NEW.residuos, TG_OP, now(), NULL, new_json);\n"
            + "  ELSIF (TG_OP = 'UPDATE') THEN\n"
            + "    old_json := to_jsonb(OLD);\n"
            + "    new_json := to_jsonb(NEW);\n"
            + "    INSERT INTO caminhao_log (id, capacidade, motorista, placa, residuos, operacao, data_hora, old_data, new_data)\n"
            + "    VALUES (NEW.id, NEW.capacidade, NEW.motorista, NEW.placa, NEW.residuos, TG_OP, now(), old_json, new_json);\n"
            + "  ELSIF (TG_OP = 'DELETE') THEN\n"
            + "    old_json := to_jsonb(OLD);\n"
            + "    INSERT INTO caminhao_log (id, capacidade, motorista, placa, residuos, operacao, data_hora, old_data, new_data)\n"
            + "    VALUES (OLD.id, OLD.capacidade, OLD.motorista, OLD.placa, OLD.residuos, TG_OP, now(), old_json, NULL);\n"
            + "  END IF;\n"
            + "  RETURN NULL;\n"
            + "END;\n"
            + "$$ LANGUAGE plpgsql;";
        jdbcTemplate.execute(funcCaminhao);

        jdbcTemplate.execute("DROP TRIGGER IF EXISTS trg_caminhao_audit ON caminhao;");
        String trgCaminhao = ""
            + "CREATE TRIGGER trg_caminhao_audit\n"
            + "  AFTER INSERT OR UPDATE OR DELETE ON caminhao\n"
            + "  FOR EACH ROW EXECUTE FUNCTION fn_audit_caminhao();";
        jdbcTemplate.execute(trgCaminhao);
    }
}
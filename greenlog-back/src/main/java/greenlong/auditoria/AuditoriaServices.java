    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.auditoria;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 07/06/2025
 * @brief Class AuditoriaServices
 */

@Service
@RequiredArgsConstructor
public class AuditoriaServices {

    private final JdbcTemplate jdbcTemplate;

        public void criarFuncoesETriggersCaminhao() {
        String funcCaminhao = """
        CREATE OR REPLACE FUNCTION fn_audit_caminhao() RETURNS TRIGGER AS $$ 
            DECLARE
              old_json      JSONB;
              new_json      JSONB;
              residuos_data JSONB;
            BEGIN
              IF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
                SELECT jsonb_agg(residuo) INTO residuos_data
                FROM caminhao_residuos
                WHERE caminhao_id = NEW.id;
              END IF;
        
              IF (TG_OP = 'INSERT') THEN
                new_json := to_jsonb(NEW);

                IF residuos_data IS NULL THEN
                    residuos_data := '[]'::jsonb;
                END IF;
                INSERT INTO caminhao_log (id, capacidade, motorista, placa, operacao, data_hora, new_data, residuos_data)
                VALUES (NEW.id, NEW.capacidade, NEW.motorista, NEW.placa, TG_OP, now(), new_json, residuos_data);
        
              ELSIF (TG_OP = 'UPDATE') THEN
                old_json := to_jsonb(OLD);
                new_json := to_jsonb(NEW);

                IF residuos_data IS NULL THEN
                    residuos_data := '[]'::jsonb;
                END IF;
                INSERT INTO caminhao_log (id, capacidade, motorista, placa, operacao, data_hora, old_data, new_data, residuos_data)
                VALUES (NEW.id, NEW.capacidade, NEW.motorista, NEW.placa, TG_OP, now(), old_json, new_json, residuos_data);
        
              ELSIF (TG_OP = 'DELETE') THEN
                old_json := to_jsonb(OLD);
                              
                SELECT jsonb_agg(residuo) INTO residuos_data
                FROM caminhao_residuos
                WHERE caminhao_id = OLD.id;
                
                IF residuos_data IS NULL THEN
                    residuos_data := '[]'::jsonb;
                END IF;
                INSERT INTO caminhao_log (id, capacidade, motorista, placa, operacao, data_hora, old_data, residuos_data)
                VALUES (OLD.id, OLD.capacidade, OLD.motorista, OLD.placa, TG_OP, now(), old_json, residuos_data);
              END IF;
              
              RETURN NULL;
            END;
        $$ LANGUAGE plpgsql;
        """;
        jdbcTemplate.execute(funcCaminhao);

        jdbcTemplate.execute("DROP TRIGGER IF EXISTS trg_caminhao_audit ON caminhao;");
        String trgCaminhao = """
            CREATE TRIGGER trg_caminhao_audit
              AFTER INSERT OR UPDATE OR DELETE ON caminhao
              FOR EACH ROW EXECUTE FUNCTION fn_audit_caminhao();
            """;
        jdbcTemplate.execute(trgCaminhao);
    }

    public void criarFuncoesETriggersCaminhaoResiduos() {
        String funcCaminhaoResiduos = """
        CREATE OR REPLACE FUNCTION fn_audit_caminhao_residuos() RETURNS TRIGGER AS $$ 
            DECLARE
              v_residuos_data JSONB; -- VARIÁVEL RENOMEADA
              v_caminhao_id BIGINT;
            BEGIN
              v_caminhao_id := COALESCE(NEW.caminhao_id, OLD.caminhao_id);

              SELECT coalesce(jsonb_agg(residuo), '[]'::jsonb)
                INTO v_residuos_data -- USANDO A VARIÁVEL RENOMEADA
                FROM caminhao_residuos
               WHERE caminhao_id = v_caminhao_id;

              UPDATE caminhao_log
                 SET residuos_data = v_residuos_data -- ATRIBUIÇÃO AGORA É CLARA
               WHERE id = v_caminhao_id
                 AND data_hora = (SELECT max(data_hora) FROM caminhao_log WHERE id = v_caminhao_id);

              RETURN NULL;
            END;
        $$ LANGUAGE plpgsql;
        """;
        jdbcTemplate.execute(funcCaminhaoResiduos);

        jdbcTemplate.execute("DROP TRIGGER IF EXISTS trg_caminhao_residuos_audit ON caminhao_residuos;");
        String trgCaminhaoResiduos = """
            CREATE TRIGGER trg_caminhao_residuos_audit
              AFTER INSERT OR UPDATE OR DELETE ON caminhao_residuos
              FOR EACH ROW EXECUTE FUNCTION fn_audit_caminhao_residuos();
            """;
        jdbcTemplate.execute(trgCaminhaoResiduos);
    }

    public void criarFuncoesETriggersPontosColeta() {
        String funcPontos = """
            CREATE OR REPLACE FUNCTION fn_audit_pontos_coleta() RETURNS TRIGGER AS $$
            DECLARE
              old_json    JSONB;
              new_json    JSONB;
              bairro_data JSONB;
            BEGIN
              IF (TG_OP = 'INSERT') THEN
                new_json := to_jsonb(NEW);
                SELECT to_jsonb(b) INTO bairro_data FROM bairro b WHERE b.id = NEW.bairro_id;
                
                -- CORRIGIDO: Adicionadas as colunas faltantes
                INSERT INTO pontos_coleta_log (
                  id, bairro_id, bairro_data, nome, responsavel, 
                  email_responsavel, endereco, telefone_responsavel, 
                  operacao, data_hora, new_data
                )
                VALUES (
                  NEW.id, NEW.bairro_id, bairro_data, NEW.nome, NEW.responsavel, 
                  NEW.email_responsavel, NEW.endereco, NEW.telefone_responsavel,
                  TG_OP, now(), new_json
                );
            
              ELSIF (TG_OP = 'UPDATE') THEN
                old_json := to_jsonb(OLD);
                new_json := to_jsonb(NEW);
                SELECT to_jsonb(b) INTO bairro_data FROM bairro b WHERE b.id = NEW.bairro_id;
            
                -- CORRIGIDO: Adicionadas as colunas faltantes
                INSERT INTO pontos_coleta_log (
                  id, bairro_id, bairro_data, nome, responsavel, 
                  email_responsavel, endereco, telefone_responsavel,
                  operacao, data_hora, old_data, new_data
                )
                VALUES (
                  NEW.id, NEW.bairro_id, bairro_data, NEW.nome, NEW.responsavel,
                  NEW.email_responsavel, NEW.endereco, NEW.telefone_responsavel,
                  TG_OP, now(), old_json, new_json
                );
            
              ELSIF (TG_OP = 'DELETE') THEN
                old_json := to_jsonb(OLD);
                SELECT to_jsonb(b) INTO bairro_data FROM bairro b WHERE b.id = OLD.bairro_id;
            
                -- CORRIGIDO: Adicionadas as colunas faltantes
                INSERT INTO pontos_coleta_log (
                  id, bairro_id, bairro_data, nome, responsavel, 
                  email_responsavel, endereco, telefone_responsavel,
                  operacao, data_hora, old_data
                )
                VALUES (
                  OLD.id, OLD.bairro_id, bairro_data, OLD.nome, OLD.responsavel,
                  OLD.email_responsavel, OLD.endereco, OLD.telefone_responsavel,
                  TG_OP, now(), old_json
                );
                
              END IF;
              RETURN NULL;
            END;
            $$ LANGUAGE plpgsql;
            """;
        jdbcTemplate.execute(funcPontos);

        jdbcTemplate.execute("DROP TRIGGER IF EXISTS trg_pontos_coleta_audit ON pontos_coleta;");
        String trgPontos = """
            CREATE TRIGGER trg_pontos_coleta_audit
              AFTER INSERT OR UPDATE OR DELETE ON pontos_coleta
              FOR EACH ROW EXECUTE FUNCTION fn_audit_pontos_coleta();
            """;
        jdbcTemplate.execute(trgPontos);
    }
    
    public void criarTriggerAtualizaResiduosLog() {
        String funcAtualiza = """
            CREATE OR REPLACE FUNCTION fn_atualiza_residuos_log() RETURNS TRIGGER AS $$
            DECLARE
              todos_residuos JSONB;
              p_coleta_id BIGINT;
            BEGIN
              p_coleta_id := COALESCE(NEW.ponto_coleta_id, OLD.ponto_coleta_id);
            
              SELECT coalesce(to_jsonb(array_agg(tipo_residuo)), '[]'::jsonb)
                INTO todos_residuos
                FROM ponto_coleta_tipos_residuos
               WHERE ponto_coleta_id = p_coleta_id;

              IF TG_OP = 'DELETE' THEN
                  UPDATE pontos_coleta_log
                     SET residuos_old = todos_residuos
                   WHERE id = p_coleta_id
                     AND data_hora = (SELECT max(data_hora) FROM pontos_coleta_log WHERE id = p_coleta_id);
              ELSE
                  UPDATE pontos_coleta_log
                     SET residuos_new = todos_residuos
                   WHERE id = p_coleta_id
                     AND data_hora = (SELECT max(data_hora) FROM pontos_coleta_log WHERE id = p_coleta_id);
              END IF;
              RETURN NULL;
            END;
            $$ LANGUAGE plpgsql;
            """;
        jdbcTemplate.execute(funcAtualiza);

        jdbcTemplate.execute("DROP TRIGGER IF EXISTS trg_residuo_ponto_coleta_audit ON ponto_coleta_tipos_residuos;");
        String trgAtualiza = """
            CREATE TRIGGER trg_residuo_ponto_coleta_audit
              AFTER INSERT OR UPDATE OR DELETE ON ponto_coleta_tipos_residuos
              FOR EACH ROW EXECUTE FUNCTION fn_atualiza_residuos_log();
            """;
        jdbcTemplate.execute(trgAtualiza);
    }

    public void criarFuncoesETriggersRotas() {
        String funcRotas = """
            CREATE OR REPLACE FUNCTION fn_audit_rotas() RETURNS TRIGGER AS $$
            BEGIN
              IF (TG_OP = 'INSERT') THEN
                INSERT INTO rotas_log (id, data, caminhao_id, pontos_coleta_destino_id, pontos_coleta_origem_id, operacao, data_hora, new_data)
                VALUES (NEW.id, NEW.data, NEW.caminhao_id, NEW.pontos_coleta_destino_id, NEW.pontos_coleta_origem_id, TG_OP, now(), to_jsonb(NEW));
              ELSIF (TG_OP = 'UPDATE') THEN
                INSERT INTO rotas_log (id, data, caminhao_id, pontos_coleta_destino_id, pontos_coleta_origem_id, operacao, data_hora, old_data, new_data)
                VALUES (NEW.id, NEW.data, NEW.caminhao_id, NEW.pontos_coleta_destino_id, NEW.pontos_coleta_origem_id, TG_OP, now(), to_jsonb(OLD), to_jsonb(NEW));
              ELSIF (TG_OP = 'DELETE') THEN
                 INSERT INTO rotas_log (id, data, caminhao_id, pontos_coleta_destino_id, pontos_coleta_origem_id, operacao, data_hora, old_data)
                VALUES (OLD.id, OLD.data, OLD.caminhao_id, OLD.pontos_coleta_destino_id, OLD.pontos_coleta_origem_id, TG_OP, now(), to_jsonb(OLD));
              END IF;
              RETURN NULL;
            END;
            $$ LANGUAGE plpgsql;
            """;
        jdbcTemplate.execute(funcRotas);

        jdbcTemplate.execute("DROP TRIGGER IF EXISTS trg_rotas_audit ON rotas;");
        String trgRotas = """
            CREATE TRIGGER trg_rotas_audit
              AFTER INSERT OR UPDATE OR DELETE ON rotas
              FOR EACH ROW EXECUTE FUNCTION fn_audit_rotas();
            """;
        jdbcTemplate.execute(trgRotas);
    }
}
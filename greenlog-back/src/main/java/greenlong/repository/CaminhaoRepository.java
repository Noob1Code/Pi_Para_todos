/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.repository;

import greenlong.model.Caminhao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class CaminhaoRepository
 */

@Repository
public interface CaminhaoRepository extends JpaRepository<Caminhao, Long> {
    boolean existsByPlacaIgnoreCase(String placa);
    Optional<Caminhao> findByPlacaIgnoreCase(String placa);
        
    interface CaminhaoTotalAnualProjection {
        Long getId();
        String getPlaca();
        String getMotorista();
        Double getTotalAnual();
    }

    interface DistanciaMensalProjection {
        Integer getMes();
        Double getDistanciaMensal();
    }
    
    // Query 1: Busca o total percorrido por cada caminhão no ano
    @Query(value = """
        SELECT
            c.id, c.placa, c.motorista,
            COALESCE(SUM(r.distancia_total), 0) AS totalAnual
        FROM caminhao c
        LEFT JOIN rotas r ON c.id = r.caminhao_id
        LEFT JOIN itinerarios i ON r.id = i.rota_id AND EXTRACT(YEAR FROM i.data) = :ano
        GROUP BY c.id, c.placa, c.motorista
    """, nativeQuery = true)
    List<CaminhaoTotalAnualProjection> findTotaisAnuaisPorCaminhao(@Param("ano") Integer ano);

    // Query 2: Busca a distância mensal para UM caminhão específico
    @Query(value = """
        SELECT
            EXTRACT(MONTH FROM i.data) AS mes,
            SUM(r.distancia_total) AS distanciaMensal
        FROM rotas r
        JOIN itinerarios i ON r.id = i.rota_id
        WHERE r.caminhao_id = :caminhaoId AND EXTRACT(YEAR FROM i.data) = :ano
        GROUP BY mes
    """, nativeQuery = true)
    List<DistanciaMensalProjection> findDistanciasMensaisParaCaminhao(@Param("ano") Integer ano, @Param("caminhaoId") Long caminhaoId);
}
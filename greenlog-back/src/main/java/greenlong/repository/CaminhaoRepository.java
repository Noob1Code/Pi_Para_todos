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
    
    interface CaminhaoDistanciaProjection {
        Long getId();
        String getPlaca();
        String getMotorista();
        Integer getCapacidade();
        Double getQuilometrosPercorridos();
    }

    @Query(value = """
        SELECT
            c.id AS id,
            c.placa AS placa,
            c.motorista AS motorista,
            c.capacidade AS capacidade,
            SUM(r.distancia_total) AS quilometrosPercorridos
        FROM
            caminhao c
        JOIN
            rotas r ON c.id = r.caminhao_id
        JOIN
            itinerarios i ON r.id = i.rota_id
        WHERE
            EXTRACT(YEAR FROM i.data) = :ano
        GROUP BY
            c.id, c.placa, c.motorista, c.capacidade
        HAVING
            SUM(r.distancia_total) > 0
    """, nativeQuery = true)
    List<CaminhaoDistanciaProjection> findDistanciasAnuaisPorCaminhao(@Param("ano") Integer ano);
}
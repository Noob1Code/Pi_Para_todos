/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.repository;

import greenlong.model.Itinerario;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 12/06/2025
 * @brief Class ItinerarioRepository
 */

@Repository
public interface ItinerarioRepository extends JpaRepository<Itinerario, Long> {
    Optional<Itinerario> findByDataAndRota_Caminhao_Id(LocalDate data, Long caminhaoId);
    Optional<Itinerario> findByDataAndRota_Caminhao_IdAndIdNot(LocalDate data, Long caminhaoId, Long itinerarioId);
    List<Itinerario> findByData(LocalDate data);
    List<Itinerario> findByRota_Caminhao_Id(Long caminhaoId);
    boolean existsByRotaId(Long rotaId);
    boolean existsByRota_Caminhao_Id(Long caminhaoId);
    
        interface ItinerariosPorDiaProjection {
        String getDiaDaSemana();
        Long getQuantidadeDeItinerarios();
    }
        
        @Query(value = """
        SELECT
            CASE
                WHEN EXTRACT(ISODOW FROM data) = 1 THEN 'Segunda-feira'
                WHEN EXTRACT(ISODOW FROM data) = 2 THEN 'Terça-feira'
                WHEN EXTRACT(ISODOW FROM data) = 3 THEN 'Quarta-feira'
                WHEN EXTRACT(ISODOW FROM data) = 4 THEN 'Quinta-feira'
                WHEN EXTRACT(ISODOW FROM data) = 5 THEN 'Sexta-feira'
                WHEN EXTRACT(ISODOW FROM data) = 6 THEN 'Sábado'
                WHEN EXTRACT(ISODOW FROM data) = 7 THEN 'Domingo'
            END AS diaDaSemana,
            COUNT(*) AS quantidadeDeItinerarios
        FROM
            itinerarios
        GROUP BY
            diaDaSemana
        ORDER BY
            MIN(EXTRACT(ISODOW FROM data))
    """, nativeQuery = true)
    List<ItinerariosPorDiaProjection> countItinerariosPorDiaDaSemana();
}

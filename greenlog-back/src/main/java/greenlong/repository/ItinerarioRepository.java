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
}

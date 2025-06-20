/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.repository;

import greenlong.model.Rota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class RotaRepository
 */

@Repository
public interface RotaRepository extends JpaRepository<Rota, Long> {
    boolean existsByCaminhaoId(Long caminhaoId);
    boolean existsByArestasPercorridas_Id(Long conexaoId);
    boolean existsByDestinoId(Long bairroId);
}

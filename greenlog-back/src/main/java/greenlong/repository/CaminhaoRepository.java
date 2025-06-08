/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.repository;

import greenlong.model.Caminhao;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
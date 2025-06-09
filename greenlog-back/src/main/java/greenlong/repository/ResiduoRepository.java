/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.repository;

import greenlong.model.Residuo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 09/06/2025
 * @brief Class ResiduoRepository
 */

@Repository
public interface ResiduoRepository extends JpaRepository<Residuo, Long> {
     List<Residuo> findByNomeIn(List<String> nomes);
}
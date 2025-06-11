/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlong.repository;

import greenlong.model.PontoColeta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Class PontoColetaRepository
 */

@Repository
public interface PontoColetaRepository extends JpaRepository<PontoColeta, Long>, JpaSpecificationExecutor<PontoColeta> {
    Optional<PontoColeta> findByNome(String nome);
    boolean existsByNome(String nome);
    boolean existsByBairroId(Long bairroId);
    List<PontoColeta> findDistinctByTiposResiduosAceitos_NomeIn(List<String> nomesResiduos);
}
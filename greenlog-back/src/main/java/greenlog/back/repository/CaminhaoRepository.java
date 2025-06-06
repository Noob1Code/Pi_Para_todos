/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlog.back.repository;

import greenlog.back.model.Caminhao;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author Gabriel Braga <ninjagamer9795286@gmail.com>
 * @date 04/06/2025
 * @brief Class CaminhaoRepository
 */
public interface CaminhaoRepository extends JpaRepository<Caminhao, Long> {
    boolean existsByPlacaIgnoreCase(String placa);
}
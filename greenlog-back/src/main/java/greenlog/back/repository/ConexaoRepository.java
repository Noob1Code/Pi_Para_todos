/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package greenlog.back.repository;

import greenlog.back.model.Conexao;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Cansei2
 */

public interface ConexaoRepository extends JpaRepository<Conexao,Integer>{
    
}

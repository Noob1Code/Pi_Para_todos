package greenlog.back.repository;

/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 02/06/2025
 * @brief Class UsuarioRepository
 */
import greenlog.back.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> { 

    boolean existsByCpf(String cpf);
    boolean existsByUsername(String username); 

    Optional<Usuario> findByUsername(String username);
}
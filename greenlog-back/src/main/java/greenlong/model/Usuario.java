package greenlong.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 06/06/2025
 * @brief Entidade Usuario, agora implementando UserDetails para integração com Spring Security.
 */

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails { // 1. IMPLEMENTA A INTERFACE UserDetails
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false)
    private Integer idade;

    @Column(nullable = false, length = 11)
    private String telefone;

    @Column(nullable = false, length = 20)
    private String genero;

    @Column(nullable = false)
    private String senha;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Como não estamos a usar papéis (roles) como ADMIN, USER, etc., podemos retornar uma lista vazia.
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // A conta nunca expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // A conta nunca é bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // As credenciais nunca expiram
    }

    @Override
    public boolean isEnabled() {
        return true; // A conta está sempre habilitada
    }
}

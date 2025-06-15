package greenlong.service;

import greenlong.dto.LoginRequestDTO;
import greenlong.dto.UsuarioCadastroDTO;
import greenlong.dto.UsuarioResponseDTO;
import greenlong.model.Usuario;
import greenlong.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UsuarioResponseDTO cadastrarUsuario(UsuarioCadastroDTO dto) {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username já cadastrado.");
        }
        if (usuarioRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setUsername(dto.getUsername());
        novoUsuario.setNome(dto.getNome());
        novoUsuario.setCpf(dto.getCpf());
        novoUsuario.setIdade(dto.getIdade());
        novoUsuario.setTelefone(dto.getTelefone());
        novoUsuario.setGenero(dto.getGenero());
        novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        return toResponseDTO(usuarioSalvo);
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioResponseDTO> login(LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getSenha()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return usuarioRepository.findByUsername(loginRequest.getUsername())
                    .map(this::toResponseDTO);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(usuario.getId(), usuario.getUsername(), usuario.getNome(), usuario.getCpf());
    }
}

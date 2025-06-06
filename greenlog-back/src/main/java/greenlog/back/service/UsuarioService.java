package greenlog.back.service;
/**
 *
 * @author Kayque de Freitas <kayquefreitas08@gmail.com>
 * @data 02/06/2025
 * @brief Class UsuarioService
 */
import greenlog.back.dto.LoginRequestDTO;
import greenlog.back.model.Usuario;
import greenlog.back.dto.UsuarioCadastroDTO;
import greenlog.back.dto.UsuarioResponseDTO;
import greenlog.back.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Para hashear senha
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // INJETAR

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) { // MODIFICAR CONSTRUTOR
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario cadastrarUsuario(UsuarioCadastroDTO dto) {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username já cadastrado.");
        }
        if (usuarioRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setUsername(dto.getUsername()); // SALVAR USERNAME
        novoUsuario.setNome(dto.getNome());
        novoUsuario.setCpf(dto.getCpf());
        novoUsuario.setIdade(dto.getIdade());
        novoUsuario.setTelefone(dto.getTelefone());
        novoUsuario.setGenero(dto.getGenero());

        // HASHEAR A SENHA ANTES DE SALVAR
        String senhaHasheada = passwordEncoder.encode(dto.getSenha());
        novoUsuario.setSenha(senhaHasheada);

        return usuarioRepository.save(novoUsuario);
    }

    public Optional<Usuario> login(LoginRequestDTO loginRequest) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(loginRequest.getUsername());

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            // Compara a senha fornecida (raw) com a senha hasheada no banco
            if (passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenha())) {
                return Optional.of(usuario); // Senha correta
            }
        }
        return Optional.empty(); // Usuário não encontrado ou senha incorreta
    }
    
    public List<UsuarioResponseDTO> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
            .map(u -> new UsuarioResponseDTO(
                u.getId(),
                u.getUsername(),
                u.getNome(),
                u.getCpf()
            ))
            .toList();
        }
}
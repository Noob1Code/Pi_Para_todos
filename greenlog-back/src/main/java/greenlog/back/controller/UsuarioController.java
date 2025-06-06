package greenlog.back.controller;

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
import greenlog.back.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios") // Prefixo para todos os endpoints deste controller
// @CrossOrigin(origins = "http://localhost:4200") // Permite requisições do seu frontend Angular
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody UsuarioCadastroDTO usuarioDTO) {
        try {
            Usuario novoUsuario = usuarioService.cadastrarUsuario(usuarioDTO);
            // Crie um DTO para resposta para não expor a senha (mesmo hasheada)
            UsuarioResponseDTO response = new UsuarioResponseDTO(
                novoUsuario.getId(),
                novoUsuario.getUsername(),
                novoUsuario.getNome(),
                novoUsuario.getCpf() // etc.
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro interno ao cadastrar usuário."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@Valid @RequestBody LoginRequestDTO loginRequest) {
        Optional<Usuario> usuarioOptional = usuarioService.login(loginRequest);

        if (usuarioOptional.isPresent()) {
            Usuario usuarioLogado = usuarioOptional.get();
            // Em um sistema real, você geraria um token JWT aqui e o retornaria.
            // Por agora, vamos retornar os dados do usuário (sem a senha).
            UsuarioResponseDTO response = new UsuarioResponseDTO(
                usuarioLogado.getId(),
                usuarioLogado.getUsername(),
                usuarioLogado.getNome(),
                usuarioLogado.getCpf() // Adicione outros campos se desejar
            );
            return ResponseEntity.ok(response);
        } else {
            // Mensagem genérica para não vazar informação se o usuário existe ou não
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("erro", "Usuário ou senha inválidos."));
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
    
    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }
}
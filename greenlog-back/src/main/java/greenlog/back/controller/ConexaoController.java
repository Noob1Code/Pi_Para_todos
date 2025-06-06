package greenlog.back.controller;

import greenlog.back.model.Conexao;
import greenlog.back.repository.ConexaoRepository;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import greenlog.back.repository.ConexaoRepository;

/**
 *
 * @author Cansei2
 */

@RestController
@RequestMapping("/api/conexaos")
public class ConexaoController {

    private final ConexaoRepository conexaoRepository;

    public ConexaoController(ConexaoRepository cadastroConexaosRepository) {
        this.conexaoRepository = cadastroConexaosRepository;
    }

    @GetMapping
    public List<Conexao> get() {
        return conexaoRepository.findAll();
    }

    @PostMapping
    public void post(@RequestBody Conexao conexao) {
        conexaoRepository.save(conexao);
    }

    @PutMapping("/{id}")
    public void atualizar(@PathVariable int id, @RequestBody Conexao conexaoAtualizada) {
        Conexao conexaoExistente = conexaoRepository.findById(id).orElse(null);
        if (conexaoExistente != null) {
            conexaoExistente.setRua(conexaoAtualizada.getRua());
            conexaoExistente.setOrigem(conexaoAtualizada.getOrigem());
            conexaoExistente.setDestino(conexaoAtualizada.getDestino());
            conexaoExistente.setQuilometros(conexaoAtualizada.getQuilometros());
            conexaoRepository.save(conexaoExistente);
        }

    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable int id) {
        conexaoRepository.deleteById(id);
    }
}

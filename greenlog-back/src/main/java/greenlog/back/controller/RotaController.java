package greenlog.back.controller;

import greenlog.back.model.Rota;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import greenlog.back.repository.RotaRepository;

/**
 *
 * @author Cansei2
 */

@RestController
@RequestMapping("/api/rotas")
public class RotaController {

    private final RotaRepository rotasRepository;

    public RotaController(RotaRepository cadastroRotasRepository) {
        this.rotasRepository = cadastroRotasRepository;
    }

    @GetMapping
    public List<Rota> get() {
        return rotasRepository.findAll();
    }

    @PostMapping
    public void post(@RequestBody Rota rota) {
        System.out.println(rota.getPontoColetaOrigem());
        rotasRepository.save(rota);
    }

    @PutMapping("/{id}")
    public void atualizar(@PathVariable int id, @RequestBody Rota rotaAtualizada) {
        Rota rotaExistente = rotasRepository.findById(id).orElse(null);
        if (rotaExistente != null) {
            rotaExistente.setCaminhao(rotaAtualizada.getCaminhao());
            rotaExistente.setPontoColetaOrigem(rotaAtualizada.getPontoColetaOrigem());
            rotaExistente.setPontoColetaDestino(rotaAtualizada.getPontoColetaDestino());
            rotaExistente.setData(rotaAtualizada.getData());
            rotasRepository.save(rotaExistente);
        }

    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable int id) {
        rotasRepository.deleteById(id);
    }
}

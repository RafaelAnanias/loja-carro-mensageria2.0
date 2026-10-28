package br.org.edu.ifrn.LojaCarro.controllers;

import br.org.edu.ifrn.LojaCarro.CarroException;
import br.org.edu.ifrn.LojaCarro.dto.CarroMensagemDTO;
import br.org.edu.ifrn.LojaCarro.mensageria.Enviar;
import br.org.edu.ifrn.LojaCarro.model.Acao;
import br.org.edu.ifrn.LojaCarro.model.Cargo;
import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.model.Usuario;
import br.org.edu.ifrn.LojaCarro.services.CarroService;
import br.org.edu.ifrn.LojaCarro.services.LogAuditoriaService;
import br.org.edu.ifrn.LojaCarro.services.UsuarioClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/carro")
@CrossOrigin(origins = "*")
public class CarroController {

    private static final Logger log = LoggerFactory.getLogger(CarroController.class);
    private static final String FILA_RABBITMQ = "claudemyr_rafael";

    @Autowired
    private CarroService carroService;

    @Autowired
    private UsuarioClientService usuarioClientService;

    @Autowired
    private LogAuditoriaService logAuditoriaService;

    @PostMapping("/salvar")
    public ResponseEntity<Carro> salvarCarro(@RequestParam Long usuarioId, @RequestBody Carro c) {
        log.info("Tentativa de cadastro de veículo solicitada pelo usuário ID: {}", usuarioId);

        Usuario usuario = usuarioClientService.buscarPorId(usuarioId);

        if (usuario.getCargo() != Cargo.GERENTE && usuario.getCargo() != Cargo.ADMIN) {
            log.warn("Permissão negada: Usuário '{}' com cargo '{}' tentou cadastrar um carro.",
                    usuario.getNome(), usuario.getCargo());
            logAuditoriaService.registrar(usuario, Acao.CADASTRO, "CARRO", null, "FALHA");
            throw new CarroException("Acesso negado: Apenas Gerente ou Admin pode cadastrar veículos.");
        }

        try {
            Carro savedCarro = carroService.save(c);
            log.info("Carro modelo '{}' (ID: {}) cadastrado com sucesso pelo usuário '{}'.",
                    savedCarro.getModelo(), savedCarro.getId(), usuario.getNome());
            logAuditoriaService.registrar(usuario, Acao.CADASTRO, "CARRO", savedCarro.getId(), "SUCESSO");

            // Envia mensagem JSON para o RabbitMQ
            CarroMensagemDTO msg = new CarroMensagemDTO(savedCarro.getId(), savedCarro.getModelo(), savedCarro.getAno(), savedCarro.getPreco());
            Enviar.enviarMensagem(msg, FILA_RABBITMQ);

            return ResponseEntity.ok(savedCarro);
        } catch (CarroException ex) {
            logAuditoriaService.registrar(usuario, Acao.CADASTRO, "CARRO", null, "FALHA");
            throw ex;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carro> atualizarCarro(@PathVariable Long id, @RequestParam Long usuarioId, @RequestBody Carro c) {
        log.info("Atualizando carro com ID: {} solicitado por usuário ID: {}", id, usuarioId);
        Usuario usuario = usuarioClientService.buscarPorId(usuarioId);
        c.setId(id);
        try {
            Carro updatedCarro = carroService.update(c);
            log.info("Carro ID: {} atualizado com sucesso pelo usuário '{}'.", updatedCarro.getId(), usuario.getNome());
            logAuditoriaService.registrar(usuario, Acao.ATUALIZACAO, "CARRO", updatedCarro.getId(), "SUCESSO");

            // Envia mensagem JSON para o RabbitMQ
            CarroMensagemDTO msg = new CarroMensagemDTO(updatedCarro.getId(), updatedCarro.getModelo(), updatedCarro.getAno(), updatedCarro.getPreco());
            Enviar.enviarMensagem(msg, FILA_RABBITMQ);

            return ResponseEntity.ok(updatedCarro);
        } catch (CarroException ex) {
            logAuditoriaService.registrar(usuario, Acao.ATUALIZACAO, "CARRO", id, "FALHA");
            throw ex;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCarro(@PathVariable Long id, @RequestParam Long usuarioId) {
        log.info("Excluindo carro com ID: {} solicitado por usuário ID: {}", id, usuarioId);
        Usuario usuario = usuarioClientService.buscarPorId(usuarioId);
        try {
            // Busca o carro antes de deletar para poder pegar os dados para o RabbitMQ
            Optional<Carro> carroParaDeletar = carroService.findById(id);

            carroService.deleteById(id);
            log.info("Carro com ID: {} excluído com sucesso pelo usuário '{}'.", id, usuario.getNome());
            logAuditoriaService.registrar(usuario, Acao.EXCLUSAO, "CARRO", id, "SUCESSO");

            // Envia mensagem JSON para o RabbitMQ
            if(carroParaDeletar.isPresent()) {
                Carro carro = carroParaDeletar.get();
                CarroMensagemDTO msg = new CarroMensagemDTO(carro.getId(), carro.getModelo(), carro.getAno(), carro.getPreco());
                Enviar.enviarMensagem(msg, FILA_RABBITMQ);
            }

            return ResponseEntity.noContent().build();
        } catch (CarroException ex) {
            logAuditoriaService.registrar(usuario, Acao.EXCLUSAO, "CARRO", id, "FALHA");
            throw ex;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carro> pesquisarCarroPorId(@PathVariable Long id) {
        log.info("Buscando carro com ID: {}", id);
        Optional<Carro> carro = carroService.findById(id);
        if (carro.isEmpty()) {
            log.warn("Carro com ID: {} não foi encontrado.", id);
        }
        return carro.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/listarCarros")
    public ResponseEntity<List<Carro>> pesquisarTodosCarros() {
        log.info("Listando todos os carros cadastrados.");
        List<Carro> carros = carroService.findAll();
        return ResponseEntity.ok(carros);
    }

    @PostMapping(value = "/getCarro", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Carro> pesquisarCarroPorModelo(@RequestBody String modelo) {
        log.info("Pesquisando carro pelo modelo: '{}'", modelo.trim());
        Optional<Carro> carro = carroService.findByModelo(modelo.trim());
        if (carro.isEmpty()) {
            log.warn("Nenhum carro encontrado para o modelo: '{}'", modelo.trim());
        }
        return carro.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/deleteCarro", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deletarCarroPorModelo(@RequestBody String payload, @RequestParam Long usuarioId) {
        log.info("Tentativa de exclusão do carro pelo modelo: '{}'", payload.trim());
        Usuario usuario = usuarioClientService.buscarPorId(usuarioId);
        try {
            Carro carro = carroService.deleteByModelo(payload.trim());
            log.info("Carro modelo '{}' excluído com sucesso pelo usuário '{}'.", carro.getModelo(), usuario.getNome());
            logAuditoriaService.registrar(usuario, Acao.EXCLUSAO, "CARRO", carro.getId(), "SUCESSO");

            // Envia mensagem JSON para o RabbitMQ
            CarroMensagemDTO msg = new CarroMensagemDTO(carro.getId(), carro.getModelo(), carro.getAno(), carro.getPreco());
            Enviar.enviarMensagem(msg, FILA_RABBITMQ);

            return ResponseEntity.ok("Carro deletado: " + carro.getModelo());
        } catch (CarroException ex) {
            logAuditoriaService.registrar(usuario, Acao.EXCLUSAO, "CARRO", null, "FALHA");
            throw ex;
        }
    }

    @PostMapping(value = "/updateCarro", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Carro> atualizarCarroPorModelo(@RequestBody String payload, @RequestParam Long usuarioId) {
        log.info("Recebida requisição legado para atualizar carro por modelo: {}", payload);
        Usuario usuario = usuarioClientService.buscarPorId(usuarioId);
        try {
            String[] partes = payload.split(",", 2);
            if (partes.length < 2) {
                log.warn("Payload inválido recebido em /updateCarro: {}", payload);
                throw new CarroException("Payload inválido. Use modelo,preco.");
            }
            String modelo = partes[0].trim();
            double preco = parsePreco(partes[1].trim());
            Carro updatedCarro = carroService.updateByModelo(modelo, preco);
            log.info("Carro modelo '{}' atualizado com novo preço: {} pelo usuário '{}'.", modelo, preco, usuario.getNome());
            logAuditoriaService.registrar(usuario, Acao.ATUALIZACAO, "CARRO", updatedCarro.getId(), "SUCESSO");

            // Envia mensagem JSON para o RabbitMQ
            CarroMensagemDTO msg = new CarroMensagemDTO(updatedCarro.getId(), updatedCarro.getModelo(), updatedCarro.getAno(), updatedCarro.getPreco());
            Enviar.enviarMensagem(msg, FILA_RABBITMQ);

            return ResponseEntity.ok(updatedCarro);
        } catch (CarroException ex) {
            logAuditoriaService.registrar(usuario, Acao.ATUALIZACAO, "CARRO", null, "FALHA");
            throw ex;
        }
    }

    @PostMapping(value = "/teste", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> bomDia(@RequestBody String nome) {
        log.info("Endpoint /teste acionado com o nome: {}", nome.trim());
        return ResponseEntity.ok("Bom dia, " + nome.trim());
    }

    private double parsePreco(String preco) {
        try {
            return Double.parseDouble(preco);
        } catch (NumberFormatException ex) {
            log.error("Erro ao converter valor do preço '{}': {}", preco, ex.getMessage());
            throw new CarroException("Preço inválido: " + preco);
        }
    }
}
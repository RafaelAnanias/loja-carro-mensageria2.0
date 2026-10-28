package br.org.edu.ifrn.LojaCarro.controllers;

import br.org.edu.ifrn.LojaCarro.dto.LogConsultaDTO;
import br.org.edu.ifrn.LojaCarro.model.LogAuditoria;
import br.org.edu.ifrn.LojaCarro.services.LogAuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/log")
@CrossOrigin(origins = "*")
public class LogController {

    @Autowired
    private LogAuditoriaService logAuditoriaService;

    /**
     * Consulta de auditoria: mostra quem fez, o que fez, sobre qual recurso,
     * o ID afetado e o resultado.
     *
     * Ex.: { "quemFez": "Joao", "acao": "EXCLUSAO", "afetado": "CARRO",
     *        "idAfetado": 3, "resultado": "SUCESSO" }
     */
    @GetMapping("/consulta")
    public ResponseEntity<List<LogConsultaDTO>> consultarLogs() {
        List<LogConsultaDTO> lista = logAuditoriaService.listarTodos().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    private LogConsultaDTO toDTO(LogAuditoria log) {
        return new LogConsultaDTO(
                log.getId(),
                log.getNomeUsuario(),
                log.getCargo() != null ? log.getCargo().name() : null,
                log.getAcao() != null ? log.getAcao().name() : null,
                log.getAfetado(),
                log.getIdAfetado(),
                log.getResultado(),
                log.getDataHora()
        );
    }
}
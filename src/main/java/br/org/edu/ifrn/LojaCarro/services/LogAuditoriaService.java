package br.org.edu.ifrn.LojaCarro.services;

import br.org.edu.ifrn.LojaCarro.model.Acao;
import br.org.edu.ifrn.LojaCarro.model.LogAuditoria;
import br.org.edu.ifrn.LojaCarro.model.Usuario;
import br.org.edu.ifrn.LojaCarro.repository.LogAuditoriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogAuditoriaService {

    private static final Logger log = LoggerFactory.getLogger(LogAuditoriaService.class);

    @Autowired
    private LogAuditoriaRepository logAuditoriaRepository;

    /**
     * Registra uma ação realizada no sistema.
     *
     * @param usuario   usuário que executou a ação (obtido da API de usuários)
     * @param acao      ação executada (CADASTRO, ATUALIZACAO, EXCLUSAO, CONSULTA)
     * @param afetado   recurso afetado (CARRO, USUARIO, ...)
     * @param idAfetado id do recurso afetado (pode ser nulo)
     * @param resultado SUCESSO ou FALHA
     */
    public void registrar(Usuario usuario, Acao acao, String afetado, Long idAfetado, String resultado) {
        LogAuditoria logAuditoria = new LogAuditoria();
        logAuditoria.setUsuarioId(usuario != null ? usuario.getId() : null);
        logAuditoria.setNomeUsuario(usuario != null && usuario.getNome() != null ? usuario.getNome() : "DESCONHECIDO");
        logAuditoria.setCargo(usuario != null ? usuario.getCargo() : null);
        logAuditoria.setAcao(acao);
        logAuditoria.setAfetado(afetado);
        logAuditoria.setIdAfetado(idAfetado);
        logAuditoria.setResultado(resultado);
        logAuditoria.setDataHora(LocalDateTime.now());

        try {
            logAuditoriaRepository.save(logAuditoria);
            log.info("LOG | Quem fez: {} | Ação: {} | Afetado: {} | ID: {} | Resultado: {}",
                    logAuditoria.getNomeUsuario(), acao, afetado, idAfetado, resultado);
        } catch (Exception e) {
            log.error("Erro ao gravar log de auditoria: {}", e.getMessage(), e);
        }
    }

    public List<LogAuditoria> listarTodos() {
        return logAuditoriaRepository.findAllByOrderByDataHoraDesc();
    }
}
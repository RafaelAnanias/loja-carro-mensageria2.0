package br.org.edu.ifrn.LojaCarro.repository;

import br.org.edu.ifrn.LojaCarro.model.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long> {
    List<LogAuditoria> findAllByOrderByDataHoraDesc();
}
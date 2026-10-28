package br.org.edu.ifrn.LojaCarro.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs_auditoria")
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private String nomeUsuario;

    @Enumerated(EnumType.STRING)
    private Cargo cargo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Acao acao;

    @Column(nullable = false)
    private String afetado;

    private Long idAfetado;

    @Column(nullable = false)
    private String resultado;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    public LogAuditoria() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Acao getAcao() {
        return acao;
    }

    public void setAcao(Acao acao) {
        this.acao = acao;
    }

    public String getAfetado() {
        return afetado;
    }

    public void setAfetado(String afetado) {
        this.afetado = afetado;
    }

    public Long getIdAfetado() {
        return idAfetado;
    }

    public void setIdAfetado(Long idAfetado) {
        this.idAfetado = idAfetado;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
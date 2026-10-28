package br.org.edu.ifrn.LojaCarro.dto;

import java.time.LocalDateTime;

public class LogConsultaDTO {
    private Long id;
    private String quemFez;
    private String cargo;
    private String acao;
    private String afetado;
    private Long idAfetado;
    private String resultado;
    private LocalDateTime dataHora;

    public LogConsultaDTO() {
    }

    public LogConsultaDTO(Long id, String quemFez, String cargo, String acao, String afetado,
                          Long idAfetado, String resultado, LocalDateTime dataHora) {
        this.id = id;
        this.quemFez = quemFez;
        this.cargo = cargo;
        this.acao = acao;
        this.afetado = afetado;
        this.idAfetado = idAfetado;
        this.resultado = resultado;
        this.dataHora = dataHora;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuemFez() {
        return quemFez;
    }

    public void setQuemFez(String quemFez) {
        this.quemFez = quemFez;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
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
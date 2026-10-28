package br.org.edu.ifrn.LojaCarro.dto;

import java.io.Serializable;

public class CarroMensagemDTO implements Serializable {
    private Long id;
    private String modelo;
    private Integer ano;
    private Double preco;

    public CarroMensagemDTO() {}

    public CarroMensagemDTO(Long id, String modelo, Integer ano, Double preco) {
        this.id = id;
        this.modelo = modelo;
        this.ano = ano;
        this.preco = preco;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }
}
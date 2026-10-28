package br.org.edu.ifrn.LojaCarro.model;

import java.io.Serializable;

public class Usuario implements Serializable {
    private Long id;
    private String nome;
    private Cargo cargo;

    public Usuario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }
}
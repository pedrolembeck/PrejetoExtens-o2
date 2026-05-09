package com.doacoes.model;

public class TipoDoacao {

    private Integer id;
    private String nome;

    public TipoDoacao() {
    }

    public TipoDoacao(String nome) {
        this.nome = nome;
    }

    public TipoDoacao(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "TipoDoacao{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
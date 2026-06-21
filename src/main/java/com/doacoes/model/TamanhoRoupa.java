package com.doacoes.model;

public class TamanhoRoupa {

    private Long id;
    private String tamanho;
    private String tipo;

    public TamanhoRoupa() {
    }

    public TamanhoRoupa(Long id, String tamanho, String tipo) {
        this.id = id;
        this.tamanho = tamanho;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return tamanho + " - " + tipo;
    }
}
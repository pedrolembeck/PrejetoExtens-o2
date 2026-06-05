package com.doacoes.model;

import java.time.LocalDate;

public class Doacao {

    private Long id;
    private String nomeDoador;
    private String descricaoItem;
    private int quantidade;
    private String categoria;
    private String tamanho;
    private LocalDate dataDoacao;
    private String observacao;

    public Doacao() {
    }

    public Doacao(Long id, String nomeDoador, String descricaoItem, int quantidade,
                  String categoria, String tamanho, LocalDate dataDoacao, String observacao) {
        this.id = id;
        this.nomeDoador = nomeDoador;
        this.descricaoItem = descricaoItem;
        this.quantidade = quantidade;
        this.categoria = categoria;
        this.tamanho = tamanho;
        this.dataDoacao = dataDoacao;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeDoador() {
        return nomeDoador;
    }

    public void setNomeDoador(String nomeDoador) {
        this.nomeDoador = nomeDoador;
    }

    public String getDescricaoItem() {
        return descricaoItem;
    }

    public void setDescricaoItem(String descricaoItem) {
        this.descricaoItem = descricaoItem;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public LocalDate getDataDoacao() {
        return dataDoacao;
    }

    public void setDataDoacao(LocalDate dataDoacao) {
        this.dataDoacao = dataDoacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
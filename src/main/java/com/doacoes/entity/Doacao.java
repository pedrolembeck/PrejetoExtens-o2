package com.doacoes.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "doacao")
public class Doacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_doador", nullable = false)
    private String nomeDoador;

    @Column(name = "descricao_item", nullable = false)
    private String descricaoItem;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private String unidade;

    @Column(name = "detalhe_unidade")
    private String detalheUnidade;

    private String tamanho;

    @Column(name = "data_doacao", nullable = false)
    private LocalDate dataDoacao;

    public Doacao() {
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

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getDetalheUnidade() {
        return detalheUnidade;
    }

    public void setDetalheUnidade(String detalheUnidade) {
        this.detalheUnidade = detalheUnidade;
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
}
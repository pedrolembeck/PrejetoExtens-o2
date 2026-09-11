package com.doacoes.dto;

import com.doacoes.entity.Doacao;

import java.time.LocalDate;

public class DoacaoResponseDTO {

    private Long id;
    private String nomeDoador;
    private String descricaoItem;
    private Integer quantidade;
    private String categoria;
    private String unidade;
    private String detalheUnidade;
    private String tamanho;
    private LocalDate dataDoacao;

    public DoacaoResponseDTO(Doacao doacao) {
        this.id = doacao.getId();
        this.nomeDoador = doacao.getNomeDoador();
        this.descricaoItem = doacao.getDescricaoItem();
        this.quantidade = doacao.getQuantidade();
        this.categoria = doacao.getCategoria();
        this.unidade = doacao.getUnidade();
        this.detalheUnidade = doacao.getDetalheUnidade();
        this.tamanho = doacao.getTamanho();
        this.dataDoacao = doacao.getDataDoacao();
    }

    public Long getId() {
        return id;
    }

    public String getNomeDoador() {
        return nomeDoador;
    }

    public String getDescricaoItem() {
        return descricaoItem;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getUnidade() {
        return unidade;
    }

    public String getDetalheUnidade() {
        return detalheUnidade;
    }

    public String getTamanho() {
        return tamanho;
    }

    public LocalDate getDataDoacao() {
        return dataDoacao;
    }
}
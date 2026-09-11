package com.doacoes.dto;

import com.doacoes.entity.Estoque;

public class EstoqueResponseDTO {

    private Long id;
    private String descricao;
    private Integer quantidade;
    private String categoria;

    public EstoqueResponseDTO(Estoque estoque) {
        this.id = estoque.getId();
        this.descricao = estoque.getDescricao();
        this.quantidade = estoque.getQuantidade();
        this.categoria = estoque.getCategoria();
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public String getCategoria() {
        return categoria;
    }
}
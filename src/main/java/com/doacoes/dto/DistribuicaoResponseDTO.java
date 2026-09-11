package com.doacoes.dto;

import com.doacoes.entity.Distribuicao;

import java.time.LocalDateTime;

public class DistribuicaoResponseDTO {

    private Long id;
    private Long estoqueId;
    private Long receptorId;
    private Integer quantidade;
    private LocalDateTime data;

    public DistribuicaoResponseDTO(Distribuicao distribuicao) {
        this.id = distribuicao.getId();
        this.estoqueId = distribuicao.getEstoqueId();
        this.receptorId = distribuicao.getReceptorId();
        this.quantidade = distribuicao.getQuantidade();
        this.data = distribuicao.getData();
    }

    public Long getId() {
        return id;
    }

    public Long getEstoqueId() {
        return estoqueId;
    }

    public Long getReceptorId() {
        return receptorId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public LocalDateTime getData() {
        return data;
    }
}
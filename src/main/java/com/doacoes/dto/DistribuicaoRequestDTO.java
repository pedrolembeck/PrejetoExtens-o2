package com.doacoes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DistribuicaoRequestDTO {

    @NotNull(message = "O estoque é obrigatório")
    private Long estoqueId;

    @NotNull(message = "O receptor é obrigatório")
    private Long receptorId;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    private Integer quantidade;

    public Long getEstoqueId() {
        return estoqueId;
    }

    public void setEstoqueId(Long estoqueId) {
        this.estoqueId = estoqueId;
    }

    public Long getReceptorId() {
        return receptorId;
    }

    public void setReceptorId(Long receptorId) {
        this.receptorId = receptorId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
package com.doacoes.dto;

import com.doacoes.entity.TamanhoRoupa;

public class TamanhoRoupaResponseDTO {

    private Long id;
    private String tamanho;
    private String tipo;

    public TamanhoRoupaResponseDTO(
            TamanhoRoupa tamanhoRoupa) {

        this.id = tamanhoRoupa.getId();
        this.tamanho = tamanhoRoupa.getTamanho();
        this.tipo = tamanhoRoupa.getTipo();
    }

    public Long getId() {
        return id;
    }

    public String getTamanho() {
        return tamanho;
    }

    public String getTipo() {
        return tipo;
    }
}
package com.doacoes.dto;

import jakarta.validation.constraints.NotBlank;

public class TamanhoRoupaRequestDTO {

    @NotBlank(message = "Tamanho é obrigatório")
    private String tamanho;

    @NotBlank(message = "Tipo é obrigatório")
    private String tipo;

    public TamanhoRoupaRequestDTO() {
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
}
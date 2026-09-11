package com.doacoes.dto;

import com.doacoes.entity.TipoDoacao;

public class TipoDoacaoResponseDTO {

    private Long id;
    private String nome;

    public TipoDoacaoResponseDTO(
            TipoDoacao tipoDoacao) {

        this.id = tipoDoacao.getId();
        this.nome = tipoDoacao.getNome();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
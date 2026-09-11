package com.doacoes.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "tamanho_roupa",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tamanho_roupa_tamanho_tipo",
                        columnNames = {"tamanho", "tipo"}
                )
        }
)
public class TamanhoRoupa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tamanho;

    @Column(nullable = false)
    private String tipo;

    public TamanhoRoupa() {
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
}
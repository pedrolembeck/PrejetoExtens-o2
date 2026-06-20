package com.doacoes.model;

import java.time.LocalDateTime;

public class Distribuicao {
    private String cpfReceptor;
    private Long id;
    private Long receptorId;
    private Long estoqueId;
    private int quantidade;
    private LocalDateTime data;
    private String receptorCpf;


    public Distribuicao() {}

    public Distribuicao(Long id, Long receptorId, Long estoqueId, int quantidade, LocalDateTime data) {
        this.id = id;
        this.receptorId = receptorId;
        this.estoqueId = estoqueId;
        this.quantidade = quantidade;
        this.data = data;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getReceptorId() { return receptorId; }
    public void setReceptorId(Long receptorId) { this.receptorId = receptorId; }

    public Long getEstoqueId() { return estoqueId; }
    public void setEstoqueId(Long estoqueId) { this.estoqueId = estoqueId; }


    public String getCpfReceptor() {
        return cpfReceptor;
    }

    public void setCpfReceptor(String cpfReceptor) {
        this.cpfReceptor = cpfReceptor;
    }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }
    public String getReceptorCpf() { return receptorCpf; }
    public void setReceptorCpf(String receptorCpf) { this.receptorCpf = receptorCpf; }
}
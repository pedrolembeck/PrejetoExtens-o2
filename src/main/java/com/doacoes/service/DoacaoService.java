package com.doacoes.service;

import com.doacoes.dao.DoacaoDAO;
import com.doacoes.model.Doacao;

import javax.swing.JOptionPane;
import java.util.List;

public class DoacaoService {

    private final DoacaoDAO dao = new DoacaoDAO();

    public void inicializar() {
        dao.criarTabela();
    }

    public boolean salvar(Doacao doacao) {
        if (!validar(doacao)) {
            return false;
        }

        dao.salvar(doacao);
        return true;
    }

    public List<Doacao> listar() {
        return dao.listar();
    }

    public Doacao buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public boolean atualizar(Doacao doacao) {
        if (!validar(doacao)) {
            return false;
        }

        dao.atualizar(doacao);
        return true;
    }

    public void deletar(Long id) {
        dao.deletar(id);
    }

    private boolean validar(Doacao doacao) {
        if (doacao.getNomeDoador() == null || doacao.getNomeDoador().isBlank()) {
            JOptionPane.showMessageDialog(null, "Nome do doador é obrigatório!");
            return false;
        }

        if (doacao.getDescricaoItem() == null || doacao.getDescricaoItem().isBlank()) {
            JOptionPane.showMessageDialog(null, "Descrição do item é obrigatória!");
            return false;
        }

        if (doacao.getQuantidade() <= 0) {
            JOptionPane.showMessageDialog(null, "Quantidade deve ser maior que zero!");
            return false;
        }

        if (doacao.getCategoria() == null || doacao.getCategoria().isBlank()) {
            JOptionPane.showMessageDialog(null, "Categoria é obrigatória!");
            return false;
        }

        if (doacao.getDataDoacao() == null) {
            JOptionPane.showMessageDialog(null, "Data da doação é obrigatória!");
            return false;
        }

        return true;
    }
}
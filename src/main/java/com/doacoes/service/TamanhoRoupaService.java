package com.doacoes.service;

import com.doacoes.dao.TamanhoRoupaDAO;
import com.doacoes.model.TamanhoRoupa;

import javax.swing.JOptionPane;
import java.util.List;

public class TamanhoRoupaService {

    private final TamanhoRoupaDAO dao = new TamanhoRoupaDAO();

    public void inicializar() {
        dao.criarTabela();
    }

    public boolean salvar(TamanhoRoupa tamanhoRoupa) {
        if (!validar(tamanhoRoupa)) {
            return false;
        }

        formatar(tamanhoRoupa);

        try {
            dao.salvar(tamanhoRoupa);
            return true;
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null,
                    "Não foi possível salvar. Verifique se esse tamanho já existe para esse tipo.");
            return false;
        }
    }

    public List<TamanhoRoupa> listar() {
        return dao.listar();
    }

    public TamanhoRoupa buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public boolean atualizar(TamanhoRoupa tamanhoRoupa) {
        if (!validar(tamanhoRoupa)) {
            return false;
        }

        formatar(tamanhoRoupa);

        try {
            dao.atualizar(tamanhoRoupa);
            return true;
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null,
                    "Não foi possível atualizar. Verifique se esse tamanho já existe para esse tipo.");
            return false;
        }
    }

    public void deletar(Long id) {
        dao.deletar(id);
    }

    private boolean validar(TamanhoRoupa tamanhoRoupa) {
        if (tamanhoRoupa.getTamanho() == null || tamanhoRoupa.getTamanho().isBlank()) {
            JOptionPane.showMessageDialog(null, "Tamanho é obrigatório!");
            return false;
        }

        if (tamanhoRoupa.getTipo() == null || tamanhoRoupa.getTipo().isBlank()) {
            JOptionPane.showMessageDialog(null, "Tipo é obrigatório!");
            return false;
        }

        return true;
    }

    private void formatar(TamanhoRoupa tamanhoRoupa) {
        tamanhoRoupa.setTamanho(tamanhoRoupa.getTamanho().trim().toUpperCase());
        tamanhoRoupa.setTipo(tamanhoRoupa.getTipo().trim().toUpperCase());
    }
}
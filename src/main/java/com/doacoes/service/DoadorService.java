package com.doacoes.service;

import com.doacoes.dao.DoadorDAO;
import com.doacoes.model.Doador;

import javax.swing.JOptionPane;
import java.util.List;

public class DoadorService {

    private final DoadorDAO dao = new DoadorDAO();

    public void inicializar() {
        dao.criarTabela();
    }

    public boolean salvar(Doador doador) {
        if (!validar(doador)) {
            return false;
        }

        formatar(doador);

        try {
            dao.salvar(doador);
            return true;
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar doador. Verifique os dados informados.");
            return false;
        }
    }

    public List<Doador> listar() {
        return dao.listar();
    }

    public Doador buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public boolean atualizar(Doador doador) {
        if (!validar(doador)) {
            return false;
        }

        formatar(doador);

        try {
            dao.atualizar(doador);
            return true;
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar doador. Verifique os dados informados.");
            return false;
        }
    }

    public void deletar(Long id) {
        Doador doador = dao.buscarPorId(id);

        if (doador == null) {
            JOptionPane.showMessageDialog(null, "Doador não encontrado!");
            return;
        }

        dao.deletar(id);
    }

    public List<Doador> pesquisar(String termo) {
        if (termo == null || termo.isBlank()) {
            return listar();
        }

        return dao.pesquisar(termo.trim());
    }

    private boolean validar(Doador doador) {
        if (doador.getNome() == null || doador.getNome().isBlank()) {
            JOptionPane.showMessageDialog(null, "Nome é obrigatório!");
            return false;
        }

        if (doador.getCpf() != null && !doador.getCpf().isBlank()) {
            String cpfLimpo = doador.getCpf().replaceAll("[^0-9]", "");
            if (cpfLimpo.length() != 11) {
                JOptionPane.showMessageDialog(null, "CPF inválido! Informe 11 dígitos.");
                return false;
            }
        }

        if (doador.getEmail() != null && !doador.getEmail().isBlank()) {
            if (!doador.getEmail().contains("@")) {
                JOptionPane.showMessageDialog(null, "E-mail inválido!");
                return false;
            }
        }

        return true;
    }

    private void formatar(Doador doador) {
        doador.setNome(doador.getNome().trim().toUpperCase());

        if (doador.getEndereco() != null && !doador.getEndereco().isBlank()) {
            doador.setEndereco(doador.getEndereco().trim());
        }

        if (doador.getTelefone() != null && !doador.getTelefone().isBlank()) {
            doador.setTelefone(doador.getTelefone().trim());
        }

        if (doador.getEmail() != null && !doador.getEmail().isBlank()) {
            doador.setEmail(doador.getEmail().trim().toLowerCase());
        }
    }
}
package com.doacoes.service;

import com.doacoes.dao.ReceptorDAO;
import com.doacoes.model.Receptor;

import javax.swing.JOptionPane;
import java.util.List;

public class ReceptorService {

    private final ReceptorDAO dao = new ReceptorDAO();

    public void inicializar() {
        dao.criarTabela();
    }

    public boolean salvar(Receptor receptor) {
        if (!validar(receptor)) {
            return false;
        }

        formatar(receptor);

        try {
            dao.salvar(receptor);
            return true;
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar receptor. Verifique os dados informados.");
            return false;
        }
    }

    public List<Receptor> listar() {
        return dao.listar();
    }

    public Receptor buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public boolean atualizar(Receptor receptor) {
        if (!validar(receptor)) {
            return false;
        }

        formatar(receptor);

        try {
            dao.atualizar(receptor);
            return true;
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar receptor. Verifique os dados informados.");
            return false;
        }
    }

    public void deletar(Long id) {
        Receptor receptor = dao.buscarPorId(id);

        if (receptor == null) {
            JOptionPane.showMessageDialog(null, "Receptor não encontrado!");
            return;
        }

        dao.deletar(id);
    }

    public List<Receptor> pesquisar(String termo) {
        if (termo == null || termo.isBlank()) {
            return listar();
        }

        return dao.pesquisar(termo.trim());
    }

    private boolean validar(Receptor receptor) {
        if (receptor.getNome() == null || receptor.getNome().isBlank()) {
            JOptionPane.showMessageDialog(null, "Nome é obrigatório!");
            return false;
        }

        if (receptor.getCpf() != null && !receptor.getCpf().isBlank()) {
            String cpfLimpo = receptor.getCpf().replaceAll("[^0-9]", "");
            if (cpfLimpo.length() != 11) {
                JOptionPane.showMessageDialog(null, "CPF inválido! Informe 11 dígitos.");
                return false;
            }
        }

        if (receptor.getEmail() != null && !receptor.getEmail().isBlank()) {
            if (!receptor.getEmail().contains("@")) {
                JOptionPane.showMessageDialog(null, "E-mail inválido!");
                return false;
            }
        }

        return true;
    }

    private void formatar(Receptor receptor) {
        receptor.setNome(receptor.getNome().trim().toUpperCase());

        if (receptor.getEndereco() != null && !receptor.getEndereco().isBlank()) {
            receptor.setEndereco(receptor.getEndereco().trim());
        }

        if (receptor.getTelefone() != null && !receptor.getTelefone().isBlank()) {
            receptor.setTelefone(receptor.getTelefone().trim());
        }

        if (receptor.getEmail() != null && !receptor.getEmail().isBlank()) {
            receptor.setEmail(receptor.getEmail().trim().toLowerCase());
        }
    }
}
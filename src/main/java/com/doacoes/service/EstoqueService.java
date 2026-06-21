package com.doacoes.service;

import com.doacoes.dao.EstoqueDAO;
import com.doacoes.model.Estoque;

import javax.swing.JOptionPane;
import java.util.List;

public class EstoqueService {

    private final EstoqueDAO dao = new EstoqueDAO();

    public void inicializar() {
        dao.criarTabela();
    }

    public boolean salvar(Estoque estoque) {

        if (estoque.getDescricao() == null || estoque.getDescricao().isBlank()) {
            JOptionPane.showMessageDialog(null, "Descrição inválida!");
            return false;
        }

        if (estoque.getQuantidade() < 0) {
            JOptionPane.showMessageDialog(null, "Quantidade não pode ser negativa!");
            return false;
        }

        if (estoque.getCategoria() == null || estoque.getCategoria().isBlank()) {
            JOptionPane.showMessageDialog(null, "Categoria inválida!");
            return false;
        }

        dao.salvar(estoque);
        return true;
    }

    public List<Estoque> listar() {
        return dao.listar();
    }

    public Estoque buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public boolean atualizar(Estoque estoque) {

        if (estoque.getDescricao() == null || estoque.getDescricao().isBlank()) {
            JOptionPane.showMessageDialog(null, "Descrição inválida!");
            return false;
        }

        if (estoque.getQuantidade() < 0) {
            JOptionPane.showMessageDialog(null, "Quantidade não pode ser negativa!");
            return false;
        }

        if (estoque.getCategoria() == null || estoque.getCategoria().isBlank()) {
            JOptionPane.showMessageDialog(null, "Categoria inválida!");
            return false;
        }

        dao.atualizar(estoque);
        return true;
    }

    public void deletar(Long id) {

        Estoque estoque = dao.buscarPorId(id);

        if (estoque == null) {
            JOptionPane.showMessageDialog(null, "Item de estoque não encontrado!");
            return;
        }

        dao.deletar(id);
    }

    public boolean adicionarQuantidade(Long id, int quantidade) {

        if (quantidade <= 0) {
            JOptionPane.showMessageDialog(null, "Quantidade deve ser maior que zero!");
            return false;
        }

        Estoque estoque = dao.buscarPorId(id);

        if (estoque == null) {
            JOptionPane.showMessageDialog(null, "Item não encontrado!");
            return false;
        }

        dao.adicionarQuantidade(id, quantidade);
        return true;
    }

    public boolean adicionarOuCriarPorDoacao(String descricao, int quantidade, String categoria) {

        if (descricao == null || descricao.isBlank()) {
            JOptionPane.showMessageDialog(null, "Descrição do item inválida!");
            return false;
        }

        if (quantidade <= 0) {
            JOptionPane.showMessageDialog(null, "Quantidade da doação deve ser maior que zero!");
            return false;
        }

        if (categoria == null || categoria.isBlank()) {
            JOptionPane.showMessageDialog(null, "Categoria inválida!");
            return false;
        }

        String descricaoFormatada = descricao.trim().toUpperCase();
        String categoriaFormatada = categoria.trim().toUpperCase();

        Estoque estoque = dao.buscarPorDescricaoECategoria(descricaoFormatada, categoriaFormatada);

        if (estoque != null) {
            dao.adicionarQuantidade(estoque.getId(), quantidade);
        } else {
            Estoque novoEstoque = new Estoque();
            novoEstoque.setDescricao(descricaoFormatada);
            novoEstoque.setQuantidade(quantidade);
            novoEstoque.setCategoria(categoriaFormatada);

            dao.salvar(novoEstoque);
        }

        return true;
    }

    public boolean removerQuantidade(Long id, int quantidade) {

        if (quantidade <= 0) {
            JOptionPane.showMessageDialog(null, "Quantidade deve ser maior que zero!");
            return false;
        }

        Estoque estoque = dao.buscarPorId(id);

        if (estoque == null) {
            JOptionPane.showMessageDialog(null, "Item não encontrado!");
            return false;
        }

        if (estoque.getQuantidade() < quantidade) {
            JOptionPane.showMessageDialog(
                    null,
                    "Quantidade insuficiente! Disponível: " + estoque.getQuantidade()
            );
            return false;
        }

        dao.removerQuantidade(id, quantidade);
        return true;
    }
}
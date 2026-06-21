package com.doacoes.service;

import com.doacoes.dao.DoacaoDAO;
import com.doacoes.model.Doacao;

import javax.swing.JOptionPane;
import java.util.List;

public class DoacaoService {

    private final DoacaoDAO dao = new DoacaoDAO();
    private final EstoqueService estoqueService = new EstoqueService();

    public void inicializar() {
        dao.criarTabela();
        estoqueService.inicializar();
    }

    public boolean salvar(Doacao doacao) {
        if (!validar(doacao)) {
            return false;
        }

        dao.salvar(doacao);

        estoqueService.adicionarOuCriarPorDoacao(
                montarDescricaoParaEstoque(doacao),
                doacao.getQuantidade(),
                doacao.getCategoria()
        );

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

    public List<Doacao> pesquisar(String termo) {
        if (termo == null || termo.isBlank()) {
            return listar();
        }

        return dao.pesquisar(termo.trim());
    }

    private String montarDescricaoParaEstoque(Doacao doacao) {
        if ("VESTUARIO".equals(doacao.getCategoria())
                && doacao.getTamanho() != null
                && !doacao.getTamanho().isBlank()) {
            return doacao.getDescricaoItem() + " - TAM " + doacao.getTamanho();
        }

        if ("ALIMENTOS".equals(doacao.getCategoria())) {
            String descricao = doacao.getDescricaoItem();

            if (doacao.getUnidade() != null
                    && !doacao.getUnidade().isBlank()
                    && !"UNIDADE".equals(doacao.getUnidade())) {
                descricao += " - " + doacao.getUnidade();
            }

            if (doacao.getDetalheUnidade() != null
                    && !doacao.getDetalheUnidade().isBlank()) {
                descricao += " " + doacao.getDetalheUnidade();
            }

            return descricao;
        }

        return doacao.getDescricaoItem();
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

        if (doacao.getUnidade() == null || doacao.getUnidade().isBlank()) {
            JOptionPane.showMessageDialog(null, "Unidade é obrigatória!");
            return false;
        }

        if ("VESTUARIO".equals(doacao.getCategoria())
                && (doacao.getTamanho() == null || doacao.getTamanho().isBlank())) {
            JOptionPane.showMessageDialog(null, "Tamanho é obrigatório para vestuário!");
            return false;
        }

        if (doacao.getDataDoacao() == null) {
            JOptionPane.showMessageDialog(null, "Data da doação é obrigatória!");
            return false;
        }

        return true;
    }
}
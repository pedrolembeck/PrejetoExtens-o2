package com.doacoes.view;

import com.doacoes.model.Estoque;
import com.doacoes.service.EstoqueService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EstoqueView extends JFrame {

    private final EstoqueService service = new EstoqueService();

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public EstoqueView() {
        service.inicializar();
        configurarJanela();
        construirInterface();
        carregarTabela();
    }

    private void configurarJanela() {
        setTitle("Gerenciamento de Estoque");
        setSize(1100, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));
    }

    private void construirInterface() {

        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(new Color(33, 97, 140));

        JLabel titulo = new JLabel("Controle de Estoque");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        painelTitulo.add(titulo);

        add(painelTitulo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Descrição", "Quantidade", "Categoria"};

        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(25);
        tabela.setFont(new Font("Arial", Font.PLAIN, 13));
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(new Color(52, 152, 219));
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.setSelectionBackground(new Color(174, 214, 241));

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(new EmptyBorder(0, 10, 0, 10));

        add(scroll, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.setBackground(new Color(245, 245, 245));
        painelBotoes.setBorder(new EmptyBorder(5, 10, 10, 10));

        JButton btnCadastrar = criarBotao("Cadastrar", new Color(39, 174, 96));
        JButton btnEditar = criarBotao("Editar", new Color(41, 128, 185));
        JButton btnDeletar = criarBotao("Deletar", new Color(192, 57, 43));
        JButton btnAdicionar = criarBotao("+ Entrada", new Color(142, 68, 173));
        JButton btnRemover = criarBotao("- Saída", new Color(211, 84, 0));
        JButton btnAtualizar = criarBotao("Atualizar", new Color(127, 140, 141));
        JButton btnDoacao = criarBotao("Doação", new Color(52, 73, 94));

        btnCadastrar.addActionListener(e -> abrirFormCadastro());
        btnEditar.addActionListener(e -> abrirFormEdicao());
        btnDeletar.addActionListener(e -> deletarSelecionado());
        btnAdicionar.addActionListener(e -> movimentarEstoque(true));
        btnRemover.addActionListener(e -> movimentarEstoque(false));
        btnAtualizar.addActionListener(e -> carregarTabela());

        btnDoacao.addActionListener(e -> {
            DoacaoView tela = new DoacaoView();
            tela.setVisible(true);
        });

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnDoacao);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);

        List<Estoque> lista = service.listar();

        for (Estoque e : lista) {
            modeloTabela.addRow(new Object[]{
                    e.getId(),
                    e.getDescricao(),
                    e.getQuantidade(),
                    e.getCategoria()
            });
        }
    }

    private void abrirFormCadastro() {

        JDialog dialog = criarDialog("Cadastrar Item no Estoque");

        JTextField campoDescricao = new JTextField(20);
        JTextField campoQuantidade = new JTextField(20);
        JTextField campoCategoria = new JTextField(20);

        JPanel form = criarFormulario(
                new String[]{"Descrição:", "Quantidade:", "Categoria:"},
                new JTextField[]{campoDescricao, campoQuantidade, campoCategoria}
        );

        JButton btnSalvar = criarBotao("Salvar", new Color(39, 174, 96));

        btnSalvar.addActionListener(e -> {
            try {
                Estoque estoque = new Estoque();
                estoque.setDescricao(campoDescricao.getText().trim().toUpperCase());
                estoque.setQuantidade(Integer.parseInt(campoQuantidade.getText().trim()));
                estoque.setCategoria(campoCategoria.getText().trim().toUpperCase());

                boolean salvou = service.salvar(estoque);

                if (salvou) {
                    JOptionPane.showMessageDialog(dialog, "Cadastrado com sucesso!");
                    carregarTabela();
                    dialog.dispose();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Quantidade deve ser um número inteiro!");
            }
        });

        JPanel painelBtn = new JPanel();
        painelBtn.add(btnSalvar);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(painelBtn, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void abrirFormEdicao() {

        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item na tabela!");
            return;
        }

        Long id = (Long) modeloTabela.getValueAt(linhaSelecionada, 0);
        Estoque estoque = service.buscarPorId(id);

        if (estoque == null) {
            JOptionPane.showMessageDialog(this, "Item não encontrado!");
            return;
        }

        JDialog dialog = criarDialog("Editar Item do Estoque");

        JTextField campoDescricao = new JTextField(estoque.getDescricao(), 20);
        JTextField campoQuantidade = new JTextField(String.valueOf(estoque.getQuantidade()), 20);
        JTextField campoCategoria = new JTextField(estoque.getCategoria(), 20);

        JPanel form = criarFormulario(
                new String[]{"Descrição:", "Quantidade:", "Categoria:"},
                new JTextField[]{campoDescricao, campoQuantidade, campoCategoria}
        );

        JButton btnSalvar = criarBotao("Salvar", new Color(41, 128, 185));

        btnSalvar.addActionListener(e -> {
            try {
                estoque.setDescricao(campoDescricao.getText().trim().toUpperCase());
                estoque.setQuantidade(Integer.parseInt(campoQuantidade.getText().trim()));
                estoque.setCategoria(campoCategoria.getText().trim().toUpperCase());

                boolean atualizou = service.atualizar(estoque);

                if (atualizou) {
                    JOptionPane.showMessageDialog(dialog, "Atualizado com sucesso!");
                    carregarTabela();
                    dialog.dispose();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Quantidade deve ser um número inteiro!");
            }
        });

        JPanel painelBtn = new JPanel();
        painelBtn.add(btnSalvar);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(painelBtn, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deletarSelecionado() {

        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item na tabela!");
            return;
        }

        Long id = (Long) modeloTabela.getValueAt(linhaSelecionada, 0);
        String descricao = (String) modeloTabela.getValueAt(linhaSelecionada, 1);

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Confirma a exclusão do item abaixo?\n\n" +
                        "ID: " + id + "\n" +
                        "Descrição: " + descricao,
                "Confirmar exclusão",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (confirmacao == JOptionPane.OK_OPTION) {
            service.deletar(id);
            JOptionPane.showMessageDialog(this, "Deletado com sucesso!");
            carregarTabela();
        }
    }

    private void movimentarEstoque(boolean entrada) {

        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item na tabela!");
            return;
        }

        Long id = (Long) modeloTabela.getValueAt(linhaSelecionada, 0);
        String descricao = (String) modeloTabela.getValueAt(linhaSelecionada, 1);
        String tipo = entrada ? "Entrada" : "Saída";

        String input = JOptionPane.showInputDialog(
                this,
                tipo + " de estoque para: " + descricao + "\n\nQuantidade:"
        );

        if (input == null || input.isBlank()) {
            return;
        }

        try {
            int quantidade = Integer.parseInt(input.trim());
            boolean ok;

            if (entrada) {
                ok = service.adicionarQuantidade(id, quantidade);
            } else {
                ok = service.removerQuantidade(id, quantidade);
            }

            if (ok) {
                JOptionPane.showMessageDialog(this, tipo + " registrada com sucesso!");
                carregarTabela();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida!");
        }
    }

    private JDialog criarDialog(String titulo) {
        JDialog dialog = new JDialog(this, titulo, true);
        dialog.setSize(380, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(new Color(245, 245, 245));
        return dialog;
    }

    private JPanel criarFormulario(String[] labels, JTextField[] campos) {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(245, 245, 245));
        form.setBorder(new EmptyBorder(15, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0;

            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Arial", Font.BOLD, 13));
            form.add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            campos[i].setFont(new Font("Arial", Font.PLAIN, 13));
            form.add(campos[i], gbc);
        }

        return form;
    }
}
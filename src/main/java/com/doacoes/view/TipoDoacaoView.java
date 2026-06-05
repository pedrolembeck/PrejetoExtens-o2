package com.doacoes.view;

import com.doacoes.enums.CategoriaItem;
import com.doacoes.model.TipoDoacao;
import com.doacoes.service.TipoDoacaoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TipoDoacaoView extends JFrame {

    private final TipoDoacaoService service = new TipoDoacaoService();

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TipoDoacaoView() {
        configurarJanela();
        construirInterface();
        carregarTabela();
    }

    private void configurarJanela() {
        setTitle("Gerenciamento de Tipos de Doação");
        setSize(650, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));
    }

    private void construirInterface() {

        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(new Color(41, 128, 185));
        painelTitulo.setBorder(new EmptyBorder(12, 20, 12, 20));
        JLabel titulo = new JLabel("Tipos de Doação");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        painelTitulo.add(titulo);
        add(painelTitulo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome", "Categoria"};
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
        JButton btnEditar    = criarBotao("Editar", new Color(41, 128, 185));
        JButton btnDeletar   = criarBotao("Deletar", new Color(192, 57, 43));
        JButton btnAtualizar = criarBotao("Atualizar Lista", new Color(127, 140, 141));

        btnCadastrar.addActionListener(e -> abrirFormCadastro());
        btnEditar.addActionListener(e -> abrirFormEdicao());
        btnDeletar.addActionListener(e -> deletarSelecionado());
        btnAtualizar.addActionListener(e -> carregarTabela());

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnAtualizar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(130, 35));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<TipoDoacao> lista = service.listar();
        for (TipoDoacao t : lista) {
            modeloTabela.addRow(new Object[]{
                    t.getId(),
                    t.getNome(),
                    t.getCategoria()
            });
        }
    }

    private CategoriaItem selecionarCategoria(Component parent) {
        String[] opcoes = {"VESTUARIO", "ALIMENTOS"};
        String escolha = (String) JOptionPane.showInputDialog(
                parent,
                "Selecione a categoria:",
                "Categoria",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );
        if (escolha == null) return null;
        return CategoriaItem.valueOf(escolha);
    }

    private void abrirFormCadastro() {

        JDialog dialog = criarDialog("Cadastrar Tipo de Doação");

        JTextField campoNome = new JTextField(20);

        JPanel form = criarFormulario(
                new String[]{"Nome:"},
                new JTextField[]{campoNome}
        );

        JButton btnSalvar = criarBotao("Salvar", new Color(39, 174, 96));

        btnSalvar.addActionListener(e -> {
            CategoriaItem categoria = selecionarCategoria(dialog);
            if (categoria == null) return;

            TipoDoacao tipo = new TipoDoacao();
            tipo.setNome(campoNome.getText().trim().toUpperCase());
            tipo.setCategoria(categoria);

            boolean salvou = service.salvar(tipo);

            if (salvou) {
                JOptionPane.showMessageDialog(dialog, "Cadastrado com sucesso!");
                carregarTabela();
                dialog.dispose();
            }
        });

        JPanel painelBtn = new JPanel();
        painelBtn.add(btnSalvar);
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(painelBtn, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void abrirFormEdicao() {

        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item na tabela!");
            return;
        }

        Long id = (Long) modeloTabela.getValueAt(linha, 0);
        TipoDoacao tipo = service.buscarPorId(id);

        if (tipo == null) {
            JOptionPane.showMessageDialog(this, "Tipo não encontrado!");
            return;
        }

        JDialog dialog = criarDialog("Editar Tipo de Doação");

        JTextField campoNome = new JTextField(tipo.getNome(), 20);

        JPanel form = criarFormulario(
                new String[]{"Nome:"},
                new JTextField[]{campoNome}
        );

        JButton btnSalvar = criarBotao("Salvar", new Color(41, 128, 185));

        btnSalvar.addActionListener(e -> {
            CategoriaItem categoria = selecionarCategoria(dialog);
            if (categoria == null) return;

            tipo.setNome(campoNome.getText().trim().toUpperCase());
            tipo.setCategoria(categoria);

            boolean atualizou = service.atualizar(tipo);

            if (atualizou) {
                JOptionPane.showMessageDialog(dialog, "Atualizado com sucesso!");
                carregarTabela();
                dialog.dispose();
            }
        });

        JPanel painelBtn = new JPanel();
        painelBtn.add(btnSalvar);
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(painelBtn, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deletarSelecionado() {

        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item na tabela!");
            return;
        }

        Long id   = (Long) modeloTabela.getValueAt(linha, 0);
        String nome = (String) modeloTabela.getValueAt(linha, 1);

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Confirma a exclusão do item abaixo?\n\n" +
                        "ID: " + id + "\nNome: " + nome,
                "Confirmar exclusão",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (confirmacao == JOptionPane.OK_OPTION) {
            service.deletar(id);
            JOptionPane.showMessageDialog(this, "Deletado com sucesso!");
            carregarTabela();
        }
    }

    private JDialog criarDialog(String titulo) {
        JDialog dialog = new JDialog(this, titulo, true);
        dialog.setSize(350, 200);
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
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Arial", Font.BOLD, 13));
            form.add(label, gbc);

            gbc.gridx = 1; gbc.weightx = 1;
            campos[i].setFont(new Font("Arial", Font.PLAIN, 13));
            form.add(campos[i], gbc);
        }

        return form;
    }
}

package com.doacoes.view;

import com.doacoes.model.TamanhoRoupa;
import com.doacoes.service.TamanhoRoupaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TamanhoRoupaView extends JFrame {

    private final TamanhoRoupaService service = new TamanhoRoupaService();

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private static final String[] TIPOS = {
            "BEBE", "CRIANCA", "ADULTO", "CALCADO"
    };

    public TamanhoRoupaView() {
        service.inicializar();
        configurarJanela();
        construirInterface();
        carregarTabela();
    }

    private void configurarJanela() {
        setTitle("Gerenciamento de Tamanhos");
        setSize(650, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));
    }

    private void construirInterface() {
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(new Color(52, 73, 94));
        painelTitulo.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel titulo = new JLabel("Tamanhos de Roupas");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);

        painelTitulo.add(titulo);
        add(painelTitulo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Tamanho", "Tipo"};

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

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(new EmptyBorder(0, 10, 0, 10));
        add(scroll, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnEditar = new JButton("Editar");
        JButton btnDeletar = new JButton("Deletar");
        JButton btnAtualizar = new JButton("Atualizar Lista");

        btnCadastrar.addActionListener(e -> cadastrar());
        btnEditar.addActionListener(e -> editar());
        btnDeletar.addActionListener(e -> deletar());
        btnAtualizar.addActionListener(e -> carregarTabela());

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnAtualizar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);

        List<TamanhoRoupa> tamanhos = service.listar();

        for (TamanhoRoupa tamanho : tamanhos) {
            modeloTabela.addRow(new Object[]{
                    tamanho.getId(),
                    tamanho.getTamanho(),
                    tamanho.getTipo()
            });
        }
    }

    private void cadastrar() {
        TamanhoRoupa tamanho = abrirFormulario(null);

        if (tamanho == null) {
            return;
        }

        boolean salvou = service.salvar(tamanho);

        if (salvou) {
            JOptionPane.showMessageDialog(this, "Tamanho cadastrado com sucesso!");
            carregarTabela();
        }
    }

    private void editar() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um tamanho na tabela!");
            return;
        }

        Long id = Long.valueOf(modeloTabela.getValueAt(linha, 0).toString());

        TamanhoRoupa tamanhoExistente = service.buscarPorId(id);

        if (tamanhoExistente == null) {
            JOptionPane.showMessageDialog(this, "Tamanho não encontrado!");
            return;
        }

        TamanhoRoupa tamanhoAlterado = abrirFormulario(tamanhoExistente);

        if (tamanhoAlterado == null) {
            return;
        }

        boolean atualizou = service.atualizar(tamanhoAlterado);

        if (atualizou) {
            JOptionPane.showMessageDialog(this, "Tamanho atualizado com sucesso!");
            carregarTabela();
        }
    }

    private void deletar() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um tamanho na tabela!");
            return;
        }

        Long id = Long.valueOf(modeloTabela.getValueAt(linha, 0).toString());
        String tamanho = modeloTabela.getValueAt(linha, 1).toString();
        String tipo = modeloTabela.getValueAt(linha, 2).toString();

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja deletar o tamanho abaixo?\n\n" +
                        "Tamanho: " + tamanho + "\n" +
                        "Tipo: " + tipo,
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            service.deletar(id);
            JOptionPane.showMessageDialog(this, "Tamanho deletado com sucesso!");
            carregarTabela();
        }
    }

    private TamanhoRoupa abrirFormulario(TamanhoRoupa tamanhoRoupa) {
        JTextField campoTamanho = new JTextField(15);
        JComboBox<String> campoTipo = new JComboBox<>(TIPOS);

        if (tamanhoRoupa != null) {
            campoTamanho.setText(tamanhoRoupa.getTamanho());
            campoTipo.setSelectedItem(tamanhoRoupa.getTipo());
        }

        JPanel painel = new JPanel(new GridLayout(0, 2, 8, 8));

        painel.add(new JLabel("Tamanho:"));
        painel.add(campoTamanho);

        painel.add(new JLabel("Tipo:"));
        painel.add(campoTipo);

        int resultado = JOptionPane.showConfirmDialog(
                this,
                painel,
                tamanhoRoupa == null ? "Cadastrar Tamanho" : "Editar Tamanho",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return null;
        }

        TamanhoRoupa novoTamanho = new TamanhoRoupa();

        if (tamanhoRoupa != null) {
            novoTamanho.setId(tamanhoRoupa.getId());
        }

        novoTamanho.setTamanho(campoTamanho.getText());
        novoTamanho.setTipo(campoTipo.getSelectedItem().toString());

        return novoTamanho;
    }
}
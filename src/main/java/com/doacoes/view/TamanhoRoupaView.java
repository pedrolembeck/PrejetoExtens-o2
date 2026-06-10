package com.doacoes.view;

import com.doacoes.model.TamanhoRoupa;
import com.doacoes.service.TamanhoRoupaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class TamanhoRoupaView extends JFrame {

    private final TamanhoRoupaService service = new TamanhoRoupaService();

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TamanhoRoupaView() {
        service.inicializar();
        configurarJanela();
        construirInterface();
        carregarTabela();
    }

    private void configurarJanela() {
        setTitle("Solidário - Tamanhos");
        setSize(820, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void construirInterface() {
        JPanel fundo = new JPanel(new BorderLayout());
        fundo.setBackground(new Color(241, 245, 249));
        setContentPane(fundo);

        fundo.add(criarCabecalho(), BorderLayout.NORTH);
        fundo.add(criarConteudo(), BorderLayout.CENTER);
        fundo.add(criarRodape(), BorderLayout.SOUTH);
    }

    private JPanel criarCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(new Color(22, 160, 133));
        cabecalho.setBorder(new EmptyBorder(24, 35, 24, 35));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Tamanhos de Roupas");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Cadastre e organize os tamanhos utilizados nas doações de vestuário.");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(235, 245, 245));

        textos.add(titulo);
        textos.add(Box.createVerticalStrut(5));
        textos.add(subtitulo);

        JLabel modulo = new JLabel("SOLIDÁRIO");
        modulo.setFont(new Font("Arial", Font.BOLD, 15));
        modulo.setForeground(Color.WHITE);
        modulo.setHorizontalAlignment(SwingConstants.RIGHT);

        cabecalho.add(textos, BorderLayout.WEST);
        cabecalho.add(modulo, BorderLayout.EAST);

        return cabecalho;
    }

    private JPanel criarConteudo() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(241, 245, 249));
        painel.setBorder(new EmptyBorder(25, 35, 20, 35));

        JPanel card = new RoundedPanel(Color.WHITE, 20);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topoCard = new JPanel(new BorderLayout());
        topoCard.setOpaque(false);
        topoCard.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel tituloLista = new JLabel("Tamanhos cadastrados");
        tituloLista.setFont(new Font("Arial", Font.BOLD, 18));
        tituloLista.setForeground(new Color(44, 62, 80));

        JLabel descricaoLista = new JLabel("Visualize, edite ou remova os tamanhos cadastrados.");
        descricaoLista.setFont(new Font("Arial", Font.PLAIN, 13));
        descricaoLista.setForeground(new Color(100, 116, 139));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(tituloLista);
        textos.add(Box.createVerticalStrut(4));
        textos.add(descricaoLista);

        topoCard.add(textos, BorderLayout.WEST);

        card.add(topoCard, BorderLayout.NORTH);
        card.add(criarTabela(), BorderLayout.CENTER);

        painel.add(card, BorderLayout.CENTER);

        return painel;
    }

    private JScrollPane criarTabela() {
        String[] colunas = {"ID", "Tamanho", "Tipo"};

        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(34);
        tabela.setFont(new Font("Arial", Font.PLAIN, 13));
        tabela.setForeground(new Color(44, 62, 80));
        tabela.setSelectionBackground(new Color(209, 242, 235));
        tabela.setSelectionForeground(new Color(44, 62, 80));
        tabela.setGridColor(new Color(230, 235, 240));
        tabela.setShowVerticalLines(false);
        tabela.setFillsViewportHeight(true);
        tabela.setAutoCreateRowSorter(true);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(350);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(220);

        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(new Color(44, 62, 80));
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.getTableHeader().setPreferredSize(new Dimension(0, 36));
        tabela.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);

        tabela.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(1).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(2).setCellRenderer(centralizado);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        scroll.getViewport().setBackground(Color.WHITE);

        return scroll;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 14));
        rodape.setBackground(Color.WHITE);
        rodape.setBorder(new EmptyBorder(0, 35, 0, 35));

        JButton btnCadastrar = criarBotao("Cadastrar", new Color(39, 174, 96));
        JButton btnEditar = criarBotao("Editar", new Color(41, 128, 185));
        JButton btnDeletar = criarBotao("Deletar", new Color(192, 57, 43));
        JButton btnAtualizar = criarBotao("Atualizar", new Color(127, 140, 141));

        btnCadastrar.addActionListener(e -> abrirFormCadastro());
        btnEditar.addActionListener(e -> abrirFormEdicao());
        btnDeletar.addActionListener(e -> deletarSelecionado());
        btnAtualizar.addActionListener(e -> carregarTabela());

        rodape.add(btnCadastrar);
        rodape.add(btnEditar);
        rodape.add(btnDeletar);
        rodape.add(btnAtualizar);

        return rodape;
    }

    private JButton criarBotao(String texto, Color cor) {
        RoundedButton btn = new RoundedButton(texto, cor);
        btn.setPreferredSize(new Dimension(150, 36));
        return btn;
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);

        List<TamanhoRoupa> lista = service.listar();

        for (TamanhoRoupa tamanho : lista) {
            modeloTabela.addRow(new Object[]{
                    tamanho.getId(),
                    tamanho.getTamanho(),
                    tamanho.getTipo()
            });
        }
    }

    private void abrirFormCadastro() {
        abrirFormulario(null);
    }

    private void abrirFormEdicao() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um tamanho na tabela.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linha);
        Long id = Long.valueOf(modeloTabela.getValueAt(linhaModelo, 0).toString());

        TamanhoRoupa tamanho = service.buscarPorId(id);

        if (tamanho == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Tamanho não encontrado.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        abrirFormulario(tamanho);
    }

    private void abrirFormulario(TamanhoRoupa tamanhoEdicao) {
        boolean editando = tamanhoEdicao != null;

        JDialog dialog = new JDialog(
                this,
                editando ? "Editar Tamanho" : "Cadastrar Tamanho",
                true
        );

        dialog.setSize(430, 330);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(241, 245, 249));

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(editando ? new Color(41, 128, 185) : new Color(39, 174, 96));
        cabecalho.setBorder(new EmptyBorder(18, 25, 18, 25));

        JLabel titulo = new JLabel(editando ? "Editar Tamanho" : "Novo Tamanho");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);

        cabecalho.add(titulo, BorderLayout.WEST);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(25, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        JLabel lblTamanho = new JLabel("Tamanho");
        lblTamanho.setFont(new Font("Arial", Font.BOLD, 13));
        lblTamanho.setForeground(new Color(52, 73, 94));

        JTextField campoTamanho = new JTextField();
        campoTamanho.setText(editando ? tamanhoEdicao.getTamanho() : "");
        estilizarCampo(campoTamanho);

        JLabel lblTipo = new JLabel("Tipo");
        lblTipo.setFont(new Font("Arial", Font.BOLD, 13));
        lblTipo.setForeground(new Color(52, 73, 94));

        JComboBox<String> campoTipo = new JComboBox<>(new String[]{
                "BEBE",
                "CRIANCA",
                "ADULTO",
                "CALCADO"
        });
        campoTipo.setFont(new Font("Arial", Font.PLAIN, 13));
        campoTipo.setPreferredSize(new Dimension(260, 38));
        campoTipo.setBackground(Color.WHITE);

        if (editando && tamanhoEdicao.getTipo() != null) {
            campoTipo.setSelectedItem(tamanhoEdicao.getTipo());
        }

        gbc.gridy = 0;
        form.add(lblTamanho, gbc);

        gbc.gridy = 1;
        form.add(campoTamanho, gbc);

        gbc.gridy = 2;
        form.add(lblTipo, gbc);

        gbc.gridy = 3;
        form.add(campoTipo, gbc);

        JPanel rodapeDialog = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        rodapeDialog.setBackground(Color.WHITE);
        rodapeDialog.setBorder(new EmptyBorder(0, 20, 10, 20));

        JButton btnCancelar = criarBotaoDialog("Cancelar", new Color(127, 140, 141));
        JButton btnSalvar = criarBotaoDialog(
                editando ? "Salvar Alterações" : "Cadastrar",
                editando ? new Color(41, 128, 185) : new Color(39, 174, 96)
        );

        btnCancelar.addActionListener(e -> dialog.dispose());

        btnSalvar.addActionListener(e -> {
            String tamanhoTexto = campoTamanho.getText().trim().toUpperCase();
            String tipo = campoTipo.getSelectedItem().toString();

            if (tamanhoTexto.isEmpty()) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Informe o tamanho.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );
                campoTamanho.requestFocus();
                return;
            }

            boolean sucesso;

            if (editando) {
                tamanhoEdicao.setTamanho(tamanhoTexto);
                tamanhoEdicao.setTipo(tipo);
                sucesso = service.atualizar(tamanhoEdicao);
            } else {
                TamanhoRoupa novoTamanho = new TamanhoRoupa();
                novoTamanho.setTamanho(tamanhoTexto);
                novoTamanho.setTipo(tipo);
                sucesso = service.salvar(novoTamanho);
            }

            if (sucesso) {
                JOptionPane.showMessageDialog(
                        dialog,
                        editando
                                ? "Tamanho atualizado com sucesso!"
                                : "Tamanho cadastrado com sucesso!"
                );

                carregarTabela();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Não foi possível salvar o tamanho.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        rodapeDialog.add(btnCancelar);
        rodapeDialog.add(btnSalvar);

        dialog.add(cabecalho, BorderLayout.NORTH);
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(rodapeDialog, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(() -> campoTamanho.requestFocus());

        dialog.setVisible(true);
    }

    private JButton criarBotaoDialog(String texto, Color cor) {
        RoundedButton btn = new RoundedButton(texto, cor);
        btn.setPreferredSize(new Dimension(145, 36));
        return btn;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(260, 38));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                new EmptyBorder(8, 12, 8, 12)
        ));
        campo.setBackground(new Color(250, 250, 250));
        campo.setForeground(new Color(44, 62, 80));
    }

    private void deletarSelecionado() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um tamanho na tabela.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linha);

        Long id = Long.valueOf(modeloTabela.getValueAt(linhaModelo, 0).toString());
        String tamanho = modeloTabela.getValueAt(linhaModelo, 1).toString();
        Object tipo = modeloTabela.getValueAt(linhaModelo, 2);

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Confirma a exclusão do tamanho abaixo?\n\n" +
                        "ID: " + id + "\n" +
                        "Tamanho: " + tamanho + "\n" +
                        "Tipo: " + tipo,
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            service.deletar(id);

            JOptionPane.showMessageDialog(
                    this,
                    "Tamanho deletado com sucesso!"
            );

            carregarTabela();
        }
    }

    private static class RoundedPanel extends JPanel {

        private final Color backgroundColor;
        private final int radius;

        public RoundedPanel(Color backgroundColor, int radius) {
            this.backgroundColor = backgroundColor;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(backgroundColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));

            g2.dispose();

            super.paintComponent(g);
        }
    }

    private static class RoundedButton extends JButton {

        private final Color backgroundColor;

        public RoundedButton(String text, Color backgroundColor) {
            super(text);
            this.backgroundColor = backgroundColor;

            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 12));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(9, 14, 9, 14));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2.setColor(backgroundColor.darker());
            } else if (getModel().isRollover()) {
                g2.setColor(backgroundColor.brighter());
            } else {
                g2.setColor(backgroundColor);
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            g2.dispose();

            super.paintComponent(g);
        }
    }
}
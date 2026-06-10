package com.doacoes.view;

import com.doacoes.model.Estoque;
import com.doacoes.service.EstoqueService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
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
        setTitle("Solidário - Estoque");
        setSize(1100, 620);
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

        JLabel titulo = new JLabel("Controle de Estoque");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Acompanhe, cadastre e movimente os itens disponíveis para doação.");
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

        JLabel tituloLista = new JLabel("Itens em estoque");
        tituloLista.setFont(new Font("Arial", Font.BOLD, 18));
        tituloLista.setForeground(new Color(44, 62, 80));

        JLabel descricaoLista = new JLabel("Visualize os itens disponíveis e registre entradas ou saídas de estoque.");
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
        String[] colunas = {"ID", "Descrição", "Quantidade", "Categoria"};

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
        tabela.getColumnModel().getColumn(1).setPreferredWidth(520);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(160);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(220);

        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(new Color(44, 62, 80));
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.getTableHeader().setPreferredSize(new Dimension(0, 36));
        tabela.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);

        tabela.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(2).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(3).setCellRenderer(centralizado);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        scroll.getViewport().setBackground(Color.WHITE);

        return scroll;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 14));
        rodape.setBackground(Color.WHITE);
        rodape.setBorder(new EmptyBorder(0, 25, 0, 25));

        JButton btnCadastrar = criarBotao("Cadastrar", new Color(39, 174, 96));
        JButton btnEditar = criarBotao("Editar", new Color(41, 128, 185));
        JButton btnDeletar = criarBotao("Deletar", new Color(192, 57, 43));
        JButton btnAdicionar = criarBotao("+ Entrada", new Color(142, 68, 173));
        JButton btnRemover = criarBotao("- Saída", new Color(211, 84, 0));
        JButton btnAtualizar = criarBotao("Atualizar", new Color(127, 140, 141));
        JButton btnRelatorio = criarBotao("Relatório CSV", new Color(52, 73, 94));

        btnCadastrar.addActionListener(e -> abrirFormCadastro());
        btnEditar.addActionListener(e -> abrirFormEdicao());
        btnDeletar.addActionListener(e -> deletarSelecionado());
        btnAdicionar.addActionListener(e -> movimentarEstoque(true));
        btnRemover.addActionListener(e -> movimentarEstoque(false));
        btnAtualizar.addActionListener(e -> carregarTabela());
        btnRelatorio.addActionListener(e -> exportarRelatorioCSV());

        rodape.add(btnCadastrar);
        rodape.add(btnEditar);
        rodape.add(btnDeletar);
        rodape.add(btnAdicionar);
        rodape.add(btnRemover);
        rodape.add(btnAtualizar);
        rodape.add(btnRelatorio);

        return rodape;
    }

    private JButton criarBotao(String texto, Color cor) {
        RoundedButton btn = new RoundedButton(texto, cor);
        btn.setPreferredSize(new Dimension(135, 36));
        return btn;
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);

        List<Estoque> lista = service.listar();

        for (Estoque estoque : lista) {
            modeloTabela.addRow(new Object[]{
                    estoque.getId(),
                    estoque.getDescricao(),
                    estoque.getQuantidade(),
                    estoque.getCategoria()
            });
        }
    }

    private void abrirFormCadastro() {
        abrirFormulario(null);
    }

    private void abrirFormEdicao() {
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um item na tabela.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linhaSelecionada);
        Long id = Long.valueOf(modeloTabela.getValueAt(linhaModelo, 0).toString());

        Estoque estoque = service.buscarPorId(id);

        if (estoque == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Item não encontrado.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        abrirFormulario(estoque);
    }

    private void abrirFormulario(Estoque estoqueEdicao) {
        boolean editando = estoqueEdicao != null;

        JDialog dialog = new JDialog(
                this,
                editando ? "Editar Item do Estoque" : "Cadastrar Item no Estoque",
                true
        );

        dialog.setSize(500, 440);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(241, 245, 249));

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(editando ? new Color(41, 128, 185) : new Color(39, 174, 96));
        cabecalho.setBorder(new EmptyBorder(18, 25, 18, 25));

        JLabel titulo = new JLabel(editando ? "Editar Item do Estoque" : "Novo Item no Estoque");
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

        JTextField campoDescricao = new JTextField();
        estilizarCampo(campoDescricao);

        JTextField campoQuantidade = new JTextField();
        estilizarCampo(campoQuantidade);

        JTextField campoCategoria = new JTextField();
        estilizarCampo(campoCategoria);

        if (editando) {
            campoDescricao.setText(estoqueEdicao.getDescricao());
            campoQuantidade.setText(String.valueOf(estoqueEdicao.getQuantidade()));
            campoCategoria.setText(estoqueEdicao.getCategoria());
        }

        adicionarCampo(form, gbc, 0, "Descrição", campoDescricao);
        adicionarCampo(form, gbc, 2, "Quantidade", campoQuantidade);
        adicionarCampo(form, gbc, 4, "Categoria", campoCategoria);

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
            try {
                String descricao = campoDescricao.getText().trim().toUpperCase();
                String categoria = campoCategoria.getText().trim().toUpperCase();

                if (descricao.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Informe a descrição do item.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    campoDescricao.requestFocus();
                    return;
                }

                int quantidade = Integer.parseInt(campoQuantidade.getText().trim());

                if (quantidade < 0) {
                    JOptionPane.showMessageDialog(dialog, "A quantidade não pode ser negativa.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    campoQuantidade.requestFocus();
                    return;
                }

                if (categoria.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Informe a categoria do item.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    campoCategoria.requestFocus();
                    return;
                }

                boolean sucesso;

                if (editando) {
                    estoqueEdicao.setDescricao(descricao);
                    estoqueEdicao.setQuantidade(quantidade);
                    estoqueEdicao.setCategoria(categoria);

                    sucesso = service.atualizar(estoqueEdicao);
                } else {
                    Estoque novoEstoque = new Estoque();
                    novoEstoque.setDescricao(descricao);
                    novoEstoque.setQuantidade(quantidade);
                    novoEstoque.setCategoria(categoria);

                    sucesso = service.salvar(novoEstoque);
                }

                if (sucesso) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            editando ? "Item atualizado com sucesso!" : "Item cadastrado com sucesso!"
                    );

                    carregarTabela();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Não foi possível salvar o item.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Quantidade deve ser um número inteiro.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );
                campoQuantidade.requestFocus();
            }
        });

        rodapeDialog.add(btnCancelar);
        rodapeDialog.add(btnSalvar);

        dialog.add(cabecalho, BorderLayout.NORTH);
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(rodapeDialog, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(() -> campoDescricao.requestFocus());

        dialog.setVisible(true);
    }

    private void deletarSelecionado() {
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um item na tabela.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linhaSelecionada);

        Long id = Long.valueOf(modeloTabela.getValueAt(linhaModelo, 0).toString());
        String descricao = String.valueOf(modeloTabela.getValueAt(linhaModelo, 1));
        Object quantidade = modeloTabela.getValueAt(linhaModelo, 2);
        Object categoria = modeloTabela.getValueAt(linhaModelo, 3);

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Confirma a exclusão do item abaixo?\n\n" +
                        "ID: " + id + "\n" +
                        "Descrição: " + descricao + "\n" +
                        "Quantidade: " + quantidade + "\n" +
                        "Categoria: " + categoria,
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            service.deletar(id);

            JOptionPane.showMessageDialog(this, "Item deletado com sucesso!");
            carregarTabela();
        }
    }

    private void movimentarEstoque(boolean entrada) {
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um item na tabela.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linhaSelecionada);

        Long id = Long.valueOf(modeloTabela.getValueAt(linhaModelo, 0).toString());
        String descricao = String.valueOf(modeloTabela.getValueAt(linhaModelo, 1));
        Object quantidadeAtual = modeloTabela.getValueAt(linhaModelo, 2);

        abrirDialogMovimento(id, descricao, quantidadeAtual, entrada);
    }

    private void abrirDialogMovimento(Long id, String descricao, Object quantidadeAtual, boolean entrada) {
        String tipo = entrada ? "Entrada de Estoque" : "Saída de Estoque";

        JDialog dialog = new JDialog(this, tipo, true);
        dialog.setSize(460, 310);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(241, 245, 249));

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(entrada ? new Color(142, 68, 173) : new Color(211, 84, 0));
        cabecalho.setBorder(new EmptyBorder(18, 25, 18, 25));

        JLabel titulo = new JLabel(tipo);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);

        cabecalho.add(titulo, BorderLayout.WEST);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(25, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 0, 7, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        JLabel infoItem = new JLabel("<html><b>Item:</b> " + descricao + "<br><b>Quantidade atual:</b> " + quantidadeAtual + "</html>");
        infoItem.setFont(new Font("Arial", Font.PLAIN, 13));
        infoItem.setForeground(new Color(44, 62, 80));

        JLabel lblQuantidade = new JLabel("Quantidade");
        lblQuantidade.setFont(new Font("Arial", Font.BOLD, 13));
        lblQuantidade.setForeground(new Color(52, 73, 94));

        JTextField campoQuantidade = new JTextField();
        estilizarCampo(campoQuantidade);

        gbc.gridy = 0;
        form.add(infoItem, gbc);

        gbc.gridy = 1;
        form.add(Box.createVerticalStrut(8), gbc);

        gbc.gridy = 2;
        form.add(lblQuantidade, gbc);

        gbc.gridy = 3;
        form.add(campoQuantidade, gbc);

        JPanel rodapeDialog = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        rodapeDialog.setBackground(Color.WHITE);
        rodapeDialog.setBorder(new EmptyBorder(0, 20, 10, 20));

        JButton btnCancelar = criarBotaoDialog("Cancelar", new Color(127, 140, 141));
        JButton btnConfirmar = criarBotaoDialog(
                entrada ? "Confirmar Entrada" : "Confirmar Saída",
                entrada ? new Color(142, 68, 173) : new Color(211, 84, 0)
        );

        btnCancelar.addActionListener(e -> dialog.dispose());

        btnConfirmar.addActionListener(e -> {
            try {
                int quantidade = Integer.parseInt(campoQuantidade.getText().trim());

                if (quantidade <= 0) {
                    JOptionPane.showMessageDialog(dialog, "A quantidade deve ser maior que zero.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    campoQuantidade.requestFocus();
                    return;
                }

                boolean ok;

                if (entrada) {
                    ok = service.adicionarQuantidade(id, quantidade);
                } else {
                    ok = service.removerQuantidade(id, quantidade);
                }

                if (ok) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            entrada ? "Entrada registrada com sucesso!" : "Saída registrada com sucesso!"
                    );

                    carregarTabela();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Não foi possível registrar a movimentação.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Quantidade inválida.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );
                campoQuantidade.requestFocus();
            }
        });

        rodapeDialog.add(btnCancelar);
        rodapeDialog.add(btnConfirmar);

        dialog.add(cabecalho, BorderLayout.NORTH);
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(rodapeDialog, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(() -> campoQuantidade.requestFocus());

        dialog.setVisible(true);
    }

    private void adicionarCampo(
            JPanel form,
            GridBagConstraints gbc,
            int linha,
            String label,
            JComponent campo
    ) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(new Color(52, 73, 94));

        gbc.gridy = linha;
        form.add(lbl, gbc);

        gbc.gridy = linha + 1;
        form.add(campo, gbc);
    }

    private JButton criarBotaoDialog(String texto, Color cor) {
        RoundedButton btn = new RoundedButton(texto, cor);
        btn.setPreferredSize(new Dimension(155, 36));
        return btn;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(360, 38));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                new EmptyBorder(8, 12, 8, 12)
        ));
        campo.setBackground(new Color(250, 250, 250));
        campo.setForeground(new Color(44, 62, 80));
    }

    private void exportarRelatorioCSV() {
        if (modeloTabela.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Não há dados para exportar.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar relatório de estoque");
        fileChooser.setSelectedFile(new File("relatorio_estoque.csv"));

        int opcao = fileChooser.showSaveDialog(this);

        if (opcao != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File arquivo = fileChooser.getSelectedFile();

        if (!arquivo.getName().toLowerCase().endsWith(".csv")) {
            arquivo = new File(arquivo.getAbsolutePath() + ".csv");
        }

        try (
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(arquivo), StandardCharsets.UTF_8)
                )
        ) {
            for (int coluna = 0; coluna < modeloTabela.getColumnCount(); coluna++) {
                writer.write(modeloTabela.getColumnName(coluna));

                if (coluna < modeloTabela.getColumnCount() - 1) {
                    writer.write(";");
                }
            }

            writer.newLine();

            for (int linha = 0; linha < modeloTabela.getRowCount(); linha++) {
                for (int coluna = 0; coluna < modeloTabela.getColumnCount(); coluna++) {
                    Object valor = modeloTabela.getValueAt(linha, coluna);

                    String texto = valor == null ? "" : valor.toString();
                    texto = texto.replace(";", ",")
                            .replace("\n", " ")
                            .replace("\r", " ");

                    writer.write(texto);

                    if (coluna < modeloTabela.getColumnCount() - 1) {
                        writer.write(";");
                    }
                }

                writer.newLine();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Relatório de estoque gerado com sucesso:\n" + arquivo.getAbsolutePath()
            );

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao gerar relatório de estoque:\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
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
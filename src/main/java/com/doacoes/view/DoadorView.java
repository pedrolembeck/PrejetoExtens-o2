package com.doacoes.view;

import com.doacoes.model.Doador;
import com.doacoes.service.DoadorService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.ParseException;
import java.util.List;

public class DoadorView extends JFrame {

    private final DoadorService service = new DoadorService();

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public DoadorView() {
        service.inicializar();
        configurarJanela();
        construirInterface();
        carregarTabela();
    }

    private void configurarJanela() {
        setTitle("Solidário - Doadores");
        setSize(900, 580);
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
        cabecalho.setBackground(new Color(142, 68, 173));
        cabecalho.setBorder(new EmptyBorder(24, 35, 24, 35));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Doadores");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Cadastre, edite e gerencie os doadores do sistema.");
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

        JLabel tituloLista = new JLabel("Doadores cadastrados");
        tituloLista.setFont(new Font("Arial", Font.BOLD, 18));
        tituloLista.setForeground(new Color(44, 62, 80));

        JLabel descricaoLista = new JLabel("Visualize, edite ou remova os doadores cadastrados no sistema.");
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
        String[] colunas = {"ID", "Nome", "CPF", "Telefone", "Email", "Endereço"};

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
        tabela.setSelectionBackground(new Color(225, 209, 242));
        tabela.setSelectionForeground(new Color(44, 62, 80));
        tabela.setGridColor(new Color(230, 235, 240));
        tabela.setShowVerticalLines(false);
        tabela.setFillsViewportHeight(true);
        tabela.setAutoCreateRowSorter(true);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(180);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(200);

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
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 14));
        rodape.setBackground(Color.WHITE);
        rodape.setBorder(new EmptyBorder(0, 35, 0, 35));

        JButton btnCadastrar = criarBotao("Cadastrar", new Color(39, 174, 96));
        JButton btnEditar = criarBotao("Editar", new Color(41, 128, 185));
        JButton btnDeletar = criarBotao("Deletar", new Color(192, 57, 43));
        JButton btnAtualizar = criarBotao("Atualizar", new Color(127, 140, 141));

        btnCadastrar.addActionListener(e -> cadastrar());
        btnEditar.addActionListener(e -> editar());
        btnDeletar.addActionListener(e -> deletar());
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

        List<Doador> doadores = service.listar();

        for (Doador doador : doadores) {
            modeloTabela.addRow(new Object[]{
                    doador.getId(),
                    doador.getNome(),
                    doador.getCpf(),
                    doador.getTelefone(),
                    doador.getEmail(),
                    doador.getEndereco()
            });
        }
    }

    private void cadastrar() {
        Doador doador = abrirFormulario(null);

        if (doador != null) {
            boolean salvou = service.salvar(doador);

            if (salvou) {
                JOptionPane.showMessageDialog(this, "Doador cadastrado com sucesso!");
                carregarTabela();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível cadastrar o doador.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void editar() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um doador na tabela.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linha);
        Long id = Long.valueOf(modeloTabela.getValueAt(linhaModelo, 0).toString());

        Doador doadorExistente = service.buscarPorId(id);

        if (doadorExistente == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Doador não encontrado.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Doador doadorAlterado = abrirFormulario(doadorExistente);

        if (doadorAlterado != null) {
            boolean atualizou = service.atualizar(doadorAlterado);

            if (atualizou) {
                JOptionPane.showMessageDialog(this, "Doador atualizado com sucesso!");
                carregarTabela();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível atualizar o doador.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void deletar() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um doador na tabela.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linha);

        Long id = Long.valueOf(modeloTabela.getValueAt(linhaModelo, 0).toString());
        String nome = modeloTabela.getValueAt(linhaModelo, 1).toString();
        String cpf = modeloTabela.getValueAt(linhaModelo, 2) != null
                ? modeloTabela.getValueAt(linhaModelo, 2).toString()
                : "-";

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Confirma a exclusão do doador abaixo?\n\n" +
                        "ID: " + id + "\n" +
                        "Nome: " + nome + "\n" +
                        "CPF: " + cpf,
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            service.deletar(id);

            JOptionPane.showMessageDialog(this, "Doador deletado com sucesso!");

            carregarTabela();
        }
    }

    private Doador abrirFormulario(Doador doador) {
        boolean editando = doador != null;

        final Doador[] resultado = new Doador[1];

        JDialog dialog = new JDialog(
                this,
                editando ? "Editar Doador" : "Cadastrar Doador",
                true
        );

        dialog.setSize(500, 560);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(241, 245, 249));

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(editando ? new Color(41, 128, 185) : new Color(39, 174, 96));
        cabecalho.setBorder(new EmptyBorder(18, 25, 18, 25));

        JLabel titulo = new JLabel(editando ? "Editar Doador" : "Novo Doador");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);

        cabecalho.add(titulo, BorderLayout.WEST);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(22, 30, 18, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        JTextField campoNome = new JTextField();
        estilizarCampo(campoNome);

        JFormattedTextField campoCpf = criarCampoCpf();

        JTextField campoTelefone = new JTextField();
        estilizarCampo(campoTelefone);

        JTextField campoEmail = new JTextField();
        estilizarCampo(campoEmail);

        JTextField campoEndereco = new JTextField();
        estilizarCampo(campoEndereco);

        JTextField campoObservacao = new JTextField();
        estilizarCampo(campoObservacao);

        if (editando) {
            campoNome.setText(doador.getNome());
            campoCpf.setText(formatarCpfParaMascara(doador.getCpf()));
            campoTelefone.setText(doador.getTelefone());
            campoEmail.setText(doador.getEmail());
            campoEndereco.setText(doador.getEndereco());
            campoObservacao.setText(doador.getObservacao());
        }

        adicionarCampo(form, gbc, 0, "Nome *", campoNome);
        adicionarCampo(form, gbc, 2, "CPF", campoCpf);
        adicionarCampo(form, gbc, 4, "Telefone", campoTelefone);
        adicionarCampo(form, gbc, 6, "E-mail", campoEmail);
        adicionarCampo(form, gbc, 8, "Endereço", campoEndereco);
        adicionarCampo(form, gbc, 10, "Observação", campoObservacao);

        JScrollPane scrollForm = new JScrollPane(form);
        scrollForm.setBorder(null);
        scrollForm.getViewport().setBackground(Color.WHITE);
        scrollForm.getVerticalScrollBar().setUnitIncrement(16);

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
            String nome = campoNome.getText().trim();
            String cpf = campoCpf.getText().trim();
            String cpfLimpo = cpf.replaceAll("[^0-9]", "");
            String telefone = campoTelefone.getText().trim();
            String email = campoEmail.getText().trim();
            String endereco = campoEndereco.getText().trim();
            String observacao = campoObservacao.getText().trim();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Informe o nome do doador.", "Atenção", JOptionPane.WARNING_MESSAGE);
                campoNome.requestFocus();
                return;
            }

            Doador novoDoador = new Doador(
                    editando ? doador.getId() : null,
                    nome,
                    cpfLimpo.isEmpty() ? null : cpf,
                    endereco.isEmpty() ? null : endereco,
                    telefone.isEmpty() ? null : telefone,
                    email.isEmpty() ? null : email,
                    observacao.isEmpty() ? null : observacao
            );

            resultado[0] = novoDoador;
            dialog.dispose();
        });

        rodapeDialog.add(btnCancelar);
        rodapeDialog.add(btnSalvar);

        dialog.add(cabecalho, BorderLayout.NORTH);
        dialog.add(scrollForm, BorderLayout.CENTER);
        dialog.add(rodapeDialog, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(() -> campoNome.requestFocus());

        dialog.setVisible(true);

        return resultado[0];
    }

    private JFormattedTextField criarCampoCpf() {
        try {
            MaskFormatter mascara = new MaskFormatter("###.###.###-##");
            mascara.setPlaceholderCharacter('_');
            mascara.setValueContainsLiteralCharacters(true);

            JFormattedTextField campo = new JFormattedTextField(mascara);
            campo.setFont(new Font("Arial", Font.PLAIN, 14));
            campo.setPreferredSize(new Dimension(360, 38));
            campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 225, 230)),
                    new EmptyBorder(8, 12, 8, 12)
            ));
            campo.setBackground(new Color(250, 250, 250));
            campo.setForeground(new Color(44, 62, 80));

            return campo;

        } catch (ParseException e) {
            throw new RuntimeException("Erro ao criar máscara de CPF", e);
        }
    }

    private String formatarCpfParaMascara(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return "";
        }

        String cpfLimpo = cpf.replaceAll("[^0-9]", "");

        if (cpfLimpo.length() != 11) {
            return cpf;
        }

        return cpfLimpo.substring(0, 3) + "." +
                cpfLimpo.substring(3, 6) + "." +
                cpfLimpo.substring(6, 9) + "-" +
                cpfLimpo.substring(9, 11);
    }

    private void adicionarCampo(JPanel form, GridBagConstraints gbc, int linha, String label, JComponent campo) {
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
        btn.setPreferredSize(new Dimension(145, 36));
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
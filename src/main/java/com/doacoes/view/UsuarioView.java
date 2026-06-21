package com.doacoes.view;

import com.doacoes.model.Usuario;
import com.doacoes.service.UsuarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class UsuarioView extends JFrame {

    private final UsuarioService service = new UsuarioService();

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public UsuarioView() {
        service.inicializar();
        configurarJanela();
        construirInterface();
        carregarTabela();
    }

    private void configurarJanela() {
        setTitle("Solidário - Usuários");
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

        JLabel titulo = new JLabel("Usuários do Sistema");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Cadastre, edite e gerencie os acessos ao sistema.");
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

        JLabel tituloLista = new JLabel("Usuários cadastrados");
        tituloLista.setFont(new Font("Arial", Font.BOLD, 18));
        tituloLista.setForeground(new Color(44, 62, 80));

        JLabel descricaoLista = new JLabel("Visualize, edite ou remova os usuários cadastrados no sistema.");
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
        String[] colunas = {"ID", "Nome", "Login", "Perfil"};

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
        tabela.getColumnModel().getColumn(1).setPreferredWidth(320);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(220);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(160);

        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(new Color(44, 62, 80));
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.getTableHeader().setPreferredSize(new Dimension(0, 36));
        tabela.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);

        tabela.getColumnModel().getColumn(0).setCellRenderer(centralizado);
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

        List<Usuario> usuarios = service.listar();

        for (Usuario usuario : usuarios) {
            modeloTabela.addRow(new Object[]{
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getLogin(),
                    usuario.getPerfil()
            });
        }
    }

    private void cadastrar() {
        Usuario usuario = abrirFormulario(null);

        if (usuario != null) {
            boolean salvou = service.salvar(usuario);

            if (salvou) {
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
                carregarTabela();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível cadastrar o usuário.",
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
                    "Selecione um usuário na tabela.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linha);
        Long id = Long.valueOf(modeloTabela.getValueAt(linhaModelo, 0).toString());

        Usuario usuarioExistente = service.buscarPorId(id);

        if (usuarioExistente == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Usuário não encontrado.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Usuario usuarioAlterado = abrirFormulario(usuarioExistente);

        if (usuarioAlterado != null) {
            boolean atualizou = service.atualizar(usuarioAlterado);

            if (atualizou) {
                JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!");
                carregarTabela();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível atualizar o usuário.",
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
                    "Selecione um usuário na tabela.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linha);

        Long id = Long.valueOf(modeloTabela.getValueAt(linhaModelo, 0).toString());
        String nome = modeloTabela.getValueAt(linhaModelo, 1).toString();
        String login = modeloTabela.getValueAt(linhaModelo, 2).toString();

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Confirma a exclusão do usuário abaixo?\n\n" +
                        "ID: " + id + "\n" +
                        "Nome: " + nome + "\n" +
                        "Login: " + login,
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            service.deletar(id);

            JOptionPane.showMessageDialog(
                    this,
                    "Usuário deletado com sucesso!"
            );

            carregarTabela();
        }
    }

    private Usuario abrirFormulario(Usuario usuario) {
        boolean editando = usuario != null;

        final Usuario[] resultado = new Usuario[1];

        JDialog dialog = new JDialog(
                this,
                editando ? "Editar Usuário" : "Cadastrar Usuário",
                true
        );

        dialog.setSize(500, 520);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(241, 245, 249));

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(editando ? new Color(41, 128, 185) : new Color(39, 174, 96));
        cabecalho.setBorder(new EmptyBorder(18, 25, 18, 25));

        JLabel titulo = new JLabel(editando ? "Editar Usuário" : "Novo Usuário");
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

        JTextField campoLogin = new JTextField();
        estilizarCampo(campoLogin);

        JPasswordField campoSenha = new JPasswordField();
        estilizarCampo(campoSenha);

        JComboBox<String> campoPerfil = new JComboBox<>(new String[]{
                "ADMIN", "USUARIO"
        });
        campoPerfil.setFont(new Font("Arial", Font.PLAIN, 14));
        campoPerfil.setPreferredSize(new Dimension(360, 38));
        campoPerfil.setBackground(Color.WHITE);
        campoPerfil.setForeground(new Color(44, 62, 80));

        if (editando) {
            campoNome.setText(usuario.getNome());
            campoLogin.setText(usuario.getLogin());
            campoSenha.setText(usuario.getSenha());
            campoPerfil.setSelectedItem(usuario.getPerfil());
        }

        adicionarCampo(form, gbc, 0, "Nome", campoNome);
        adicionarCampo(form, gbc, 2, "Login", campoLogin);
        adicionarCampo(form, gbc, 4, "Senha", campoSenha);
        adicionarCampo(form, gbc, 6, "Perfil", campoPerfil);

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
            String login = campoLogin.getText().trim();
            String senha = new String(campoSenha.getPassword()).trim();
            String perfil = campoPerfil.getSelectedItem().toString();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Informe o nome do usuário.", "Atenção", JOptionPane.WARNING_MESSAGE);
                campoNome.requestFocus();
                return;
            }

            if (login.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Informe o login do usuário.", "Atenção", JOptionPane.WARNING_MESSAGE);
                campoLogin.requestFocus();
                return;
            }

            if (senha.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Informe a senha do usuário.", "Atenção", JOptionPane.WARNING_MESSAGE);
                campoSenha.requestFocus();
                return;
            }

            Usuario novoUsuario = new Usuario();

            if (editando) {
                novoUsuario.setId(usuario.getId());
            }

            novoUsuario.setNome(nome);
            novoUsuario.setLogin(login);
            novoUsuario.setSenha(senha);
            novoUsuario.setPerfil(perfil);

            resultado[0] = novoUsuario;
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
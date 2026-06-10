package com.doacoes.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class MenuView extends JFrame {

    public MenuView() {
        configurarJanela();
        construirInterface();
    }

    private void configurarJanela() {
        setTitle("Solidário - Menu Principal");
        setSize(920, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void construirInterface() {
        JPanel fundo = new JPanel(new BorderLayout());
        fundo.setBackground(new Color(241, 245, 249));
        setContentPane(fundo);

        fundo.add(criarCabecalho(), BorderLayout.NORTH);
        fundo.add(criarPainelCentral(), BorderLayout.CENTER);
        fundo.add(criarRodape(), BorderLayout.SOUTH);
    }

    private JPanel criarCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(new Color(22, 160, 133));
        cabecalho.setBorder(new EmptyBorder(28, 45, 28, 45));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("SOLIDÁRIO");
        titulo.setFont(new Font("Arial", Font.BOLD, 34));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Sistema de Gerenciamento de Doações");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitulo.setForeground(new Color(235, 245, 245));

        textos.add(titulo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(subtitulo);

        JLabel usuario = new JLabel("Menu Principal");
        usuario.setFont(new Font("Arial", Font.BOLD, 15));
        usuario.setForeground(Color.WHITE);
        usuario.setHorizontalAlignment(SwingConstants.RIGHT);

        cabecalho.add(textos, BorderLayout.WEST);
        cabecalho.add(usuario, BorderLayout.EAST);

        return cabecalho;
    }

    private JPanel criarPainelCentral() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(241, 245, 249));
        painel.setBorder(new EmptyBorder(28, 45, 20, 45));

        JLabel instrucao = new JLabel("Selecione um módulo para continuar");
        instrucao.setFont(new Font("Arial", Font.BOLD, 18));
        instrucao.setForeground(new Color(44, 62, 80));

        JLabel descricao = new JLabel("Acesse as funcionalidades do sistema de forma rápida e organizada.");
        descricao.setFont(new Font("Arial", Font.PLAIN, 13));
        descricao.setForeground(new Color(100, 116, 139));

        JPanel painelTexto = new JPanel();
        painelTexto.setOpaque(false);
        painelTexto.setLayout(new BoxLayout(painelTexto, BoxLayout.Y_AXIS));
        painelTexto.add(instrucao);
        painelTexto.add(Box.createVerticalStrut(5));
        painelTexto.add(descricao);

        painel.add(painelTexto, BorderLayout.NORTH);

        JPanel painelCards = new JPanel(new GridLayout(4, 2, 18, 18));
        painelCards.setOpaque(false);
        painelCards.setBorder(new EmptyBorder(25, 0, 0, 0));

        painelCards.add(criarCardModulo(
                "Tipo de Doação",
                "Cadastrar e organizar tipos de itens",
                new Color(41, 128, 185),
                e -> abrirTipoDoacao()
        ));

        painelCards.add(criarCardModulo(
                "Usuários",
                "Gerenciar acessos ao sistema",
                new Color(52, 73, 94),
                e -> abrirUsuarios()
        ));

        painelCards.add(criarCardModulo(
                "Tamanhos",
                "Cadastrar tamanhos de roupas",
                new Color(108, 92, 231),
                e -> abrirTamanhos()
        ));

        painelCards.add(criarCardModulo(
                "Estoque",
                "Acompanhar itens disponíveis",
                new Color(39, 174, 96),
                e -> abrirEstoque()
        ));

        painelCards.add(criarCardModulo(
                "Doador",
                "Gerenciar pessoas que realizam doações",
                new Color(142, 68, 173),
                e -> JOptionPane.showMessageDialog(this, "Módulo Doador\n(Em desenvolvimento - Dacio)")
        ));

        painelCards.add(criarCardModulo(
                "Receptor",
                "Gerenciar pessoas que recebem doações",
                new Color(230, 126, 34),
                e -> JOptionPane.showMessageDialog(this, "Módulo Receptor\n(Em desenvolvimento - Dacio)")
        ));

        painelCards.add(criarCardModulo(
                "Doação",
                "Registrar novas doações recebidas",
                new Color(22, 160, 133),
                e -> abrirDoacao()
        ));

        painelCards.add(criarCardModulo(
                "Distribuição",
                "Controlar a entrega das doações",
                new Color(192, 57, 43),
                e -> JOptionPane.showMessageDialog(this, "Módulo Distribuição\n(Em desenvolvimento - Pedro)")
        ));

        painel.add(painelCards, BorderLayout.CENTER);

        return painel;
    }

    private JButton criarCardModulo(String titulo, String subtitulo, Color cor,
                                    java.awt.event.ActionListener acao) {

        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color corFundo = cor;

                if (getModel().isPressed()) {
                    corFundo = cor.darker();
                } else if (getModel().isRollover()) {
                    corFundo = cor.brighter();
                }

                g2.setColor(corFundo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

                g2.setColor(new Color(255, 255, 255, 35));
                g2.fillOval(getWidth() - 70, -25, 110, 110);

                g2.dispose();

                super.paintComponent(g);
            }
        };

        btn.setLayout(new BorderLayout());
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 19));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("<html>" + subtitulo + "</html>");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(255, 255, 255, 220));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(lblTitulo);
        textos.add(Box.createVerticalStrut(8));
        textos.add(lblSubtitulo);

        btn.add(textos, BorderLayout.CENTER);
        btn.addActionListener(acao);

        return btn;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(Color.WHITE);
        rodape.setBorder(new EmptyBorder(14, 45, 14, 45));

        JLabel info = new JLabel("Solidário • Gestão de doações, estoque e distribuição");
        info.setFont(new Font("Arial", Font.PLAIN, 12));
        info.setForeground(new Color(100, 116, 139));

        JButton btnSair = new JButton("Sair do Sistema");
        btnSair.setFont(new Font("Arial", Font.BOLD, 13));
        btnSair.setBackground(new Color(231, 76, 60));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.setBorderPainted(false);
        btnSair.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSair.setPreferredSize(new Dimension(160, 36));

        btnSair.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja sair do sistema e voltar para a tela de login?",
                    "Sair",
                    JOptionPane.YES_NO_OPTION
            );

            if (op == JOptionPane.YES_OPTION) {
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
                dispose();
            }
        });

        rodape.add(info, BorderLayout.WEST);
        rodape.add(btnSair, BorderLayout.EAST);

        return rodape;
    }

    public void iniciar() {
        setVisible(true);
    }

    private void abrirUsuarios() {
        UsuarioView usuarioView = new UsuarioView();
        usuarioView.setVisible(true);
    }

    private void abrirEstoque() {
        EstoqueView estoqueView = new EstoqueView();
        estoqueView.setVisible(true);
    }

    private void abrirDoacao() {
        DoacaoView doacaoView = new DoacaoView();
        doacaoView.setVisible(true);
    }

    private void abrirTamanhos() {
        TamanhoRoupaView tamanhoRoupaView = new TamanhoRoupaView();
        tamanhoRoupaView.setVisible(true);
    }

    private void abrirTipoDoacao() {
        try {
            TipoDoacaoView tipoDoacaoView = new TipoDoacaoView();
            tipoDoacaoView.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao abrir Tipos de Doação:\n" + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
}
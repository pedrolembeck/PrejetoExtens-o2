package com.doacoes.view;

import com.doacoes.model.Usuario;
import com.doacoes.service.UsuarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

public class LoginView extends JFrame {

    private final UsuarioService usuarioService = new UsuarioService();

    private JTextField campoLogin;
    private JPasswordField campoSenha;

    public LoginView() {
        usuarioService.inicializar();
        configurarJanela();
        construirInterface();
    }

    private void configurarJanela() {
        setTitle("Solidário - Login");
        setSize(900, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void construirInterface() {
        BackgroundPanel fundo = new BackgroundPanel();
        fundo.setLayout(new GridBagLayout());
        setContentPane(fundo);

        JPanel container = new JPanel(new GridLayout(1, 2));
        container.setOpaque(false);
        container.setPreferredSize(new Dimension(790, 430));

        container.add(criarPainelMarca());
        container.add(criarPainelLogin());

        fundo.add(container);
    }

    private JPanel criarPainelMarca() {
        JPanel painel = new BrandPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(new EmptyBorder(45, 35, 45, 35));

        JPanel icone = criarIconeSolidario();

        JLabel titulo = new JLabel("SOLIDÁRIO");
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Sistema de Gestão de Doações");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitulo.setForeground(new Color(235, 245, 245));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel frase = new JLabel(obterFraseAleatoria());
        frase.setFont(new Font("Arial", Font.PLAIN, 14));
        frase.setForeground(new Color(230, 245, 245));
        frase.setAlignmentX(Component.CENTER_ALIGNMENT);
        frase.setHorizontalAlignment(SwingConstants.CENTER);
        frase.setPreferredSize(new Dimension(320, 60));
        frase.setMaximumSize(new Dimension(320, 60));

        painel.add(Box.createVerticalGlue());
        painel.add(icone);
        painel.add(Box.createVerticalStrut(18));
        painel.add(titulo);
        painel.add(Box.createVerticalStrut(8));
        painel.add(subtitulo);
        painel.add(Box.createVerticalStrut(28));
        painel.add(frase);
        painel.add(Box.createVerticalGlue());

        return painel;
    }

    private JPanel criarPainelLogin() {
        JPanel painel = new RoundedPanel(Color.WHITE);
        painel.setLayout(new GridBagLayout());
        painel.setBorder(new EmptyBorder(35, 45, 35, 45));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        JLabel tituloLogin = new JLabel("Acessar conta");
        tituloLogin.setFont(new Font("Arial", Font.BOLD, 26));
        tituloLogin.setForeground(new Color(44, 62, 80));

        JLabel subtituloLogin = new JLabel("Informe suas credenciais para continuar");
        subtituloLogin.setFont(new Font("Arial", Font.PLAIN, 13));
        subtituloLogin.setForeground(new Color(127, 140, 141));

        JLabel lblLogin = new JLabel("Login");
        lblLogin.setFont(new Font("Arial", Font.BOLD, 13));
        lblLogin.setForeground(new Color(52, 73, 94));

        campoLogin = new JTextField();
        estilizarCampo(campoLogin);

        JLabel lblSenha = new JLabel("Senha");
        lblSenha.setFont(new Font("Arial", Font.BOLD, 13));
        lblSenha.setForeground(new Color(52, 73, 94));

        campoSenha = new JPasswordField();
        estilizarCampo(campoSenha);

        RoundedButton btnEntrar = new RoundedButton("Entrar no Sistema", new Color(22, 160, 133));
        btnEntrar.setPreferredSize(new Dimension(280, 42));

        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Arial", Font.BOLD, 13));
        btnSair.setForeground(new Color(127, 140, 141));
        btnSair.setBackground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.setBorderPainted(false);
        btnSair.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnEntrar.addActionListener(e -> autenticar());
        btnSair.addActionListener(e -> System.exit(0));
        campoSenha.addActionListener(e -> autenticar());

        gbc.gridy = 0;
        painel.add(tituloLogin, gbc);

        gbc.gridy = 1;
        painel.add(subtituloLogin, gbc);

        gbc.gridy = 2;
        painel.add(Box.createVerticalStrut(18), gbc);

        gbc.gridy = 3;
        painel.add(lblLogin, gbc);

        gbc.gridy = 4;
        painel.add(campoLogin, gbc);

        gbc.gridy = 5;
        painel.add(lblSenha, gbc);

        gbc.gridy = 6;
        painel.add(campoSenha, gbc);

        gbc.gridy = 7;
        painel.add(Box.createVerticalStrut(12), gbc);

        gbc.gridy = 8;
        painel.add(btnEntrar, gbc);

        gbc.gridy = 9;
        painel.add(btnSair, gbc);

        SwingUtilities.invokeLater(() -> campoLogin.requestFocus());

        return painel;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(280, 38));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                new EmptyBorder(8, 12, 8, 12)
        ));
        campo.setBackground(new Color(250, 250, 250));
        campo.setForeground(new Color(44, 62, 80));
    }

    private JPanel criarIconeSolidario() {
        JPanel painelIcone = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fillOval(5, 5, 90, 90);

                g2.setColor(new Color(22, 160, 133));

                Path2D.Double coracao = new Path2D.Double();
                coracao.moveTo(50, 72);
                coracao.curveTo(25, 52, 21, 35, 32, 27);
                coracao.curveTo(40, 21, 48, 27, 50, 36);
                coracao.curveTo(52, 27, 60, 21, 68, 27);
                coracao.curveTo(79, 35, 75, 52, 50, 72);
                coracao.closePath();

                g2.fill(coracao);
                g2.dispose();
            }
        };

        painelIcone.setPreferredSize(new Dimension(100, 100));
        painelIcone.setMaximumSize(new Dimension(100, 100));
        painelIcone.setMinimumSize(new Dimension(100, 100));
        painelIcone.setOpaque(false);
        painelIcone.setAlignmentX(Component.CENTER_ALIGNMENT);

        return painelIcone;
    }

    private String obterFraseAleatoria() {
        String[] frases = {
                "Organizando solidariedade,<br>conectando doações a quem mais precisa.",
                "Cada doação registrada<br>é um passo para transformar vidas.",
                "Gestão simples para tornar<br>a solidariedade mais eficiente.",
                "Controle, organização e cuidado<br>em cada doação recebida.",
                "A tecnologia ajudando<br>a solidariedade chegar mais longe.",
                "Doar é um gesto de amor.<br>Organizar é garantir o destino certo.",
                "Conectando pessoas, doações<br>e esperança em um só lugar.",
                "Solidariedade com organização,<br>responsabilidade e propósito."
        };

        int indice = new java.util.Random().nextInt(frases.length);

        return "<html><div style='text-align:center;'>" +
                frases[indice] +
                "</div></html>";
    }

    private void autenticar() {
        String login = campoLogin.getText().trim();
        String senha = new String(campoSenha.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe o login e a senha.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Usuario usuario = usuarioService.autenticar(login, senha);

        if (usuario != null) {
            MenuView menuView = new MenuView();
            menuView.setVisible(true);

            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Login ou senha inválidos.",
                    "Acesso negado",
                    JOptionPane.ERROR_MESSAGE
            );

            campoSenha.setText("");
            campoSenha.requestFocus();
        }
    }

    private static class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(236, 240, 241),
                    getWidth(), getHeight(), new Color(214, 234, 229)
            );

            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.dispose();
        }
    }

    private static class BrandPanel extends JPanel {

        public BrandPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(22, 160, 133),
                    getWidth(), getHeight(), new Color(17, 122, 101)
            );

            g2.setPaint(gradient);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() + 25, getHeight(), 24, 24));

            g2.dispose();

            super.paintComponent(g);
        }
    }

    private static class RoundedPanel extends JPanel {

        private final Color backgroundColor;

        public RoundedPanel(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(backgroundColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 24, 24));

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
            setFont(new Font("Arial", Font.BOLD, 14));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(10, 15, 10, 15));
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

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

            g2.dispose();

            super.paintComponent(g);
        }
    }
}
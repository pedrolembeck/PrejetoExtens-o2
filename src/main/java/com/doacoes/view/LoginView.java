package com.doacoes.view;

import com.doacoes.model.Usuario;
import com.doacoes.service.UsuarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

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
        setTitle("Login - Sistema de Doações");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(236, 240, 241));
    }

    private void construirInterface() {
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(new Color(23, 32, 42));
        painelTitulo.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel titulo = new JLabel("Acesso ao Sistema");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);

        painelTitulo.add(titulo);
        add(painelTitulo, BorderLayout.NORTH);

        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBackground(new Color(236, 240, 241));
        painelForm.setBorder(new EmptyBorder(20, 35, 20, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblLogin = new JLabel("Login:");
        lblLogin.setFont(new Font("Arial", Font.BOLD, 13));

        campoLogin = new JTextField(20);
        campoLogin.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.BOLD, 13));

        campoSenha = new JPasswordField(20);
        campoSenha.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setBackground(new Color(41, 128, 185));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 13));
        btnEntrar.setPreferredSize(new Dimension(120, 35));

        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(new Color(127, 140, 141));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.setBorderPainted(false);
        btnSair.setFont(new Font("Arial", Font.BOLD, 13));
        btnSair.setPreferredSize(new Dimension(120, 35));

        btnEntrar.addActionListener(e -> autenticar());
        btnSair.addActionListener(e -> System.exit(0));
        campoSenha.addActionListener(e -> autenticar());

        gbc.gridx = 0;
        gbc.gridy = 0;
        painelForm.add(lblLogin, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        painelForm.add(campoLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        painelForm.add(lblSenha, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        painelForm.add(campoSenha, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        painelBotoes.setBackground(new Color(236, 240, 241));
        painelBotoes.add(btnEntrar);
        painelBotoes.add(btnSair);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        painelForm.add(painelBotoes, gbc);

        add(painelForm, BorderLayout.CENTER);
    }

    private void autenticar() {
        String login = campoLogin.getText();
        String senha = new String(campoSenha.getPassword());

        Usuario usuario = usuarioService.autenticar(login, senha);

        if (usuario != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Bem-vindo, " + usuario.getNome() + "!"
            );

            MenuView menuView = new MenuView();
            menuView.setVisible(true);

            dispose();
        }
    }
}
package com.doacoes.view;

import com.doacoes.model.Usuario;
import com.doacoes.service.UsuarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        setTitle("Gerenciamento de Usuários");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));
    }

    private void construirInterface() {
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(new Color(52, 73, 94));
        painelTitulo.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel titulo = new JLabel("Usuários do Sistema");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);

        painelTitulo.add(titulo);
        add(painelTitulo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome", "Login", "Perfil"};

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
        JButton btnAtualizar = criarBotao("Atualizar Lista", new Color(127, 140, 141));

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

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(140, 35));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
            }
        }
    }

    private void editar() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela!");
            return;
        }

        Long id = Long.valueOf(modeloTabela.getValueAt(linha, 0).toString());

        Usuario usuarioExistente = service.buscarPorId(id);

        if (usuarioExistente == null) {
            JOptionPane.showMessageDialog(this, "Usuário não encontrado!");
            return;
        }

        Usuario usuarioAlterado = abrirFormulario(usuarioExistente);

        if (usuarioAlterado != null) {
            boolean atualizou = service.atualizar(usuarioAlterado);

            if (atualizou) {
                JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!");
                carregarTabela();
            }
        }
    }

    private void deletar() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela!");
            return;
        }

        Long id = Long.valueOf(modeloTabela.getValueAt(linha, 0).toString());
        String nome = modeloTabela.getValueAt(linha, 1).toString();

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente deletar este usuário?\n\n" +
                        "ID: " + id + "\n" +
                        "Nome: " + nome,
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            service.deletar(id);
            JOptionPane.showMessageDialog(this, "Usuário deletado com sucesso!");
            carregarTabela();
        }
    }

    private Usuario abrirFormulario(Usuario usuario) {
        JTextField campoNome = new JTextField();
        JTextField campoLogin = new JTextField();
        JPasswordField campoSenha = new JPasswordField();

        JComboBox<String> campoPerfil = new JComboBox<>(new String[]{
                "ADMIN", "USUARIO"
        });

        if (usuario != null) {
            campoNome.setText(usuario.getNome());
            campoLogin.setText(usuario.getLogin());
            campoSenha.setText(usuario.getSenha());
            campoPerfil.setSelectedItem(usuario.getPerfil());
        }

        JPanel painel = new JPanel(new GridLayout(0, 2, 8, 8));

        painel.add(new JLabel("Nome:"));
        painel.add(campoNome);

        painel.add(new JLabel("Login:"));
        painel.add(campoLogin);

        painel.add(new JLabel("Senha:"));
        painel.add(campoSenha);

        painel.add(new JLabel("Perfil:"));
        painel.add(campoPerfil);

        int resultado = JOptionPane.showConfirmDialog(
                this,
                painel,
                usuario == null ? "Cadastrar Usuário" : "Editar Usuário",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return null;
        }

        Usuario novoUsuario = new Usuario();

        if (usuario != null) {
            novoUsuario.setId(usuario.getId());
        }

        novoUsuario.setNome(campoNome.getText().trim());
        novoUsuario.setLogin(campoLogin.getText().trim());
        novoUsuario.setSenha(new String(campoSenha.getPassword()).trim());
        novoUsuario.setPerfil(campoPerfil.getSelectedItem().toString());

        return novoUsuario;
    }
}
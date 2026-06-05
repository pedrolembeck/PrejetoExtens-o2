package com.doacoes.view;

import com.doacoes.enums.CategoriaItem;
import com.doacoes.model.TipoDoacao;
import com.doacoes.service.TipoDoacaoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuView extends JFrame {

    private final TipoDoacaoService tipoService = new TipoDoacaoService();

    public MenuView() {
        configurarJanela();
        construirInterface();
    }

    private void configurarJanela() {
        setTitle("Sistema de Doações - Menu Principal");
        setSize(600, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(new Color(236, 240, 241));
    }

    private void construirInterface() {

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(new Color(23, 32, 42));
        cabecalho.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitulo = new JLabel("Sistema de Gerenciamento de Doações");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblSubtitulo = new JLabel("Selecione um módulo para continuar");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(149, 165, 166));
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        cabecalho.add(lblTitulo, BorderLayout.CENTER);
        cabecalho.add(lblSubtitulo, BorderLayout.SOUTH);

        add(cabecalho, BorderLayout.NORTH);

        JPanel painelModulos = new JPanel(new GridLayout(3, 2, 15, 15));
        painelModulos.setBackground(new Color(236, 240, 241));
        painelModulos.setBorder(new EmptyBorder(25, 30, 15, 30));

        painelModulos.add(criarBotaoModulo(
                "Tipo de Doação",
                "Cadastrar e gerenciar tipos",
                new Color(41, 128, 185),
                e -> abrirTipoDoacao()
        ));

        painelModulos.add(criarBotaoModulo(
                "Estoque",
                "Controlar itens em estoque",
                new Color(39, 174, 96),
                e -> abrirEstoque()
        ));

        painelModulos.add(criarBotaoModulo(
                "Doador",
                "Gerenciar doadores",
                new Color(142, 68, 173),
                e -> JOptionPane.showMessageDialog(this, "Módulo Doador\n(Em desenvolvimento - Dacio)")
        ));

        painelModulos.add(criarBotaoModulo(
                "Receptor",
                "Gerenciar receptores",
                new Color(211, 84, 0),
                e -> JOptionPane.showMessageDialog(this, "Módulo Receptor\n(Em desenvolvimento - Dacio)")
        ));

        painelModulos.add(criarBotaoModulo(
                "Doação",
                "Registrar doações",
                new Color(22, 160, 133),
                e -> abrirDoacao()
        ));

        painelModulos.add(criarBotaoModulo(
                "Distribuição",
                "Gerenciar distribuições",
                new Color(192, 57, 43),
                e -> JOptionPane.showMessageDialog(this, "Módulo Distribuição\n(Em desenvolvimento - Pedro)")
        ));

        add(painelModulos, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.setBackground(new Color(189, 195, 199));
        rodape.setBorder(new EmptyBorder(8, 15, 8, 15));

        JButton btnSair = new JButton("Sair do Sistema");
        btnSair.setFont(new Font("Arial", Font.BOLD, 12));
        btnSair.setBackground(new Color(127, 140, 141));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.setBorderPainted(false);
        btnSair.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnSair.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja sair do sistema?",
                    "Sair",
                    JOptionPane.YES_NO_OPTION
            );

            if (op == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        rodape.add(btnSair);

        add(rodape, BorderLayout.SOUTH);
    }

    private JButton criarBotaoModulo(String titulo, String subtitulo, Color cor,
                                     java.awt.event.ActionListener acao) {

        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.setBackground(cor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 15));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(new EmptyBorder(10, 5, 0, 5));

        JLabel lblSub = new JLabel(subtitulo, SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 11));
        lblSub.setForeground(new Color(255, 255, 255, 200));
        lblSub.setBorder(new EmptyBorder(0, 5, 10, 5));

        btn.add(lblTitulo, BorderLayout.CENTER);
        btn.add(lblSub, BorderLayout.SOUTH);
        btn.addActionListener(acao);

        return btn;
    }

    public void iniciar() {
        setVisible(true);
    }

    private void abrirEstoque() {
        EstoqueView estoqueView = new EstoqueView();
        estoqueView.setVisible(true);
    }

    private void abrirDoacao() {
        DoacaoView doacaoView = new DoacaoView();
        doacaoView.setVisible(true);
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

    private void menuTipoDoacao() {

        int opcao;

        do {
            opcao = Integer.parseInt(
                    JOptionPane.showInputDialog("""

                            ===== TIPO DOAÇÃO =====

                            1 - Cadastrar
                            2 - Listar
                            3 - Atualizar
                            4 - Deletar
                            0 - Voltar
                            """)
            );

            switch (opcao) {
                case 1 -> cadastrarTipo();
                case 2 -> listarTipos();
                case 3 -> atualizarTipo();
                case 4 -> deletarTipo();
                case 0 -> {
                }
                default -> JOptionPane.showMessageDialog(null, "Opção inválida");
            }

        } while (opcao != 0);
    }

    private void cadastrarTipo() {

        String nome = JOptionPane.showInputDialog("Nome:").toUpperCase();

        int opcaoCategoria = Integer.parseInt(
                JOptionPane.showInputDialog("""

                    Selecione a categoria:

                    1 - VESTUARIO
                    2 - ALIMENTOS
                    """)
        );

        CategoriaItem categoria;

        switch (opcaoCategoria) {
            case 1 -> categoria = CategoriaItem.VESTUARIO;
            case 2 -> categoria = CategoriaItem.ALIMENTOS;
            default -> {
                JOptionPane.showMessageDialog(null, "Categoria inválida");
                return;
            }
        }

        TipoDoacao tipo = new TipoDoacao();
        tipo.setNome(nome);
        tipo.setCategoria(categoria);

        boolean salvou = tipoService.salvar(tipo);

        if (salvou) {
            JOptionPane.showMessageDialog(null, "Cadastrado com sucesso");
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar");
        }
    }

    private void listarTipos() {

        StringBuilder sb = new StringBuilder();

        tipoService.listar().forEach(item ->
                sb.append(item).append("\n")
        );

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void atualizarTipo() {

        Long id = Long.parseLong(
                JOptionPane.showInputDialog("Digite o ID:")
        );

        TipoDoacao tipo = tipoService.buscarPorId(id);

        if (tipo == null) {
            JOptionPane.showMessageDialog(null, "Registro não encontrado");
            return;
        }

        String novoNome = JOptionPane.showInputDialog(
                "Novo nome:",
                tipo.getNome()
        );

        int opcaoCategoria = Integer.parseInt(
                JOptionPane.showInputDialog("""

                    Selecione a categoria:

                    1 - VESTUARIO
                    2 - ALIMENTOS
                    """)
        );

        CategoriaItem categoria;

        switch (opcaoCategoria) {
            case 1 -> categoria = CategoriaItem.VESTUARIO;
            case 2 -> categoria = CategoriaItem.ALIMENTOS;
            default -> {
                JOptionPane.showMessageDialog(null, "Categoria inválida");
                return;
            }
        }

        tipo.setNome(novoNome);
        tipo.setCategoria(categoria);

        boolean atualizou = tipoService.atualizar(tipo);

        if (atualizou) {
            JOptionPane.showMessageDialog(null, "Atualizado com sucesso");
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar");
        }
    }

    private void deletarTipo() {

        Long id = Long.parseLong(
                JOptionPane.showInputDialog("Digite o ID:")
        );

        TipoDoacao tipo = tipoService.buscarPorId(id);

        if (tipo == null) {
            JOptionPane.showMessageDialog(null, "Tipo de doação não encontrado!");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                null,
                "Confirma a exclusão do item abaixo?\n\n" +
                        "ID: " + tipo.getId() + "\n" +
                        "Nome: " + tipo.getNome(),
                "Confirmar exclusão",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (confirmacao == JOptionPane.OK_OPTION) {
            tipoService.deletar(id);
            JOptionPane.showMessageDialog(null, "Deletado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Exclusão cancelada.");
        }
    }
}
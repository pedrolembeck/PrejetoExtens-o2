package com.doacoes.view;

import com.doacoes.model.Doacao;
import com.doacoes.service.DoacaoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DoacaoView extends JFrame {

    private final DoacaoService service = new DoacaoService();

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public DoacaoView() {
        service.inicializar();
        configurarJanela();
        construirInterface();
        carregarTabela();
    }

    private void configurarJanela() {
        setTitle("Gerenciamento de Doações");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
    }

    private void construirInterface() {
        JPanel painelSuperior = new JPanel(new BorderLayout());

        JLabel titulo = new JLabel("Cadastro de Doações", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(new EmptyBorder(15, 10, 8, 10));

        JPanel painelPesquisa = new JPanel(new FlowLayout());

        JLabel lblPesquisa = new JLabel("Pesquisar doador, item, categoria ou tamanho:");
        JTextField campoPesquisa = new JTextField(25);
        JButton btnPesquisar = new JButton("🔍 Pesquisar");
        JButton btnLimpar = new JButton("Limpar");

        btnPesquisar.addActionListener(e -> carregarTabelaPesquisa(campoPesquisa.getText()));

        btnLimpar.addActionListener(e -> {
            campoPesquisa.setText("");
            carregarTabela();
        });

        painelPesquisa.add(lblPesquisa);
        painelPesquisa.add(campoPesquisa);
        painelPesquisa.add(btnPesquisar);
        painelPesquisa.add(btnLimpar);

        painelSuperior.add(titulo, BorderLayout.NORTH);
        painelSuperior.add(painelPesquisa, BorderLayout.SOUTH);

        add(painelSuperior, BorderLayout.NORTH);

        String[] colunas = {"ID", "Doador", "Item", "Quantidade", "Categoria", "Tamanho", "Data", "Observação"};

        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout());

        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnAlterar = new JButton("Alterar");
        JButton btnDeletar = new JButton("Deletar");
        JButton btnAtualizar = new JButton("Atualizar Lista");

        btnCadastrar.addActionListener(e -> cadastrar());
        btnAlterar.addActionListener(e -> alterar());
        btnDeletar.addActionListener(e -> deletar());
        btnAtualizar.addActionListener(e -> carregarTabela());

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnAtualizar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);

        List<Doacao> doacoes = service.listar();

        for (Doacao doacao : doacoes) {
            adicionarLinhaTabela(doacao);
        }
    }

    private void carregarTabelaPesquisa(String termo) {
        modeloTabela.setRowCount(0);

        List<Doacao> doacoes = service.pesquisar(termo);

        for (Doacao doacao : doacoes) {
            adicionarLinhaTabela(doacao);
        }
    }

    private void adicionarLinhaTabela(Doacao doacao) {
        modeloTabela.addRow(new Object[]{
                doacao.getId(),
                doacao.getNomeDoador(),
                doacao.getDescricaoItem(),
                doacao.getQuantidade(),
                doacao.getCategoria(),
                doacao.getTamanho(),
                doacao.getDataDoacao(),
                doacao.getObservacao()
        });
    }

    private void cadastrar() {
        Doacao doacao = abrirFormulario(null);

        if (doacao != null) {
            boolean salvou = service.salvar(doacao);

            if (salvou) {
                JOptionPane.showMessageDialog(this, "Doação cadastrada com sucesso!");
                carregarTabela();
            }
        }
    }

    private void alterar() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma doação para alterar.");
            return;
        }

        Long id = Long.valueOf(modeloTabela.getValueAt(linha, 0).toString());

        Doacao doacaoExistente = service.buscarPorId(id);

        if (doacaoExistente == null) {
            JOptionPane.showMessageDialog(this, "Doação não encontrada.");
            return;
        }

        Doacao doacaoAlterada = abrirFormulario(doacaoExistente);

        if (doacaoAlterada != null) {
            boolean atualizou = service.atualizar(doacaoAlterada);

            if (atualizou) {
                JOptionPane.showMessageDialog(this, "Doação alterada com sucesso!");
                carregarTabela();
            }
        }
    }

    private void deletar() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma doação para deletar.");
            return;
        }

        Long id = Long.valueOf(modeloTabela.getValueAt(linha, 0).toString());

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente deletar esta doação?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            service.deletar(id);
            JOptionPane.showMessageDialog(this, "Doação deletada com sucesso!");
            carregarTabela();
        }
    }

    private Doacao abrirFormulario(Doacao doacao) {
        JTextField campoDoador = new JTextField();
        JTextField campoItem = new JTextField();
        JTextField campoQuantidade = new JTextField();

        JComboBox<String> campoCategoria = new JComboBox<>(new String[]{"ALIMENTOS", "VESTUARIO"});

        JComboBox<String> campoTamanho = new JComboBox<>(new String[]{
                "", "RN", "P", "M", "G", "GG", "XGG", "1", "2", "4", "6", "8", "10", "12", "14", "16"
        });

        JTextField campoData = new JTextField(LocalDate.now().toString());
        JTextField campoObservacao = new JTextField();

        campoTamanho.setEnabled(false);

        campoCategoria.addActionListener(e -> atualizarCampoTamanho(campoCategoria, campoTamanho));

        if (doacao != null) {
            campoDoador.setText(doacao.getNomeDoador());
            campoItem.setText(doacao.getDescricaoItem());
            campoQuantidade.setText(String.valueOf(doacao.getQuantidade()));
            campoCategoria.setSelectedItem(doacao.getCategoria());
            campoTamanho.setSelectedItem(doacao.getTamanho() == null ? "" : doacao.getTamanho());
            campoData.setText(String.valueOf(doacao.getDataDoacao()));
            campoObservacao.setText(doacao.getObservacao());

            atualizarCampoTamanho(campoCategoria, campoTamanho);
        }

        JPanel painel = new JPanel(new GridLayout(0, 2, 8, 8));

        painel.add(new JLabel("Doador:"));
        painel.add(campoDoador);

        painel.add(new JLabel("Item / Tipo de Vestimenta:"));
        painel.add(campoItem);

        painel.add(new JLabel("Quantidade:"));
        painel.add(campoQuantidade);

        painel.add(new JLabel("Categoria:"));
        painel.add(campoCategoria);

        painel.add(new JLabel("Tamanho:"));
        painel.add(campoTamanho);

        painel.add(new JLabel("Data da doação:"));
        painel.add(campoData);

        painel.add(new JLabel("Observação:"));
        painel.add(campoObservacao);

        int resultado = JOptionPane.showConfirmDialog(
                this,
                painel,
                doacao == null ? "Cadastrar Doação" : "Alterar Doação",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            Doacao novaDoacao = new Doacao();

            if (doacao != null) {
                novaDoacao.setId(doacao.getId());
            }

            String categoriaSelecionada = campoCategoria.getSelectedItem().toString();

            String tamanhoSelecionado = campoTamanho.getSelectedItem() == null
                    ? ""
                    : campoTamanho.getSelectedItem().toString();

            if (!"VESTUARIO".equals(categoriaSelecionada)) {
                tamanhoSelecionado = "";
            }

            novaDoacao.setNomeDoador(campoDoador.getText().trim());
            novaDoacao.setDescricaoItem(campoItem.getText().trim().toUpperCase());
            novaDoacao.setQuantidade(Integer.parseInt(campoQuantidade.getText().trim()));
            novaDoacao.setCategoria(categoriaSelecionada);
            novaDoacao.setTamanho(tamanhoSelecionado);
            novaDoacao.setDataDoacao(LocalDate.parse(campoData.getText().trim()));
            novaDoacao.setObservacao(campoObservacao.getText().trim());

            return novaDoacao;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade deve ser um número inteiro.");
            return null;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use o formato AAAA-MM-DD. Exemplo: 2026-06-02.");
            return null;
        }
    }

    private void atualizarCampoTamanho(JComboBox<String> campoCategoria, JComboBox<String> campoTamanho) {
        String categoriaSelecionada = campoCategoria.getSelectedItem().toString();

        if ("VESTUARIO".equals(categoriaSelecionada)) {
            campoTamanho.setEnabled(true);
        } else {
            campoTamanho.setSelectedItem("");
            campoTamanho.setEnabled(false);
        }
    }
}
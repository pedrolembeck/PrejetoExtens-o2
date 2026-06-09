package com.doacoes.view;

import com.doacoes.model.Doacao;
import com.doacoes.model.TamanhoRoupa;
import com.doacoes.model.TipoDoacao;
import com.doacoes.service.DoacaoService;
import com.doacoes.service.TamanhoRoupaService;
import com.doacoes.service.TipoDoacaoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DoacaoView extends JFrame {

    private final DoacaoService service = new DoacaoService();
    private final TamanhoRoupaService tamanhoRoupaService = new TamanhoRoupaService();
    private final TipoDoacaoService tipoDoacaoService = new TipoDoacaoService();

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public DoacaoView() {
        service.inicializar();
        tamanhoRoupaService.inicializar();
        configurarJanela();
        construirInterface();
        carregarTabela();
    }

    private void configurarJanela() {
        setTitle("Gerenciamento de Doações");
        setSize(1200, 620);
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

        JLabel lblPesquisa = new JLabel("Pesquisar doador, item, categoria, tamanho, unidade ou detalhe:");
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

        String[] colunas = {
                "ID", "Doador", "Item", "Quantidade", "Unidade",
                "Detalhe", "Categoria", "Tamanho", "Data", "Observação"
        };

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
        JButton btnRelatorio = new JButton("Relatório CSV");

        btnCadastrar.addActionListener(e -> cadastrar());
        btnAlterar.addActionListener(e -> alterar());
        btnDeletar.addActionListener(e -> deletar());
        btnAtualizar.addActionListener(e -> carregarTabela());
        btnRelatorio.addActionListener(e -> exportarRelatorioCSV());

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnRelatorio);

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
                doacao.getUnidade(),
                doacao.getDetalheUnidade(),
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

        JComboBox<String> campoItem = new JComboBox<>();
        campoItem.setEditable(true);

        JTextField campoQuantidade = new JTextField();

        JComboBox<String> campoUnidade = new JComboBox<>(new String[]{
                "UNIDADE", "KG", "PACOTE", "LITRO", "CAIXA", "CESTA"
        });

        JTextField campoDetalheUnidade = new JTextField();

        JComboBox<String> campoCategoria = new JComboBox<>(new String[]{
                "ALIMENTOS", "VESTUARIO"
        });

        JComboBox<String> campoTamanho = new JComboBox<>();
        carregarTamanhos(campoTamanho);

        JTextField campoData = new JTextField(LocalDate.now().toString());
        JTextField campoObservacao = new JTextField();

        carregarItensPorCategoria(campoItem, "ALIMENTOS", false);
        atualizarCamposPorCategoria(campoCategoria, campoTamanho, campoUnidade, campoDetalheUnidade);

        campoCategoria.addActionListener(e -> {
            String categoriaSelecionada = campoCategoria.getSelectedItem().toString();

            carregarItensPorCategoria(campoItem, categoriaSelecionada, true);
            atualizarCamposPorCategoria(campoCategoria, campoTamanho, campoUnidade, campoDetalheUnidade);
        });

        if (doacao != null) {
            campoDoador.setText(doacao.getNomeDoador());
            campoQuantidade.setText(String.valueOf(doacao.getQuantidade()));

            campoCategoria.setSelectedItem(doacao.getCategoria());

            carregarItensPorCategoria(campoItem, doacao.getCategoria(), false);
            campoItem.setSelectedItem(doacao.getDescricaoItem());

            campoUnidade.setSelectedItem(
                    doacao.getUnidade() == null || doacao.getUnidade().isBlank()
                            ? "UNIDADE"
                            : doacao.getUnidade()
            );

            campoDetalheUnidade.setText(
                    doacao.getDetalheUnidade() == null ? "" : doacao.getDetalheUnidade()
            );

            campoTamanho.setSelectedItem(doacao.getTamanho() == null ? "" : doacao.getTamanho());
            campoData.setText(String.valueOf(doacao.getDataDoacao()));
            campoObservacao.setText(doacao.getObservacao());

            atualizarCamposPorCategoria(campoCategoria, campoTamanho, campoUnidade, campoDetalheUnidade);
        }

        JPanel painel = new JPanel(new GridLayout(0, 2, 8, 8));

        painel.add(new JLabel("Doador:"));
        painel.add(campoDoador);

        painel.add(new JLabel("Item / Tipo de Doação:"));
        painel.add(campoItem);

        painel.add(new JLabel("Quantidade:"));
        painel.add(campoQuantidade);

        painel.add(new JLabel("Unidade:"));
        painel.add(campoUnidade);

        painel.add(new JLabel("Detalhe da unidade:"));
        painel.add(campoDetalheUnidade);

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
            String unidadeSelecionada = campoUnidade.getSelectedItem().toString();
            String detalheUnidade = campoDetalheUnidade.getText().trim().toUpperCase();

            String tamanhoSelecionado = campoTamanho.getSelectedItem() == null
                    ? ""
                    : campoTamanho.getSelectedItem().toString();

            if (!"VESTUARIO".equals(categoriaSelecionada)) {
                tamanhoSelecionado = "";
            }

            if ("VESTUARIO".equals(categoriaSelecionada)) {
                unidadeSelecionada = "UNIDADE";
                detalheUnidade = "";
            }

            String itemSelecionado = obterTextoCombo(campoItem);

            novaDoacao.setNomeDoador(campoDoador.getText().trim());
            novaDoacao.setDescricaoItem(itemSelecionado.toUpperCase());
            novaDoacao.setQuantidade(Integer.parseInt(campoQuantidade.getText().trim()));
            novaDoacao.setUnidade(unidadeSelecionada);
            novaDoacao.setDetalheUnidade(detalheUnidade);
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

    private void atualizarCamposPorCategoria(
            JComboBox<String> campoCategoria,
            JComboBox<String> campoTamanho,
            JComboBox<String> campoUnidade,
            JTextField campoDetalheUnidade
    ) {
        String categoriaSelecionada = campoCategoria.getSelectedItem().toString();

        if ("VESTUARIO".equals(categoriaSelecionada)) {
            campoTamanho.setEnabled(true);

            campoUnidade.setSelectedItem("UNIDADE");
            campoUnidade.setEnabled(false);

            campoDetalheUnidade.setText("");
            campoDetalheUnidade.setEnabled(false);
        } else {
            campoTamanho.setSelectedItem("");
            campoTamanho.setEnabled(false);

            campoUnidade.setEnabled(true);
            campoDetalheUnidade.setEnabled(true);
        }
    }

    private void carregarTamanhos(JComboBox<String> campoTamanho) {
        campoTamanho.removeAllItems();
        campoTamanho.addItem("");

        for (TamanhoRoupa tamanho : tamanhoRoupaService.listar()) {
            campoTamanho.addItem(tamanho.getTamanho() + " - " + tamanho.getTipo());
        }
    }

    private void carregarItensPorCategoria(JComboBox<String> campoItem, String categoria, boolean limparItem) {
        String valorAtual = limparItem ? "" : obterTextoCombo(campoItem);

        campoItem.removeAllItems();
        campoItem.addItem("");

        for (TipoDoacao tipo : tipoDoacaoService.listar()) {
            if (tipo.getCategoria() != null &&
                    tipo.getCategoria().name().equalsIgnoreCase(categoria)) {
                campoItem.addItem(tipo.getNome());
            }
        }

        campoItem.setSelectedItem(valorAtual);
    }

    private String obterTextoCombo(JComboBox<String> combo) {
        Object valor;

        if (combo.isEditable()) {
            valor = combo.getEditor().getItem();
        } else {
            valor = combo.getSelectedItem();
        }

        return valor == null ? "" : valor.toString().trim();
    }

    private void exportarRelatorioCSV() {
        if (modeloTabela.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Não há dados para exportar.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar relatório de doações");
        fileChooser.setSelectedFile(new File("relatorio_doacoes.csv"));

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
                    "Relatório gerado com sucesso:\n" + arquivo.getAbsolutePath()
            );

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao gerar relatório:\n" + e.getMessage()
            );
        }
    }
}
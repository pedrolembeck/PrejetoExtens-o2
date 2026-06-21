package com.doacoes.view;

import com.doacoes.model.Doacao;
import com.doacoes.model.TamanhoRoupa;
import com.doacoes.model.TipoDoacao;
import com.doacoes.service.DoacaoService;
import com.doacoes.service.TamanhoRoupaService;
import com.doacoes.service.TipoDoacaoService;

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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DoacaoView extends JFrame {

    private final DoacaoService service = new DoacaoService();
    private final TamanhoRoupaService tamanhoRoupaService = new TamanhoRoupaService();
    private final TipoDoacaoService tipoDoacaoService = new TipoDoacaoService();

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JTextField campoPesquisa;

    public DoacaoView() {
        service.inicializar();
        tamanhoRoupaService.inicializar();
        configurarJanela();
        construirInterface();
        carregarTabela();
    }

    private void configurarJanela() {
        setTitle("Solidário - Doações");
        setSize(1220, 680);
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

        JLabel titulo = new JLabel("Cadastro de Doações");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Registre, consulte e acompanhe as doações recebidas.");
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

        card.add(criarTopoCard(), BorderLayout.NORTH);
        card.add(criarTabela(), BorderLayout.CENTER);

        painel.add(card, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarTopoCard() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel tituloLista = new JLabel("Doações cadastradas");
        tituloLista.setFont(new Font("Arial", Font.BOLD, 18));
        tituloLista.setForeground(new Color(44, 62, 80));

        JLabel descricaoLista = new JLabel("Pesquise, visualize e gerencie as doações registradas no sistema.");
        descricaoLista.setFont(new Font("Arial", Font.PLAIN, 13));
        descricaoLista.setForeground(new Color(100, 116, 139));

        textos.add(tituloLista);
        textos.add(Box.createVerticalStrut(4));
        textos.add(descricaoLista);

        JPanel pesquisa = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pesquisa.setOpaque(false);

        campoPesquisa = new JTextField();
        campoPesquisa.setPreferredSize(new Dimension(310, 36));
        campoPesquisa.setFont(new Font("Arial", Font.PLAIN, 13));
        campoPesquisa.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                new EmptyBorder(7, 10, 7, 10)
        ));

        JButton btnPesquisar = criarBotaoPequeno("Pesquisar", new Color(41, 128, 185));
        JButton btnLimpar = criarBotaoPequeno("Limpar", new Color(127, 140, 141));

        btnPesquisar.addActionListener(e -> carregarTabelaPesquisa(campoPesquisa.getText()));

        btnLimpar.addActionListener(e -> {
            campoPesquisa.setText("");
            carregarTabela();
        });

        campoPesquisa.addActionListener(e -> carregarTabelaPesquisa(campoPesquisa.getText()));

        pesquisa.add(campoPesquisa);
        pesquisa.add(btnPesquisar);
        pesquisa.add(btnLimpar);

        topo.add(textos, BorderLayout.WEST);
        topo.add(pesquisa, BorderLayout.EAST);

        return topo;
    }

    private JScrollPane criarTabela() {
        String[] colunas = {
                "ID", "Doador", "Item", "Qtd.", "Unidade",
                "Detalhe", "Categoria", "Tamanho", "Data", "Observação"
        };

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

        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(new Color(44, 62, 80));
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.getTableHeader().setPreferredSize(new Dimension(0, 36));
        tabela.getTableHeader().setReorderingAllowed(false);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(55);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(160);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(160);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(70);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(90);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(6).setPreferredWidth(110);
        tabela.getColumnModel().getColumn(7).setPreferredWidth(130);
        tabela.getColumnModel().getColumn(8).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(9).setPreferredWidth(210);

        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);

        tabela.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(3).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(4).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(6).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(7).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(8).setCellRenderer(centralizado);

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
        JButton btnAlterar = criarBotao("Alterar", new Color(41, 128, 185));
        JButton btnDeletar = criarBotao("Deletar", new Color(192, 57, 43));
        JButton btnAtualizar = criarBotao("Atualizar", new Color(127, 140, 141));
        JButton btnRelatorio = criarBotao("Relatório CSV", new Color(52, 73, 94));

        btnCadastrar.addActionListener(e -> cadastrar());
        btnAlterar.addActionListener(e -> alterar());
        btnDeletar.addActionListener(e -> deletar());
        btnAtualizar.addActionListener(e -> carregarTabela());
        btnRelatorio.addActionListener(e -> exportarRelatorioCSV());

        rodape.add(btnCadastrar);
        rodape.add(btnAlterar);
        rodape.add(btnDeletar);
        rodape.add(btnAtualizar);
        rodape.add(btnRelatorio);

        return rodape;
    }

    private JButton criarBotao(String texto, Color cor) {
        RoundedButton btn = new RoundedButton(texto, cor);
        btn.setPreferredSize(new Dimension(145, 36));
        return btn;
    }

    private JButton criarBotaoPequeno(String texto, Color cor) {
        RoundedButton btn = new RoundedButton(texto, cor);
        btn.setPreferredSize(new Dimension(105, 36));
        return btn;
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
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma doação para alterar.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linha);
        Long id = Long.valueOf(modeloTabela.getValueAt(linhaModelo, 0).toString());

        Doacao doacaoExistente = service.buscarPorId(id);

        if (doacaoExistente == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Doação não encontrada.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
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
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma doação para deletar.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linha);
        Long id = Long.valueOf(modeloTabela.getValueAt(linhaModelo, 0).toString());
        String doador = String.valueOf(modeloTabela.getValueAt(linhaModelo, 1));
        String item = String.valueOf(modeloTabela.getValueAt(linhaModelo, 2));

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Confirma a exclusão da doação abaixo?\n\n" +
                        "ID: " + id + "\n" +
                        "Doador: " + doador + "\n" +
                        "Item: " + item,
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            service.deletar(id);
            JOptionPane.showMessageDialog(this, "Doação deletada com sucesso!");
            carregarTabela();
        }
    }

    private Doacao abrirFormulario(Doacao doacao) {
        boolean editando = doacao != null;
        final Doacao[] resultado = new Doacao[1];

        JDialog dialog = new JDialog(
                this,
                editando ? "Alterar Doação" : "Cadastrar Doação",
                true
        );

        dialog.setSize(620, 720);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(241, 245, 249));

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(editando ? new Color(41, 128, 185) : new Color(39, 174, 96));
        cabecalho.setBorder(new EmptyBorder(18, 25, 18, 25));

        JLabel titulo = new JLabel(editando ? "Alterar Doação" : "Nova Doação");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);

        cabecalho.add(titulo, BorderLayout.WEST);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(16, 30, 16, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        JTextField campoDoador = new JTextField();
        estilizarCampoTexto(campoDoador);

        JComboBox<String> campoItem = new JComboBox<>();
        campoItem.setEditable(true);
        estilizarCombo(campoItem);

        JTextField campoQuantidade = new JTextField();
        estilizarCampoTexto(campoQuantidade);

        JComboBox<String> campoUnidade = new JComboBox<>(new String[]{
                "UNIDADE", "KG", "PACOTE", "LITRO", "CAIXA", "CESTA"
        });
        estilizarCombo(campoUnidade);

        JTextField campoDetalheUnidade = new JTextField();
        estilizarCampoTexto(campoDetalheUnidade);

        JComboBox<String> campoCategoria = new JComboBox<>(new String[]{
                "ALIMENTOS", "VESTUARIO"
        });
        estilizarCombo(campoCategoria);

        JComboBox<String> campoTamanho = new JComboBox<>();
        carregarTamanhos(campoTamanho);
        estilizarCombo(campoTamanho);

        JTextField campoData = new JTextField(LocalDate.now().toString());
        estilizarCampoTexto(campoData);

        JTextField campoObservacao = new JTextField();
        estilizarCampoTexto(campoObservacao);

        if (editando) {
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

            campoDetalheUnidade.setText(doacao.getDetalheUnidade() == null ? "" : doacao.getDetalheUnidade());
            campoTamanho.setSelectedItem(doacao.getTamanho() == null ? "" : doacao.getTamanho());
            campoData.setText(String.valueOf(doacao.getDataDoacao()));
            campoObservacao.setText(doacao.getObservacao() == null ? "" : doacao.getObservacao());
        } else {
            carregarItensPorCategoria(campoItem, "ALIMENTOS", false);
        }

        atualizarCamposPorCategoria(campoCategoria, campoTamanho, campoUnidade, campoDetalheUnidade);

        campoCategoria.addActionListener(e -> {
            String categoriaSelecionada = campoCategoria.getSelectedItem().toString();

            carregarItensPorCategoria(campoItem, categoriaSelecionada, true);
            atualizarCamposPorCategoria(campoCategoria, campoTamanho, campoUnidade, campoDetalheUnidade);
        });

        adicionarCampo(form, gbc, 0, "Doador", campoDoador);
        adicionarCampo(form, gbc, 2, "Item / Tipo de Doação", campoItem);
        adicionarCampo(form, gbc, 4, "Quantidade", campoQuantidade);
        adicionarCampo(form, gbc, 6, "Unidade", campoUnidade);
        adicionarCampo(form, gbc, 8, "Detalhe da unidade", campoDetalheUnidade);
        adicionarCampo(form, gbc, 10, "Categoria", campoCategoria);
        adicionarCampo(form, gbc, 12, "Tamanho", campoTamanho);
        adicionarCampo(form, gbc, 14, "Data da doação", campoData);
        adicionarCampo(form, gbc, 16, "Observação", campoObservacao);

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
            Doacao novaDoacao = montarDoacaoFormulario(
                    dialog,
                    doacao,
                    campoDoador,
                    campoItem,
                    campoQuantidade,
                    campoUnidade,
                    campoDetalheUnidade,
                    campoCategoria,
                    campoTamanho,
                    campoData,
                    campoObservacao
            );

            if (novaDoacao != null) {
                resultado[0] = novaDoacao;
                dialog.dispose();
            }
        });

        rodapeDialog.add(btnCancelar);
        rodapeDialog.add(btnSalvar);

        dialog.add(cabecalho, BorderLayout.NORTH);
        dialog.add(scrollForm, BorderLayout.CENTER);
        dialog.add(rodapeDialog, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(() -> campoDoador.requestFocus());

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

    private Doacao montarDoacaoFormulario(
            JDialog dialog,
            Doacao doacaoEdicao,
            JTextField campoDoador,
            JComboBox<String> campoItem,
            JTextField campoQuantidade,
            JComboBox<String> campoUnidade,
            JTextField campoDetalheUnidade,
            JComboBox<String> campoCategoria,
            JComboBox<String> campoTamanho,
            JTextField campoData,
            JTextField campoObservacao
    ) {
        try {
            String doador = campoDoador.getText().trim();
            String itemSelecionado = obterTextoCombo(campoItem).toUpperCase();
            String categoriaSelecionada = campoCategoria.getSelectedItem().toString();
            String unidadeSelecionada = campoUnidade.getSelectedItem().toString();
            String detalheUnidade = campoDetalheUnidade.getText().trim().toUpperCase();

            String tamanhoSelecionado = campoTamanho.getSelectedItem() == null
                    ? ""
                    : campoTamanho.getSelectedItem().toString();

            if (doador.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Informe o nome do doador.", "Atenção", JOptionPane.WARNING_MESSAGE);
                campoDoador.requestFocus();
                return null;
            }

            if (itemSelecionado.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Informe o item da doação.", "Atenção", JOptionPane.WARNING_MESSAGE);
                campoItem.requestFocus();
                return null;
            }

            int quantidade = Integer.parseInt(campoQuantidade.getText().trim());

            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(dialog, "A quantidade deve ser maior que zero.", "Atenção", JOptionPane.WARNING_MESSAGE);
                campoQuantidade.requestFocus();
                return null;
            }

            if (!"VESTUARIO".equals(categoriaSelecionada)) {
                tamanhoSelecionado = "";
            }

            if ("VESTUARIO".equals(categoriaSelecionada)) {
                unidadeSelecionada = "UNIDADE";
                detalheUnidade = "";

                if (tamanhoSelecionado.isBlank()) {
                    JOptionPane.showMessageDialog(dialog, "Informe o tamanho da roupa.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    campoTamanho.requestFocus();
                    return null;
                }
            }

            Doacao novaDoacao = new Doacao();

            if (doacaoEdicao != null) {
                novaDoacao.setId(doacaoEdicao.getId());
            }

            novaDoacao.setNomeDoador(doador);
            novaDoacao.setDescricaoItem(itemSelecionado);
            novaDoacao.setQuantidade(quantidade);
            novaDoacao.setUnidade(unidadeSelecionada);
            novaDoacao.setDetalheUnidade(detalheUnidade);
            novaDoacao.setCategoria(categoriaSelecionada);
            novaDoacao.setTamanho(tamanhoSelecionado);
            novaDoacao.setDataDoacao(LocalDate.parse(campoData.getText().trim()));
            novaDoacao.setObservacao(campoObservacao.getText().trim());

            return novaDoacao;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(dialog, "Quantidade deve ser um número inteiro.", "Atenção", JOptionPane.WARNING_MESSAGE);
            campoQuantidade.requestFocus();
            return null;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(dialog, "Data inválida. Use o formato AAAA-MM-DD. Exemplo: 2026-06-02.", "Atenção", JOptionPane.WARNING_MESSAGE);
            campoData.requestFocus();
            return null;
        }
    }

    private JButton criarBotaoDialog(String texto, Color cor) {
        RoundedButton btn = new RoundedButton(texto, cor);
        btn.setPreferredSize(new Dimension(150, 36));
        return btn;
    }

    private void estilizarCampoTexto(JTextField campo) {
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(430, 36));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                new EmptyBorder(7, 12, 7, 12)
        ));
        campo.setBackground(new Color(250, 250, 250));
        campo.setForeground(new Color(44, 62, 80));
    }

    private void estilizarCombo(JComboBox<String> combo) {
        combo.setFont(new Font("Arial", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(430, 36));
        combo.setBackground(Color.WHITE);
        combo.setForeground(new Color(44, 62, 80));
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
                    "Erro ao gerar relatório:\n" + e.getMessage(),
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
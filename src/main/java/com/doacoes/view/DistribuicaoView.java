package com.doacoes.view;

import com.doacoes.model.Distribuicao;
import com.doacoes.model.Estoque;
import com.doacoes.model.Receptor;
import com.doacoes.service.DistribuicaoService;
import com.doacoes.service.EstoqueService;
import com.doacoes.service.ReceptorService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public class DistribuicaoView extends JFrame {

    private final ReceptorService receptorService = new ReceptorService();
    private final DistribuicaoService service = new DistribuicaoService();
    private final EstoqueService estoqueService = new EstoqueService();

    private JTable tabela;
    private DefaultTableModel modelo;

    public DistribuicaoView() {
        receptorService.inicializar();
        estoqueService.inicializar();
        service.inicializar();

        configurarJanela();
        montarTela();
        carregarTabela();
    }

    private void configurarJanela() {
        setTitle("Solidário - Distribuição");
        setSize(1080, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void montarTela() {
        JPanel fundo = new JPanel(new BorderLayout());
        fundo.setBackground(new Color(241, 245, 249));
        setContentPane(fundo);

        fundo.add(criarCabecalho(), BorderLayout.NORTH);
        fundo.add(criarConteudo(), BorderLayout.CENTER);
        fundo.add(criarBotoes(), BorderLayout.SOUTH);
    }

    private JPanel criarCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(new Color(192, 57, 43));
        cabecalho.setBorder(new EmptyBorder(24, 35, 24, 35));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Distribuição de Doações");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Registre a entrega de itens do estoque para os receptores cadastrados.");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(245, 245, 245));

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

        JLabel tituloLista = new JLabel("Histórico de distribuições");
        tituloLista.setFont(new Font("Arial", Font.BOLD, 18));
        tituloLista.setForeground(new Color(44, 62, 80));

        JLabel descricaoLista = new JLabel("Acompanhe quais itens foram entregues, para quem e em qual quantidade.");
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
        card.add(criarTabelaPainel(), BorderLayout.CENTER);

        painel.add(card, BorderLayout.CENTER);

        return painel;
    }

    private JScrollPane criarTabelaPainel() {
        String[] colunas = {"ID", "Receptor", "CPF", "Item", "Quantidade", "Data"};

        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setRowHeight(34);
        tabela.setFont(new Font("Arial", Font.PLAIN, 13));
        tabela.setForeground(new Color(44, 62, 80));
        tabela.setSelectionBackground(new Color(245, 198, 190));
        tabela.setSelectionForeground(new Color(44, 62, 80));
        tabela.setGridColor(new Color(230, 235, 240));
        tabela.setShowVerticalLines(false);
        tabela.setFillsViewportHeight(true);
        tabela.setAutoCreateRowSorter(true);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(260);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(130);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(300);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(180);

        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(new Color(44, 62, 80));
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.getTableHeader().setPreferredSize(new Dimension(0, 36));
        tabela.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);

        tabela.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(2).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(4).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(5).setCellRenderer(centralizado);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        scroll.getViewport().setBackground(Color.WHITE);

        return scroll;
    }

    private JPanel criarBotoes() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 14));
        rodape.setBackground(Color.WHITE);
        rodape.setBorder(new EmptyBorder(0, 35, 0, 35));

        JButton btnCadastrar = criarBotao("Distribuir", new Color(39, 174, 96));
        JButton btnEditar = criarBotao("Editar", new Color(41, 128, 185));
        JButton btnExcluir = criarBotao("Excluir", new Color(192, 57, 43));
        JButton btnAtualizar = criarBotao("Atualizar", new Color(127, 140, 141));
        JButton btnRelatorio = criarBotao("Relatório CSV", new Color(52, 73, 94));

        btnCadastrar.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> editarSelecionado());
        btnExcluir.addActionListener(e -> excluirSelecionado());
        btnAtualizar.addActionListener(e -> carregarTabela());
        btnRelatorio.addActionListener(e -> exportarRelatorioCSV());

        rodape.add(btnCadastrar);
        rodape.add(btnEditar);
        rodape.add(btnExcluir);
        rodape.add(btnAtualizar);
        rodape.add(btnRelatorio);

        return rodape;
    }

    private JButton criarBotao(String texto, Color cor) {
        RoundedButton btn = new RoundedButton(texto, cor);
        btn.setPreferredSize(new Dimension(150, 36));
        return btn;
    }

    private void carregarTabela() {
        modelo.setRowCount(0);

        for (Distribuicao d : service.listar()) {
            modelo.addRow(new Object[]{
                    d.getId(),
                    d.getNomeReceptor(),
                    d.getCpfReceptor(),
                    d.getDescricaoEstoque(),
                    d.getQuantidade(),
                    d.getData()
            });
        }
    }

    private void carregarEstoque(JComboBox<Estoque> combo) {
        combo.removeAllItems();

        for (Estoque estoque : estoqueService.listar()) {
            if (estoque.getQuantidade() >= 1) {
                combo.addItem(estoque);
            }
        }

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Estoque) {
                    Estoque estoque = (Estoque) value;
                    setText(estoque.getDescricao() + " - Disponível: " + estoque.getQuantidade());
                }

                return this;
            }
        });
    }

    private void abrirFormulario(Distribuicao distribuicao) {
        boolean editando = distribuicao != null;

        JDialog dialog = new JDialog(
                this,
                editando ? "Editar Distribuição" : "Nova Distribuição",
                true
        );

        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(241, 245, 249));

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(editando ? new Color(41, 128, 185) : new Color(192, 57, 43));
        cabecalho.setBorder(new EmptyBorder(18, 25, 18, 25));

        JLabel titulo = new JLabel(editando ? "Editar Distribuição" : "Nova Distribuição");
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

        JTextField campoCpf = new JTextField();
        estilizarCampo(campoCpf);

        JTextField campoNomeReceptor = new JTextField();
        estilizarCampo(campoNomeReceptor);
        campoNomeReceptor.setEditable(false);
        campoNomeReceptor.setBackground(new Color(235, 245, 245));

        JTextField campoQuantidade = new JTextField();
        estilizarCampo(campoQuantidade);

        JComboBox<Estoque> comboEstoque = new JComboBox<>();
        comboEstoque.setFont(new Font("Arial", Font.PLAIN, 14));
        comboEstoque.setPreferredSize(new Dimension(390, 38));
        comboEstoque.setBackground(Color.WHITE);

        carregarEstoque(comboEstoque);

        final Receptor[] receptorSelecionado = new Receptor[1];

        JButton btnBuscarReceptor = criarBotaoDialog("Buscar", new Color(52, 73, 94));
        btnBuscarReceptor.addActionListener(e -> {
            Receptor receptor = abrirSelecaoReceptor(dialog, campoCpf.getText().trim());

            if (receptor != null) {
                receptorSelecionado[0] = receptor;
                campoCpf.setText(receptor.getCpf());
                campoNomeReceptor.setText(receptor.getNome());
            }
        });

        JPanel painelCpf = new JPanel(new BorderLayout(10, 0));
        painelCpf.setOpaque(false);
        painelCpf.add(campoCpf, BorderLayout.CENTER);
        painelCpf.add(btnBuscarReceptor, BorderLayout.EAST);

        if (editando) {
            campoCpf.setText(distribuicao.getCpfReceptor());
            campoNomeReceptor.setText(distribuicao.getNomeReceptor());
            campoQuantidade.setText(String.valueOf(distribuicao.getQuantidade()));
            receptorSelecionado[0] = receptorService.buscarPorId(distribuicao.getReceptorId());

            selecionarEstoque(comboEstoque, distribuicao.getEstoqueId());
        }

        adicionarCampo(form, gbc, 0, "CPF do Receptor ou busca pelo cadastro", painelCpf);
        adicionarCampo(form, gbc, 2, "Nome do Receptor", campoNomeReceptor);
        adicionarCampo(form, gbc, 4, "Item do Estoque", comboEstoque);
        adicionarCampo(form, gbc, 6, "Quantidade", campoQuantidade);

        JPanel rodapeDialog = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        rodapeDialog.setBackground(Color.WHITE);
        rodapeDialog.setBorder(new EmptyBorder(0, 20, 10, 20));

        JButton btnCancelar = criarBotaoDialog("Cancelar", new Color(127, 140, 141));
        JButton btnSalvar = criarBotaoDialog(
                editando ? "Salvar Alterações" : "Distribuir",
                editando ? new Color(41, 128, 185) : new Color(39, 174, 96)
        );

        btnCancelar.addActionListener(e -> dialog.dispose());

        btnSalvar.addActionListener(e -> {
            try {
                if (receptorSelecionado[0] == null) {
                    Receptor receptor = abrirSelecaoReceptor(dialog, campoCpf.getText().trim());

                    if (receptor == null) {
                        return;
                    }

                    receptorSelecionado[0] = receptor;
                    campoCpf.setText(receptor.getCpf());
                    campoNomeReceptor.setText(receptor.getNome());
                }

                Estoque item = (Estoque) comboEstoque.getSelectedItem();

                if (item == null) {
                    JOptionPane.showMessageDialog(dialog, "Selecione um item do estoque.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int quantidade;

                try {
                    quantidade = Integer.parseInt(campoQuantidade.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Quantidade inválida.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    campoQuantidade.requestFocus();
                    return;
                }

                if (quantidade <= 0) {
                    JOptionPane.showMessageDialog(dialog, "A quantidade deve ser maior que zero.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    campoQuantidade.requestFocus();
                    return;
                }

                if (!editando && quantidade > item.getQuantidade()) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Quantidade maior que o estoque disponível.\nDisponível: " + item.getQuantidade(),
                            "Estoque insuficiente",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (editando) {
                    Distribuicao alterada = new Distribuicao();
                    alterada.setId(distribuicao.getId());
                    alterada.setReceptorId(receptorSelecionado[0].getId());
                    alterada.setEstoqueId(item.getId());
                    alterada.setQuantidade(quantidade);
                    alterada.setData(LocalDateTime.now());

                    service.atualizar(alterada);

                    JOptionPane.showMessageDialog(dialog, "Distribuição atualizada com sucesso!");
                } else {
                    boolean sucesso = service.distribuir(
                            item.getId(),
                            receptorSelecionado[0].getId(),
                            quantidade
                    );

                    if (!sucesso) {
                        JOptionPane.showMessageDialog(
                                dialog,
                                "Não foi possível realizar a distribuição.",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    JOptionPane.showMessageDialog(dialog, "Distribuição realizada com sucesso!");
                }

                carregarTabela();
                dialog.dispose();

            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        ex.getMessage(),
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Erro inesperado: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        rodapeDialog.add(btnCancelar);
        rodapeDialog.add(btnSalvar);

        dialog.add(cabecalho, BorderLayout.NORTH);
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(rodapeDialog, BorderLayout.SOUTH);

        campoCpf.addActionListener(e -> btnBuscarReceptor.doClick());

        SwingUtilities.invokeLater(() -> campoCpf.requestFocus());

        dialog.setVisible(true);
    }

    private Receptor abrirSelecaoReceptor(Component parent, String termoInicial) {
        final Receptor[] receptorSelecionado = new Receptor[1];

        JDialog dialog = new JDialog(this, "Selecionar Receptor", true);
        dialog.setSize(760, 480);
        dialog.setLocationRelativeTo(parent);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(241, 245, 249));

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(new Color(52, 73, 94));
        cabecalho.setBorder(new EmptyBorder(18, 25, 18, 25));

        JLabel titulo = new JLabel("Buscar receptor cadastrado");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Pesquise por CPF, nome, telefone, e-mail ou endereço.");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(235, 245, 245));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(subtitulo);

        cabecalho.add(textos, BorderLayout.WEST);

        JPanel conteudo = new JPanel(new BorderLayout(0, 14));
        conteudo.setBackground(Color.WHITE);
        conteudo.setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel painelBusca = new JPanel(new BorderLayout(10, 0));
        painelBusca.setOpaque(false);

        JTextField campoBusca = new JTextField();
        estilizarCampo(campoBusca);
        campoBusca.setText(termoInicial == null ? "" : termoInicial);

        JButton btnPesquisar = criarBotaoDialog("Pesquisar", new Color(52, 73, 94));

        painelBusca.add(campoBusca, BorderLayout.CENTER);
        painelBusca.add(btnPesquisar, BorderLayout.EAST);

        String[] colunas = {"ID", "Nome", "CPF", "Telefone", "E-mail", "Endereço"};

        DefaultTableModel modeloReceptor = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabelaReceptor = new JTable(modeloReceptor);
        tabelaReceptor.setRowHeight(32);
        tabelaReceptor.setFont(new Font("Arial", Font.PLAIN, 13));
        tabelaReceptor.setForeground(new Color(44, 62, 80));
        tabelaReceptor.setSelectionBackground(new Color(209, 242, 235));
        tabelaReceptor.setSelectionForeground(new Color(44, 62, 80));
        tabelaReceptor.setGridColor(new Color(230, 235, 240));
        tabelaReceptor.setShowVerticalLines(false);
        tabelaReceptor.setAutoCreateRowSorter(true);

        tabelaReceptor.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabelaReceptor.getTableHeader().setBackground(new Color(44, 62, 80));
        tabelaReceptor.getTableHeader().setForeground(Color.WHITE);
        tabelaReceptor.getTableHeader().setPreferredSize(new Dimension(0, 34));
        tabelaReceptor.getTableHeader().setReorderingAllowed(false);

        tabelaReceptor.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaReceptor.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabelaReceptor.getColumnModel().getColumn(2).setPreferredWidth(115);
        tabelaReceptor.getColumnModel().getColumn(3).setPreferredWidth(115);
        tabelaReceptor.getColumnModel().getColumn(4).setPreferredWidth(150);
        tabelaReceptor.getColumnModel().getColumn(5).setPreferredWidth(190);

        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);
        tabelaReceptor.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabelaReceptor.getColumnModel().getColumn(2).setCellRenderer(centralizado);
        tabelaReceptor.getColumnModel().getColumn(3).setCellRenderer(centralizado);

        JScrollPane scroll = new JScrollPane(tabelaReceptor);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        scroll.getViewport().setBackground(Color.WHITE);

        conteudo.add(painelBusca, BorderLayout.NORTH);
        conteudo.add(scroll, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        rodape.setBackground(Color.WHITE);
        rodape.setBorder(new EmptyBorder(0, 20, 10, 20));

        JButton btnCancelar = criarBotaoDialog("Cancelar", new Color(127, 140, 141));
        JButton btnSelecionar = criarBotaoDialog("Selecionar", new Color(39, 174, 96));

        btnCancelar.addActionListener(e -> dialog.dispose());

        btnSelecionar.addActionListener(e -> {
            Receptor receptor = obterReceptorSelecionadoNaTabela(tabelaReceptor, modeloReceptor, dialog);

            if (receptor != null) {
                receptorSelecionado[0] = receptor;
                dialog.dispose();
            }
        });

        btnPesquisar.addActionListener(e -> carregarTabelaReceptores(modeloReceptor, campoBusca.getText().trim()));

        campoBusca.addActionListener(e -> btnPesquisar.doClick());

        tabelaReceptor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Receptor receptor = obterReceptorSelecionadoNaTabela(tabelaReceptor, modeloReceptor, dialog);

                    if (receptor != null) {
                        receptorSelecionado[0] = receptor;
                        dialog.dispose();
                    }
                }
            }
        });

        rodape.add(btnCancelar);
        rodape.add(btnSelecionar);

        dialog.add(cabecalho, BorderLayout.NORTH);
        dialog.add(conteudo, BorderLayout.CENTER);
        dialog.add(rodape, BorderLayout.SOUTH);

        carregarTabelaReceptores(modeloReceptor, campoBusca.getText().trim());

        SwingUtilities.invokeLater(() -> campoBusca.requestFocus());

        dialog.setVisible(true);

        return receptorSelecionado[0];
    }

    private void carregarTabelaReceptores(DefaultTableModel modeloReceptor, String termo) {
        modeloReceptor.setRowCount(0);

        List<Receptor> receptores;

        if (termo == null || termo.isBlank()) {
            receptores = receptorService.listar();
        } else {
            receptores = receptorService.pesquisar(termo.trim());
        }

        for (Receptor receptor : receptores) {
            modeloReceptor.addRow(new Object[]{
                    receptor.getId(),
                    receptor.getNome(),
                    receptor.getCpf(),
                    receptor.getTelefone(),
                    receptor.getEmail(),
                    receptor.getEndereco()
            });
        }
    }

    private Receptor obterReceptorSelecionadoNaTabela(
            JTable tabelaReceptor,
            DefaultTableModel modeloReceptor,
            Component parent
    ) {
        int linha = tabelaReceptor.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(parent, "Selecione um receptor na tabela.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int linhaModelo = tabelaReceptor.convertRowIndexToModel(linha);
        Long id = Long.valueOf(modeloReceptor.getValueAt(linhaModelo, 0).toString());

        Receptor receptor = receptorService.buscarPorId(id);

        if (receptor == null) {
            JOptionPane.showMessageDialog(parent, "Receptor não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return receptor;
    }

    private void selecionarEstoque(JComboBox<Estoque> combo, Long estoqueId) {
        if (estoqueId == null) {
            return;
        }

        for (int i = 0; i < combo.getItemCount(); i++) {
            Estoque estoque = combo.getItemAt(i);

            if (estoque != null && estoque.getId().equals(estoqueId)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void editarSelecionado() {
        int row = tabela.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma distribuição.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(row);
        Long id = Long.valueOf(modelo.getValueAt(linhaModelo, 0).toString());

        Distribuicao distribuicao = service.buscarPorId(id);

        if (distribuicao == null) {
            JOptionPane.showMessageDialog(this, "Distribuição não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        abrirFormulario(distribuicao);
    }

    private void excluirSelecionado() {
        int row = tabela.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma distribuição.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(row);

        Long id = Long.valueOf(modelo.getValueAt(linhaModelo, 0).toString());
        String receptor = String.valueOf(modelo.getValueAt(linhaModelo, 1));
        String item = String.valueOf(modelo.getValueAt(linhaModelo, 3));
        Object quantidade = modelo.getValueAt(linhaModelo, 4);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Deseja excluir esta distribuição?\n\n" +
                        "ID: " + id + "\n" +
                        "Receptor: " + receptor + "\n" +
                        "Item: " + item + "\n" +
                        "Quantidade: " + quantidade,
                "Confirmação",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            service.deletar(id);
            JOptionPane.showMessageDialog(this, "Distribuição excluída com sucesso!");
            carregarTabela();
        }
    }

    private void exportarRelatorioCSV() {
        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Não há distribuições para exportar.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar relatório de distribuições");
        fileChooser.setSelectedFile(new File("relatorio_distribuicoes.csv"));

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
            for (int coluna = 0; coluna < modelo.getColumnCount(); coluna++) {
                writer.write(modelo.getColumnName(coluna));

                if (coluna < modelo.getColumnCount() - 1) {
                    writer.write(";");
                }
            }

            writer.newLine();

            int totalQuantidade = 0;

            for (int linha = 0; linha < modelo.getRowCount(); linha++) {
                for (int coluna = 0; coluna < modelo.getColumnCount(); coluna++) {
                    Object valor = modelo.getValueAt(linha, coluna);

                    String texto = valor == null ? "" : valor.toString();
                    texto = texto.replace(";", ",")
                            .replace("\n", " ")
                            .replace("\r", " ");

                    writer.write(texto);

                    if (coluna < modelo.getColumnCount() - 1) {
                        writer.write(";");
                    }
                }

                Object quantidade = modelo.getValueAt(linha, 4);

                if (quantidade != null) {
                    try {
                        totalQuantidade += Integer.parseInt(quantidade.toString());
                    } catch (NumberFormatException ignored) {
                    }
                }

                writer.newLine();
            }

            writer.newLine();
            writer.write("TOTAL GERAL;;;;" + totalQuantidade + ";");
            writer.newLine();

            JOptionPane.showMessageDialog(
                    this,
                    "Relatório de distribuições gerado com sucesso:\n" + arquivo.getAbsolutePath()
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
        campo.setPreferredSize(new Dimension(390, 38));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                new EmptyBorder(8, 12, 8, 12)
        ));
        campo.setBackground(new Color(250, 250, 250));
        campo.setForeground(new Color(44, 62, 80));
    }

    public void iniciar() {
        setVisible(true);
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
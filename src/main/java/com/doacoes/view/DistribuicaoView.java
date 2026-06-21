package com.doacoes.view;

import com.doacoes.model.Distribuicao;
import com.doacoes.service.DistribuicaoService;
import com.doacoes.model.Receptor;
import com.doacoes.service.ReceptorService;
import com.doacoes.model.Estoque;
import com.doacoes.service.EstoqueService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class DistribuicaoView extends JFrame {

    private final ReceptorService receptorService = new ReceptorService();
    private final DistribuicaoService service = new DistribuicaoService();
    private final EstoqueService estoqueService = new EstoqueService();

    private JTable tabela;
    private DefaultTableModel modelo;

    public DistribuicaoView() {
        service.inicializar();
        configurarJanela();
        montarTela();
        carregarTabela();
    }

    private void configurarJanela() {
        setTitle("Solidário - Distribuição");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void montarTela() {
        JPanel fundo = new JPanel(new BorderLayout());
        fundo.setBackground(new Color(241, 245, 249));
        setContentPane(fundo);

        fundo.add(criarCabecalho(), BorderLayout.NORTH);
        fundo.add(criarTabelaPainel(), BorderLayout.CENTER);
        fundo.add(criarBotoes(), BorderLayout.SOUTH);
    }

    private JPanel criarCabecalho() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(192, 57, 43));
        p.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel titulo = new JLabel("Distribuições");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Controle de entregas de doações aos receptores");
        sub.setForeground(new Color(245, 245, 245));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        textos.add(titulo);
        textos.add(sub);

        p.add(textos, BorderLayout.WEST);

        return p;
    }

    private JScrollPane criarTabelaPainel() {

        String[] colunas = {"ID", "CPF Receptor", "Estoque ID", "Quantidade", "Data"};

        modelo = new DefaultTableModel(colunas, 0);

        tabela = new JTable(modelo);
        tabela.setRowHeight(30);
        tabela.setFont(new Font("Arial", Font.PLAIN, 13));

        return new JScrollPane(tabela);
    }

    private JPanel criarBotoes() {

        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        p.setBackground(Color.WHITE);

        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar");

        btnCadastrar.setBackground(new Color(39, 174, 96));
        btnCadastrar.setForeground(Color.WHITE);

        btnEditar.setBackground(new Color(41, 128, 185));
        btnEditar.setForeground(Color.WHITE);

        btnExcluir.setBackground(new Color(192, 57, 43));
        btnExcluir.setForeground(Color.WHITE);

        btnAtualizar.setBackground(new Color(127, 140, 141));
        btnAtualizar.setForeground(Color.WHITE);

        btnCadastrar.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> editarSelecionado());
        btnExcluir.addActionListener(e -> excluirSelecionado());
        btnAtualizar.addActionListener(e -> carregarTabela());

        p.add(btnCadastrar);
        p.add(btnEditar);
        p.add(btnExcluir);
        p.add(btnAtualizar);

        return p;
    }

    private void carregarTabela() {
        modelo.setRowCount(0);

        for (Distribuicao d : service.listar()) {
            modelo.addRow(new Object[]{
                    d.getId(),
                    d.getCpfReceptor(),
                    d.getEstoqueId(),
                    d.getQuantidade(),
                    d.getData()
            });
        }
    }

    private void carregarEstoque(JComboBox<Estoque> combo) {
        combo.removeAllItems();

        for (Estoque e : estoqueService.listar()) {
            if (e.getQuantidade() >= 1) {
                combo.addItem(e);
            }
        }
    }

    private void abrirFormulario(Distribuicao d) {

        boolean editando = d != null;

        JDialog dialog = new JDialog(this,
                editando ? "Editar Distribuição" : "Nova Distribuição",
                true);

        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(5, 2, 8, 8));

        JTextField receptor = new JTextField();
        JTextField quantidade = new JTextField();

        JComboBox<Estoque> comboEstoque = new JComboBox<>();
        carregarEstoque(comboEstoque);

        dialog.add(new JLabel("CPF Receptor:"));
        dialog.add(receptor);

        dialog.add(new JLabel("Estoque:"));
        dialog.add(comboEstoque);

        dialog.add(new JLabel("Quantidade:"));
        dialog.add(quantidade);

        JButton salvar = new JButton(editando ? "Atualizar" : "Salvar");
        salvar.addActionListener(e -> {

            try {

                Distribuicao nova = new Distribuicao();

                List<Receptor> receptores =
                        receptorService.pesquisar(receptor.getText().trim());

                if (receptores.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "CPF não encontrado!");
                    return;
                }

                Estoque item = (Estoque) comboEstoque.getSelectedItem();

                if (item == null) {
                    JOptionPane.showMessageDialog(dialog, "Selecione um item do estoque!");
                    return;
                }

                int qtd;

                try {
                    qtd = Integer.parseInt(quantidade.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Quantidade inválida!");
                    return;
                }

                nova.setReceptorId(receptores.get(0).getId());
                nova.setEstoqueId(item.getId());
                nova.setQuantidade(qtd);
                nova.setData(LocalDateTime.now());

                service.distribuir(
                        nova.getEstoqueId(),
                        nova.getReceptorId(),
                        nova.getQuantidade()
                );

                JOptionPane.showMessageDialog(dialog, "Distribuição realizada!");
                carregarTabela();
                dialog.dispose();

            } catch (RuntimeException ex) {

                // 🔥 AQUI ESTÁ O TRATAMENTO REAL DO SEU ERRO DO ESTOQUE
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

        dialog.add(new JLabel());
        dialog.add(salvar);

        dialog.setVisible(true);
    }

    private void editarSelecionado() {

        int row = tabela.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma distribuição.");
            return;
        }

        Long id = Long.valueOf(modelo.getValueAt(row, 0).toString());
        Distribuicao d = service.buscarPorId(id);

        abrirFormulario(d);
    }

    private void excluirSelecionado() {

        int row = tabela.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma distribuição.");
            return;
        }

        Long id = Long.valueOf(modelo.getValueAt(row, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Deseja excluir esta distribuição?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            service.deletar(id);
            carregarTabela();
        }
    }

    public void iniciar() {
        setVisible(true);
    }
}
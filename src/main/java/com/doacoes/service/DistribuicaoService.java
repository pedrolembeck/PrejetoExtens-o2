package com.doacoes.service;

import com.doacoes.dao.DistribuicaoDAO;
import com.doacoes.dao.EstoqueDAO;
import com.doacoes.model.Distribuicao;

import java.time.LocalDateTime;
import java.util.List;

public class DistribuicaoService {

    private final DistribuicaoDAO dao = new DistribuicaoDAO();
    private final EstoqueDAO estoqueDAO = new EstoqueDAO();

    public void inicializar() {
        dao.criarTabela();
    }

    // ====== DISTRIBUIR (regra de negócio) ======
    public boolean distribuir(Long estoqueId, Long receptorId, int quantidade) {
        try {

            estoqueDAO.removerQuantidade(estoqueId, quantidade);

            Distribuicao d = new Distribuicao();
            d.setEstoqueId(estoqueId);
            d.setReceptorId(receptorId);
            d.setQuantidade(quantidade);
            d.setData(LocalDateTime.now());
            System.out.println(">>> SALVANDO DISTRIBUICAO");
            System.out.println("Receptor: " + d.getReceptorId());
            System.out.println("Estoque: " + d.getEstoqueId());
            System.out.println("Qtd: " + d.getQuantidade());
            System.out.println("Data: " + d.getData());

            dao.salvar(d);

            return true;

        } catch (Exception e) {
            e.printStackTrace(); // << ESSENCIAL
            return false;
        }
    }

    // ====== CRUD PADRÃO ======
    public List<Distribuicao> listar() {
        return dao.listar();
    }

    public void salvar(Distribuicao d) {
        dao.salvar(d);
    }

    public void atualizar(Distribuicao d) {
        dao.atualizar(d);
    }

    public void deletar(Long id) {
        dao.deletar(id);
    }
    private final ReceptorService receptorService = new ReceptorService();

    public Distribuicao buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }
 }

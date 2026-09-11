package com.doacoes.service;

import com.doacoes.dto.DistribuicaoRequestDTO;
import com.doacoes.entity.Distribuicao;
import com.doacoes.entity.Estoque;
import com.doacoes.repository.DistribuicaoRepository;
import com.doacoes.repository.EstoqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DistribuicaoService {

    private final DistribuicaoRepository distribuicaoRepository;
    private final EstoqueRepository estoqueRepository;

    public DistribuicaoService(
            DistribuicaoRepository distribuicaoRepository,
            EstoqueRepository estoqueRepository) {

        this.distribuicaoRepository = distribuicaoRepository;
        this.estoqueRepository = estoqueRepository;
    }

    // =========================================================
    // CREATE
    // Cria uma distribuição e retira a quantidade do estoque
    // =========================================================

    @Transactional
    public Distribuicao salvar(DistribuicaoRequestDTO dto) {

        Estoque estoque = estoqueRepository.findById(dto.getEstoqueId())
                .orElseThrow(() ->
                        new RuntimeException("Estoque não encontrado"));

        if (estoque.getQuantidade() < dto.getQuantidade()) {
            throw new RuntimeException(
                    "Quantidade insuficiente no estoque");
        }

        estoque.setQuantidade(
                estoque.getQuantidade() - dto.getQuantidade()
        );

        estoqueRepository.save(estoque);

        Distribuicao distribuicao = new Distribuicao();

        distribuicao.setEstoqueId(dto.getEstoqueId());
        distribuicao.setReceptorId(dto.getReceptorId());
        distribuicao.setQuantidade(dto.getQuantidade());
        distribuicao.setData(LocalDateTime.now());

        return distribuicaoRepository.save(distribuicao);
    }

    // =========================================================
    // READ - LISTAR
    // Não altera o estoque
    // =========================================================

    public List<Distribuicao> listar() {
        return distribuicaoRepository.findAll();
    }

    // =========================================================
    // READ - BUSCAR POR ID
    // Não altera o estoque
    // =========================================================

    public Distribuicao buscarPorId(Long id) {

        return distribuicaoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Distribuição não encontrada"));
    }

    // =========================================================
    // UPDATE
    //
    // Primeiro devolve ao estoque a quantidade antiga.
    // Depois retira a nova quantidade.
    // =========================================================

    @Transactional
    public Distribuicao atualizar(
            Long id,
            DistribuicaoRequestDTO dto) {

        Distribuicao distribuicao =
                distribuicaoRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Distribuição não encontrada"));

        Estoque estoqueAntigo =
                estoqueRepository.findById(
                        distribuicao.getEstoqueId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Estoque antigo não encontrado"));

        // Devolve a quantidade antiga ao estoque
        estoqueAntigo.setQuantidade(
                estoqueAntigo.getQuantidade()
                        + distribuicao.getQuantidade()
        );

        estoqueRepository.save(estoqueAntigo);

        // Busca o estoque que será utilizado
        Estoque novoEstoque =
                estoqueRepository.findById(
                        dto.getEstoqueId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Novo estoque não encontrado"));

        // Verifica se existe quantidade suficiente
        if (novoEstoque.getQuantidade()
                < dto.getQuantidade()) {

            throw new RuntimeException(
                    "Quantidade insuficiente no estoque");
        }

        // Retira a nova quantidade
        novoEstoque.setQuantidade(
                novoEstoque.getQuantidade()
                        - dto.getQuantidade()
        );

        estoqueRepository.save(novoEstoque);

        // Atualiza os dados da distribuição
        distribuicao.setEstoqueId(dto.getEstoqueId());
        distribuicao.setReceptorId(dto.getReceptorId());
        distribuicao.setQuantidade(dto.getQuantidade());

        return distribuicaoRepository.save(distribuicao);
    }

    // =========================================================
    // DELETE
    // Ao excluir, devolve a quantidade para o estoque
    // =========================================================

    @Transactional
    public void deletar(Long id) {

        Distribuicao distribuicao =
                distribuicaoRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Distribuição não encontrada"));

        Estoque estoque =
                estoqueRepository.findById(
                        distribuicao.getEstoqueId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Estoque não encontrado"));

        // Devolve a quantidade para o estoque
        estoque.setQuantidade(
                estoque.getQuantidade()
                        + distribuicao.getQuantidade()
        );

        estoqueRepository.save(estoque);

        // Exclui a distribuição
        distribuicaoRepository.delete(distribuicao);
    }
}
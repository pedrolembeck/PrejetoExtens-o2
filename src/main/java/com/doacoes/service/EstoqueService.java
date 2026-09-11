package com.doacoes.service;

import com.doacoes.dto.EstoqueRequestDTO;
import com.doacoes.entity.Estoque;
import com.doacoes.repository.EstoqueRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;

    public EstoqueService(
            EstoqueRepository estoqueRepository) {

        this.estoqueRepository = estoqueRepository;
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Transactional
    public Estoque salvar(
            EstoqueRequestDTO dto) {

        Estoque estoque = new Estoque();

        preencherEstoque(
                estoque,
                dto
        );

        return estoqueRepository.save(
                estoque
        );
    }

    // =====================================================
    // READ - LISTAR
    // =====================================================

    public List<Estoque> listar() {

        return estoqueRepository.findAll();
    }

    // =====================================================
    // READ - BUSCAR POR ID
    // =====================================================

    public Estoque buscarPorId(Long id) {

        return estoqueRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Item de estoque não encontrado"
                        )
                );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Transactional
    public Estoque atualizar(
            Long id,
            EstoqueRequestDTO dto) {

        Estoque estoque =
                buscarPorId(id);

        preencherEstoque(
                estoque,
                dto
        );

        return estoqueRepository.save(
                estoque
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Transactional
    public void deletar(Long id) {

        Estoque estoque =
                buscarPorId(id);

        estoqueRepository.delete(
                estoque
        );
    }

    // =====================================================
    // ADICIONAR QUANTIDADE
    // =====================================================

    @Transactional
    public Estoque adicionarQuantidade(
            Long id,
            int quantidade) {

        validarQuantidadeMaiorQueZero(
                quantidade
        );

        Estoque estoque =
                buscarPorId(id);

        estoque.setQuantidade(
                estoque.getQuantidade()
                        + quantidade
        );

        return estoqueRepository.save(
                estoque
        );
    }

    // =====================================================
    // REMOVER QUANTIDADE
    // =====================================================

    @Transactional
    public Estoque removerQuantidade(
            Long id,
            int quantidade) {

        validarQuantidadeMaiorQueZero(
                quantidade
        );

        Estoque estoque =
                buscarPorId(id);

        if (estoque.getQuantidade()
                < quantidade) {

            throw new IllegalArgumentException(
                    "Quantidade insuficiente! Disponível: "
                            + estoque.getQuantidade()
            );
        }

        estoque.setQuantidade(
                estoque.getQuantidade()
                        - quantidade
        );

        return estoqueRepository.save(
                estoque
        );
    }

    // =====================================================
    // ADICIONAR OU CRIAR ATRAVÉS DE DOAÇÃO
    // =====================================================

    @Transactional
    public Estoque adicionarOuCriarPorDoacao(
            String descricao,
            int quantidade,
            String categoria) {

        if (descricao == null
                || descricao.isBlank()) {

            throw new IllegalArgumentException(
                    "Descrição do item inválida"
            );
        }

        if (quantidade <= 0) {

            throw new IllegalArgumentException(
                    "Quantidade da doação deve ser maior que zero"
            );
        }

        if (categoria == null
                || categoria.isBlank()) {

            throw new IllegalArgumentException(
                    "Categoria inválida"
            );
        }

        String descricaoFormatada =
                descricao
                        .trim()
                        .toUpperCase();

        String categoriaFormatada =
                categoria
                        .trim()
                        .toUpperCase();

        Estoque estoque =
                estoqueRepository
                        .findFirstByDescricaoIgnoreCaseAndCategoriaIgnoreCase(
                                descricaoFormatada,
                                categoriaFormatada
                        )
                        .orElse(null);

        /*
         * Se o item já existe,
         * apenas aumenta a quantidade.
         */
        if (estoque != null) {

            estoque.setQuantidade(
                    estoque.getQuantidade()
                            + quantidade
            );

            return estoqueRepository.save(
                    estoque
            );
        }

        /*
         * Se não existe,
         * cria um novo registro.
         */

        Estoque novoEstoque =
                new Estoque();

        novoEstoque.setDescricao(
                descricaoFormatada
        );

        novoEstoque.setQuantidade(
                quantidade
        );

        novoEstoque.setCategoria(
                categoriaFormatada
        );

        return estoqueRepository.save(
                novoEstoque
        );
    }

    // =====================================================
    // REMOVER POR DOAÇÃO
    // =====================================================
    /*
     * Este método é usado pelo DoacaoService
     * quando uma doação é alterada ou excluída.
     */
    @Transactional
    public Estoque removerPorDoacao(
            String descricao,
            Integer quantidade,
            String categoria) {

        if (descricao == null
                || descricao.isBlank()) {

            throw new IllegalArgumentException(
                    "Descrição inválida"
            );
        }

        if (categoria == null
                || categoria.isBlank()) {

            throw new IllegalArgumentException(
                    "Categoria inválida"
            );
        }

        if (quantidade == null
                || quantidade <= 0) {

            throw new IllegalArgumentException(
                    "Quantidade deve ser maior que zero"
            );
        }

        String descricaoFormatada =
                descricao
                        .trim()
                        .toUpperCase();

        String categoriaFormatada =
                categoria
                        .trim()
                        .toUpperCase();

        Estoque estoque =
                estoqueRepository
                        .findFirstByDescricaoIgnoreCaseAndCategoriaIgnoreCase(
                                descricaoFormatada,
                                categoriaFormatada
                        )
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Item não encontrado no estoque"
                                )
                        );

        if (estoque.getQuantidade()
                < quantidade) {

            throw new IllegalArgumentException(
                    "Quantidade insuficiente no estoque. "
                            + "Disponível: "
                            + estoque.getQuantidade()
            );
        }

        estoque.setQuantidade(
                estoque.getQuantidade()
                        - quantidade
        );

        return estoqueRepository.save(
                estoque
        );
    }

    // =====================================================
    // AJUSTAR ESTOQUE AO ATUALIZAR UMA DOAÇÃO
    // =====================================================
    /*
     * Esse método mantém a compatibilidade
     * com o DoacaoService que montamos antes.
     */
    @Transactional
    public void ajustarPorAtualizacao(
            String descricaoAntiga,
            Integer quantidadeAntiga,
            String categoriaAntiga,
            String descricaoNova,
            Integer quantidadeNova,
            String categoriaNova) {

        String descricaoAntigaFormatada =
                descricaoAntiga
                        .trim()
                        .toUpperCase();

        String descricaoNovaFormatada =
                descricaoNova
                        .trim()
                        .toUpperCase();

        String categoriaAntigaFormatada =
                categoriaAntiga
                        .trim()
                        .toUpperCase();

        String categoriaNovaFormatada =
                categoriaNova
                        .trim()
                        .toUpperCase();

        boolean mesmoItem =
                descricaoAntigaFormatada
                        .equals(
                                descricaoNovaFormatada
                        )
                        &&
                        categoriaAntigaFormatada
                                .equals(
                                        categoriaNovaFormatada
                                );

        /*
         * Se continua sendo o mesmo item,
         * apenas calculamos a diferença.
         */
        if (mesmoItem) {

            int diferenca =
                    quantidadeNova
                            - quantidadeAntiga;

            /*
             * Exemplo:
             *
             * Antes: 10
             * Depois: 15
             *
             * Adiciona 5.
             */
            if (diferenca > 0) {

                adicionarOuCriarPorDoacao(
                        descricaoNovaFormatada,
                        diferenca,
                        categoriaNovaFormatada
                );

            }

            /*
             * Exemplo:
             *
             * Antes: 10
             * Depois: 6
             *
             * Remove 4.
             */
            else if (diferenca < 0) {

                removerPorDoacao(
                        descricaoAntigaFormatada,
                        Math.abs(diferenca),
                        categoriaAntigaFormatada
                );
            }

            return;
        }

        /*
         * Se mudou item/categoria,
         * tira do item antigo.
         */
        removerPorDoacao(
                descricaoAntigaFormatada,
                quantidadeAntiga,
                categoriaAntigaFormatada
        );

        /*
         * E adiciona ao novo item.
         */
        adicionarOuCriarPorDoacao(
                descricaoNovaFormatada,
                quantidadeNova,
                categoriaNovaFormatada
        );
    }

    // =====================================================
    // PREENCHER / FORMATAR
    // =====================================================

    private void preencherEstoque(
            Estoque estoque,
            EstoqueRequestDTO dto) {

        estoque.setDescricao(
                dto.getDescricao()
                        .trim()
                        .toUpperCase()
        );

        estoque.setQuantidade(
                dto.getQuantidade()
        );

        estoque.setCategoria(
                dto.getCategoria()
                        .trim()
                        .toUpperCase()
        );
    }

    // =====================================================
    // VALIDAR QUANTIDADE
    // =====================================================

    private void validarQuantidadeMaiorQueZero(
            int quantidade) {

        if (quantidade <= 0) {

            throw new IllegalArgumentException(
                    "Quantidade deve ser maior que zero"
            );
        }
    }
}
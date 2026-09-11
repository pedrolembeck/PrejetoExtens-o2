package com.doacoes.service;

import com.doacoes.dto.DoacaoRequestDTO;
import com.doacoes.entity.Doacao;
import com.doacoes.repository.DoacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DoacaoService {

    private final DoacaoRepository doacaoRepository;
    private final EstoqueService estoqueService;

    public DoacaoService(
            DoacaoRepository doacaoRepository,
            EstoqueService estoqueService) {

        this.doacaoRepository = doacaoRepository;
        this.estoqueService = estoqueService;
    }

    // =========================
    // CREATE
    // =========================

    @Transactional
    public Doacao salvar(DoacaoRequestDTO dto) {

        validar(dto);

        Doacao doacao = new Doacao();

        preencherDoacao(doacao, dto);

        doacao = doacaoRepository.save(doacao);

        estoqueService.adicionarOuCriarPorDoacao(
                montarDescricaoParaEstoque(doacao),
                doacao.getQuantidade(),
                doacao.getCategoria()
        );

        return doacao;
    }

    // =========================
    // READ
    // =========================

    public List<Doacao> listar() {

        return doacaoRepository.findAll();
    }

    public Doacao buscarPorId(Long id) {

        return doacaoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Doação não encontrada"
                        ));
    }

    // =========================
    // UPDATE
    // =========================

    @Transactional
    public Doacao atualizar(
            Long id,
            DoacaoRequestDTO dto) {

        validar(dto);

        Doacao doacao = buscarPorId(id);

        // Guarda os dados antigos antes da atualização
        String descricaoAntiga =
                montarDescricaoParaEstoque(doacao);

        Integer quantidadeAntiga =
                doacao.getQuantidade();

        String categoriaAntiga =
                doacao.getCategoria();

        // Atualiza os dados da doação
        preencherDoacao(doacao, dto);

        String descricaoNova =
                montarDescricaoParaEstoque(doacao);

        Integer quantidadeNova =
                doacao.getQuantidade();

        String categoriaNova =
                doacao.getCategoria();

        // Atualiza o estoque
        estoqueService.ajustarPorAtualizacao(
                descricaoAntiga,
                quantidadeAntiga,
                categoriaAntiga,
                descricaoNova,
                quantidadeNova,
                categoriaNova
        );

        return doacaoRepository.save(doacao);
    }

    // =========================
    // DELETE
    // =========================

    @Transactional
    public void deletar(Long id) {

        Doacao doacao = buscarPorId(id);

        String descricao =
                montarDescricaoParaEstoque(doacao);

        // Retira do estoque o valor que veio da doação
        estoqueService.removerPorDoacao(
                descricao,
                doacao.getQuantidade(),
                doacao.getCategoria()
        );

        doacaoRepository.delete(doacao);
    }

    // =========================
    // PESQUISAR
    // =========================

    public List<Doacao> pesquisar(String termo) {

        if (termo == null || termo.isBlank()) {

            return listar();
        }

        return doacaoRepository.pesquisar(
                termo.trim()
        );
    }

    // =========================
    // PREENCHER ENTIDADE
    // =========================

    private void preencherDoacao(
            Doacao doacao,
            DoacaoRequestDTO dto) {

        doacao.setNomeDoador(
                dto.getNomeDoador().trim()
        );

        doacao.setDescricaoItem(
                dto.getDescricaoItem().trim()
        );

        doacao.setQuantidade(
                dto.getQuantidade()
        );

        doacao.setCategoria(
                dto.getCategoria()
                        .trim()
                        .toUpperCase()
        );

        doacao.setUnidade(
                dto.getUnidade()
                        .trim()
                        .toUpperCase()
        );

        if (dto.getDetalheUnidade() != null) {

            doacao.setDetalheUnidade(
                    dto.getDetalheUnidade().trim()
            );

        } else {

            doacao.setDetalheUnidade(null);
        }

        if (dto.getTamanho() != null) {

            doacao.setTamanho(
                    dto.getTamanho()
                            .trim()
                            .toUpperCase()
            );

        } else {

            doacao.setTamanho(null);
        }

        doacao.setDataDoacao(
                dto.getDataDoacao()
        );
    }

    // =========================
    // DESCRIÇÃO DO ESTOQUE
    // =========================

    private String montarDescricaoParaEstoque(
            Doacao doacao) {

        if ("VESTUARIO".equals(
                doacao.getCategoria())) {

            if (doacao.getTamanho() != null
                    && !doacao.getTamanho().isBlank()) {

                return doacao.getDescricaoItem()
                        + " - TAM "
                        + doacao.getTamanho();
            }
        }

        if ("ALIMENTOS".equals(
                doacao.getCategoria())) {

            String descricao =
                    doacao.getDescricaoItem();

            if (doacao.getUnidade() != null
                    && !doacao.getUnidade().isBlank()
                    && !"UNIDADE".equals(
                    doacao.getUnidade())) {

                descricao +=
                        " - "
                                + doacao.getUnidade();
            }

            if (doacao.getDetalheUnidade() != null
                    && !doacao.getDetalheUnidade().isBlank()) {

                descricao +=
                        " "
                                + doacao.getDetalheUnidade();
            }

            return descricao;
        }

        return doacao.getDescricaoItem();
    }

    // =========================
    // REGRAS DE NEGÓCIO
    // =========================

    private void validar(
            DoacaoRequestDTO dto) {

        if ("VESTUARIO".equalsIgnoreCase(
                dto.getCategoria())
                && (dto.getTamanho() == null
                || dto.getTamanho().isBlank())) {

            throw new IllegalArgumentException(
                    "Tamanho é obrigatório para vestuário"
            );
        }
    }
}
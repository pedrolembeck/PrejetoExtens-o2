package com.doacoes.service;

import com.doacoes.dto.TipoDoacaoRequestDTO;
import com.doacoes.entity.TipoDoacao;
import com.doacoes.repository.TipoDoacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipoDoacaoService {

    private final TipoDoacaoRepository tipoDoacaoRepository;

    public TipoDoacaoService(
            TipoDoacaoRepository tipoDoacaoRepository) {

        this.tipoDoacaoRepository =
                tipoDoacaoRepository;
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Transactional
    public TipoDoacao salvar(
            TipoDoacaoRequestDTO dto) {

        TipoDoacao tipoDoacao =
                new TipoDoacao();

        preencherTipoDoacao(
                tipoDoacao,
                dto
        );

        return tipoDoacaoRepository.save(
                tipoDoacao
        );
    }

    // =====================================================
    // READ - LISTAR
    // =====================================================

    public List<TipoDoacao> listar() {

        return tipoDoacaoRepository
                .findAll();
    }

    // =====================================================
    // READ - BUSCAR POR ID
    // =====================================================

    public TipoDoacao buscarPorId(
            Long id) {

        return tipoDoacaoRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Tipo de doação não encontrado"
                        )
                );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Transactional
    public TipoDoacao atualizar(
            Long id,
            TipoDoacaoRequestDTO dto) {

        TipoDoacao tipoDoacao =
                buscarPorId(id);

        preencherTipoDoacao(
                tipoDoacao,
                dto
        );

        return tipoDoacaoRepository.save(
                tipoDoacao
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Transactional
    public void deletar(Long id) {

        TipoDoacao tipoDoacao =
                buscarPorId(id);

        tipoDoacaoRepository.delete(
                tipoDoacao
        );
    }

    // =====================================================
    // PREENCHER / FORMATAR
    // =====================================================

    private void preencherTipoDoacao(
            TipoDoacao tipoDoacao,
            TipoDoacaoRequestDTO dto) {

        tipoDoacao.setNome(
                dto.getNome()
                        .trim()
                        .toUpperCase()
        );
    }
}
package com.doacoes.service;

import com.doacoes.dto.DoadorRequestDTO;
import com.doacoes.entity.Doador;
import com.doacoes.repository.DoadorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DoadorService {

    private final DoadorRepository doadorRepository;

    public DoadorService(DoadorRepository doadorRepository) {
        this.doadorRepository = doadorRepository;
    }

    // =========================
    // CREATE
    // =========================

    @Transactional
    public Doador salvar(DoadorRequestDTO dto) {

        validarCpf(dto.getCpf());

        Doador doador = new Doador();

        preencherDoador(doador, dto);

        return doadorRepository.save(doador);
    }

    // =========================
    // READ - LISTAR
    // =========================

    public List<Doador> listar() {
        return doadorRepository.findAll();
    }

    // =========================
    // READ - BUSCAR POR ID
    // =========================

    public Doador buscarPorId(Long id) {

        return doadorRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Doador não encontrado"
                        ));
    }

    // =========================
    // UPDATE
    // =========================

    @Transactional
    public Doador atualizar(
            Long id,
            DoadorRequestDTO dto) {

        validarCpf(dto.getCpf());

        Doador doador = buscarPorId(id);

        preencherDoador(doador, dto);

        return doadorRepository.save(doador);
    }

    // =========================
    // DELETE
    // =========================

    @Transactional
    public void deletar(Long id) {

        Doador doador = buscarPorId(id);

        doadorRepository.delete(doador);
    }

    // =========================
    // PESQUISAR
    // =========================

    public List<Doador> pesquisar(String termo) {

        if (termo == null || termo.isBlank()) {
            return listar();
        }

        return doadorRepository.pesquisar(
                termo.trim()
        );
    }

    // =========================
    // PREENCHER / FORMATAR
    // =========================

    private void preencherDoador(
            Doador doador,
            DoadorRequestDTO dto) {

        // Nome em maiúsculo
        doador.setNome(
                dto.getNome()
                        .trim()
                        .toUpperCase()
        );

        // CPF
        if (dto.getCpf() != null
                && !dto.getCpf().isBlank()) {

            doador.setCpf(
                    dto.getCpf().trim()
            );

        } else {
            doador.setCpf(null);
        }

        // Endereço
        if (dto.getEndereco() != null
                && !dto.getEndereco().isBlank()) {

            doador.setEndereco(
                    dto.getEndereco().trim()
            );

        } else {
            doador.setEndereco(null);
        }

        // Telefone
        if (dto.getTelefone() != null
                && !dto.getTelefone().isBlank()) {

            doador.setTelefone(
                    dto.getTelefone().trim()
            );

        } else {
            doador.setTelefone(null);
        }

        // E-mail em minúsculo
        if (dto.getEmail() != null
                && !dto.getEmail().isBlank()) {

            doador.setEmail(
                    dto.getEmail()
                            .trim()
                            .toLowerCase()
            );

        } else {
            doador.setEmail(null);
        }
    }

    // =========================
    // VALIDAR CPF
    // =========================

    private void validarCpf(String cpf) {

        if (cpf == null || cpf.isBlank()) {
            return;
        }

        String cpfLimpo =
                cpf.replaceAll("[^0-9]", "");

        if (cpfLimpo.length() != 11) {

            throw new IllegalArgumentException(
                    "CPF inválido! Informe 11 dígitos."
            );
        }
    }
}
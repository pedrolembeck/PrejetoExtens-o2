package com.doacoes.service;

import com.doacoes.dto.ReceptorRequestDTO;
import com.doacoes.entity.Receptor;
import com.doacoes.repository.ReceptorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReceptorService {

    private final ReceptorRepository receptorRepository;

    public ReceptorService(
            ReceptorRepository receptorRepository) {

        this.receptorRepository = receptorRepository;
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Transactional
    public Receptor salvar(
            ReceptorRequestDTO dto) {

        validarCpf(dto.getCpf());

        Receptor receptor =
                new Receptor();

        preencherReceptor(
                receptor,
                dto
        );

        return receptorRepository.save(
                receptor
        );
    }

    // =====================================================
    // READ - LISTAR
    // =====================================================

    public List<Receptor> listar() {

        return receptorRepository.findAll();
    }

    // =====================================================
    // READ - BUSCAR POR ID
    // =====================================================

    public Receptor buscarPorId(Long id) {

        return receptorRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Receptor não encontrado"
                        )
                );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Transactional
    public Receptor atualizar(
            Long id,
            ReceptorRequestDTO dto) {

        validarCpf(dto.getCpf());

        Receptor receptor =
                buscarPorId(id);

        preencherReceptor(
                receptor,
                dto
        );

        return receptorRepository.save(
                receptor
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Transactional
    public void deletar(Long id) {

        Receptor receptor =
                buscarPorId(id);

        receptorRepository.delete(
                receptor
        );
    }

    // =====================================================
    // PESQUISAR
    // =====================================================

    public List<Receptor> pesquisar(
            String termo) {

        if (termo == null
                || termo.isBlank()) {

            return listar();
        }

        return receptorRepository
                .pesquisar(
                        termo.trim()
                );
    }

    // =====================================================
    // PREENCHER / FORMATAR
    // =====================================================

    private void preencherReceptor(
            Receptor receptor,
            ReceptorRequestDTO dto) {

        /*
         * Nome em letras maiúsculas
         */
        receptor.setNome(
                dto.getNome()
                        .trim()
                        .toUpperCase()
        );

        /*
         * CPF
         */
        if (dto.getCpf() != null
                && !dto.getCpf().isBlank()) {

            receptor.setCpf(
                    dto.getCpf().trim()
            );

        } else {

            receptor.setCpf(null);
        }

        /*
         * Endereço
         */
        if (dto.getEndereco() != null
                && !dto.getEndereco().isBlank()) {

            receptor.setEndereco(
                    dto.getEndereco().trim()
            );

        } else {

            receptor.setEndereco(null);
        }

        /*
         * Telefone
         */
        if (dto.getTelefone() != null
                && !dto.getTelefone().isBlank()) {

            receptor.setTelefone(
                    dto.getTelefone().trim()
            );

        } else {

            receptor.setTelefone(null);
        }

        /*
         * E-mail em letras minúsculas
         */
        if (dto.getEmail() != null
                && !dto.getEmail().isBlank()) {

            receptor.setEmail(
                    dto.getEmail()
                            .trim()
                            .toLowerCase()
            );

        } else {

            receptor.setEmail(null);
        }
    }

    // =====================================================
    // VALIDAR CPF
    // =====================================================

    private void validarCpf(String cpf) {

        /*
         * CPF não é obrigatório.
         */
        if (cpf == null
                || cpf.isBlank()) {

            return;
        }

        /*
         * Remove pontos, traços etc.
         *
         * Exemplo:
         *
         * 123.456.789-00
         *
         * vira:
         *
         * 12345678900
         */
        String cpfLimpo =
                cpf.replaceAll(
                        "[^0-9]",
                        ""
                );

        if (cpfLimpo.length() != 11) {

            throw new IllegalArgumentException(
                    "CPF inválido! Informe 11 dígitos."
            );
        }
    }
}
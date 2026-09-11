package com.doacoes.controller;

import com.doacoes.dto.TamanhoRoupaRequestDTO;
import com.doacoes.dto.TamanhoRoupaResponseDTO;
import com.doacoes.entity.TamanhoRoupa;
import com.doacoes.service.TamanhoRoupaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tamanhos-roupa")
public class TamanhoRoupaController {

    private final TamanhoRoupaService tamanhoRoupaService;

    public TamanhoRoupaController(
            TamanhoRoupaService tamanhoRoupaService) {

        this.tamanhoRoupaService =
                tamanhoRoupaService;
    }

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<TamanhoRoupaResponseDTO> salvar(
            @Valid
            @RequestBody TamanhoRoupaRequestDTO dto) {

        TamanhoRoupa tamanhoRoupa =
                tamanhoRoupaService.salvar(
                        dto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new TamanhoRoupaResponseDTO(
                                tamanhoRoupa
                        )
                );
    }

    // =====================================================
    // READ - LISTAR
    // =====================================================

    @GetMapping
    public ResponseEntity<List<TamanhoRoupaResponseDTO>> listar() {

        List<TamanhoRoupaResponseDTO> tamanhos =
                tamanhoRoupaService
                        .listar()
                        .stream()
                        .map(
                                TamanhoRoupaResponseDTO::new
                        )
                        .toList();

        return ResponseEntity.ok(
                tamanhos
        );
    }

    // =====================================================
    // READ - BUSCAR POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<TamanhoRoupaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        TamanhoRoupa tamanhoRoupa =
                tamanhoRoupaService
                        .buscarPorId(id);

        return ResponseEntity.ok(
                new TamanhoRoupaResponseDTO(
                        tamanhoRoupa
                )
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<TamanhoRoupaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid
            @RequestBody TamanhoRoupaRequestDTO dto) {

        TamanhoRoupa tamanhoRoupa =
                tamanhoRoupaService.atualizar(
                        id,
                        dto
                );

        return ResponseEntity.ok(
                new TamanhoRoupaResponseDTO(
                        tamanhoRoupa
                )
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        tamanhoRoupaService.deletar(
                id
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}
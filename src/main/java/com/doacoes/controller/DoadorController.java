package com.doacoes.controller;

import com.doacoes.dto.DoadorRequestDTO;
import com.doacoes.dto.DoadorResponseDTO;
import com.doacoes.entity.Doador;
import com.doacoes.service.DoadorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doadores")
public class DoadorController {

    private final DoadorService doadorService;

    public DoadorController(
            DoadorService doadorService) {

        this.doadorService = doadorService;
    }

    // =========================
    // CREATE
    // =========================

    @PostMapping
    public ResponseEntity<DoadorResponseDTO> salvar(
            @Valid
            @RequestBody DoadorRequestDTO dto) {

        Doador doador =
                doadorService.salvar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new DoadorResponseDTO(doador)
                );
    }

    // =========================
    // READ - LISTAR
    // =========================

    @GetMapping
    public ResponseEntity<List<DoadorResponseDTO>> listar() {

        List<DoadorResponseDTO> doadores =
                doadorService.listar()
                        .stream()
                        .map(DoadorResponseDTO::new)
                        .toList();

        return ResponseEntity.ok(doadores);
    }

    // =========================
    // READ - BUSCAR POR ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<DoadorResponseDTO> buscarPorId(
            @PathVariable Long id) {

        Doador doador =
                doadorService.buscarPorId(id);

        return ResponseEntity.ok(
                new DoadorResponseDTO(doador)
        );
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<DoadorResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid
            @RequestBody DoadorRequestDTO dto) {

        Doador doador =
                doadorService.atualizar(
                        id,
                        dto
                );

        return ResponseEntity.ok(
                new DoadorResponseDTO(doador)
        );
    }

    // =========================
    // DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        doadorService.deletar(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    // =========================
    // PESQUISAR
    // =========================

    @GetMapping("/pesquisar")
    public ResponseEntity<List<DoadorResponseDTO>> pesquisar(
            @RequestParam(required = false)
            String termo) {

        List<DoadorResponseDTO> doadores =
                doadorService.pesquisar(termo)
                        .stream()
                        .map(DoadorResponseDTO::new)
                        .toList();

        return ResponseEntity.ok(doadores);
    }
}
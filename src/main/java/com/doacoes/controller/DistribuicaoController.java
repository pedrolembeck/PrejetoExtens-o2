package com.doacoes.controller;

import com.doacoes.dto.DistribuicaoRequestDTO;
import com.doacoes.dto.DistribuicaoResponseDTO;
import com.doacoes.entity.Distribuicao;
import com.doacoes.service.DistribuicaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/distribuicoes")
public class DistribuicaoController {

    private final DistribuicaoService distribuicaoService;

    public DistribuicaoController(
            DistribuicaoService distribuicaoService) {

        this.distribuicaoService = distribuicaoService;
    }

    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    public ResponseEntity<DistribuicaoResponseDTO> salvar(
            @Valid @RequestBody DistribuicaoRequestDTO dto) {

        Distribuicao distribuicao =
                distribuicaoService.salvar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new DistribuicaoResponseDTO(distribuicao));
    }

    // =========================================================
    // READ - LISTAR
    // =========================================================

    @GetMapping
    public ResponseEntity<List<DistribuicaoResponseDTO>> listar() {

        List<DistribuicaoResponseDTO> distribuicoes =
                distribuicaoService.listar()
                        .stream()
                        .map(DistribuicaoResponseDTO::new)
                        .toList();

        return ResponseEntity.ok(distribuicoes);
    }

    // =========================================================
    // READ - BUSCAR POR ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<DistribuicaoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        Distribuicao distribuicao =
                distribuicaoService.buscarPorId(id);

        return ResponseEntity.ok(
                new DistribuicaoResponseDTO(distribuicao)
        );
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<DistribuicaoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DistribuicaoRequestDTO dto) {

        Distribuicao distribuicao =
                distribuicaoService.atualizar(id, dto);

        return ResponseEntity.ok(
                new DistribuicaoResponseDTO(distribuicao)
        );
    }

    // =========================================================
    // DELETE
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        distribuicaoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
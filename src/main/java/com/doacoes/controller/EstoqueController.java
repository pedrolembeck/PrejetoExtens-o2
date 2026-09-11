package com.doacoes.controller;

import com.doacoes.dto.EstoqueRequestDTO;
import com.doacoes.dto.EstoqueResponseDTO;
import com.doacoes.dto.QuantidadeRequestDTO;
import com.doacoes.entity.Estoque;
import com.doacoes.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoques")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(
            EstoqueService estoqueService) {

        this.estoqueService = estoqueService;
    }

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<EstoqueResponseDTO> salvar(
            @Valid
            @RequestBody EstoqueRequestDTO dto) {

        Estoque estoque =
                estoqueService.salvar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new EstoqueResponseDTO(
                                estoque
                        )
                );
    }

    // =====================================================
    // READ - LISTAR
    // =====================================================

    @GetMapping
    public ResponseEntity<List<EstoqueResponseDTO>> listar() {

        List<EstoqueResponseDTO> estoques =
                estoqueService
                        .listar()
                        .stream()
                        .map(
                                EstoqueResponseDTO::new
                        )
                        .toList();

        return ResponseEntity.ok(
                estoques
        );
    }

    // =====================================================
    // READ - BUSCAR POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<EstoqueResponseDTO> buscarPorId(
            @PathVariable Long id) {

        Estoque estoque =
                estoqueService
                        .buscarPorId(id);

        return ResponseEntity.ok(
                new EstoqueResponseDTO(
                        estoque
                )
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<EstoqueResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid
            @RequestBody EstoqueRequestDTO dto) {

        Estoque estoque =
                estoqueService.atualizar(
                        id,
                        dto
                );

        return ResponseEntity.ok(
                new EstoqueResponseDTO(
                        estoque
                )
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        estoqueService.deletar(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    // =====================================================
    // ADICIONAR QUANTIDADE
    // =====================================================

    @PatchMapping("/{id}/adicionar")
    public ResponseEntity<EstoqueResponseDTO> adicionarQuantidade(
            @PathVariable Long id,
            @Valid
            @RequestBody QuantidadeRequestDTO dto) {

        Estoque estoque =
                estoqueService
                        .adicionarQuantidade(
                                id,
                                dto.getQuantidade()
                        );

        return ResponseEntity.ok(
                new EstoqueResponseDTO(
                        estoque
                )
        );
    }

    // =====================================================
    // REMOVER QUANTIDADE
    // =====================================================

    @PatchMapping("/{id}/remover")
    public ResponseEntity<EstoqueResponseDTO> removerQuantidade(
            @PathVariable Long id,
            @Valid
            @RequestBody QuantidadeRequestDTO dto) {

        Estoque estoque =
                estoqueService
                        .removerQuantidade(
                                id,
                                dto.getQuantidade()
                        );

        return ResponseEntity.ok(
                new EstoqueResponseDTO(
                        estoque
                )
        );
    }
}
package com.doacoes.controller;

import com.doacoes.dto.TipoDoacaoRequestDTO;
import com.doacoes.dto.TipoDoacaoResponseDTO;
import com.doacoes.entity.TipoDoacao;
import com.doacoes.service.TipoDoacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos-doacao")
public class TipoDoacaoController {

    private final TipoDoacaoService tipoDoacaoService;

    public TipoDoacaoController(
            TipoDoacaoService tipoDoacaoService) {

        this.tipoDoacaoService =
                tipoDoacaoService;
    }

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<TipoDoacaoResponseDTO> salvar(
            @Valid
            @RequestBody TipoDoacaoRequestDTO dto) {

        TipoDoacao tipoDoacao =
                tipoDoacaoService.salvar(
                        dto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new TipoDoacaoResponseDTO(
                                tipoDoacao
                        )
                );
    }

    // =====================================================
    // READ - LISTAR
    // =====================================================

    @GetMapping
    public ResponseEntity<List<TipoDoacaoResponseDTO>> listar() {

        List<TipoDoacaoResponseDTO> tipos =
                tipoDoacaoService
                        .listar()
                        .stream()
                        .map(
                                TipoDoacaoResponseDTO::new
                        )
                        .toList();

        return ResponseEntity.ok(
                tipos
        );
    }

    // =====================================================
    // READ - BUSCAR POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<TipoDoacaoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        TipoDoacao tipoDoacao =
                tipoDoacaoService
                        .buscarPorId(id);

        return ResponseEntity.ok(
                new TipoDoacaoResponseDTO(
                        tipoDoacao
                )
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<TipoDoacaoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid
            @RequestBody TipoDoacaoRequestDTO dto) {

        TipoDoacao tipoDoacao =
                tipoDoacaoService.atualizar(
                        id,
                        dto
                );

        return ResponseEntity.ok(
                new TipoDoacaoResponseDTO(
                        tipoDoacao
                )
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        tipoDoacaoService.deletar(
                id
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}
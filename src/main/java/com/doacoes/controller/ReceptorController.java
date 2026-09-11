package com.doacoes.controller;

import com.doacoes.dto.ReceptorRequestDTO;
import com.doacoes.dto.ReceptorResponseDTO;
import com.doacoes.entity.Receptor;
import com.doacoes.service.ReceptorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receptores")
public class ReceptorController {

    private final ReceptorService receptorService;

    public ReceptorController(
            ReceptorService receptorService) {

        this.receptorService =
                receptorService;
    }

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<ReceptorResponseDTO> salvar(
            @Valid
            @RequestBody ReceptorRequestDTO dto) {

        Receptor receptor =
                receptorService.salvar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ReceptorResponseDTO(
                                receptor
                        )
                );
    }

    // =====================================================
    // READ - LISTAR
    // =====================================================

    @GetMapping
    public ResponseEntity<List<ReceptorResponseDTO>> listar() {

        List<ReceptorResponseDTO> receptores =
                receptorService
                        .listar()
                        .stream()
                        .map(
                                ReceptorResponseDTO::new
                        )
                        .toList();

        return ResponseEntity.ok(
                receptores
        );
    }

    // =====================================================
    // READ - BUSCAR POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<ReceptorResponseDTO> buscarPorId(
            @PathVariable Long id) {

        Receptor receptor =
                receptorService
                        .buscarPorId(id);

        return ResponseEntity.ok(
                new ReceptorResponseDTO(
                        receptor
                )
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<ReceptorResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid
            @RequestBody ReceptorRequestDTO dto) {

        Receptor receptor =
                receptorService
                        .atualizar(
                                id,
                                dto
                        );

        return ResponseEntity.ok(
                new ReceptorResponseDTO(
                        receptor
                )
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        receptorService.deletar(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    // =====================================================
    // PESQUISAR
    // =====================================================

    @GetMapping("/pesquisar")
    public ResponseEntity<List<ReceptorResponseDTO>> pesquisar(
            @RequestParam(required = false)
            String termo) {

        List<ReceptorResponseDTO> receptores =
                receptorService
                        .pesquisar(termo)
                        .stream()
                        .map(
                                ReceptorResponseDTO::new
                        )
                        .toList();

        return ResponseEntity.ok(
                receptores
        );
    }
}
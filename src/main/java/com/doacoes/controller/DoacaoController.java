package com.doacoes.controller;

import com.doacoes.dto.DoacaoRequestDTO;
import com.doacoes.dto.DoacaoResponseDTO;
import com.doacoes.entity.Doacao;
import com.doacoes.service.DoacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doacoes")
public class DoacaoController {

    private final DoacaoService doacaoService;

    public DoacaoController(
            DoacaoService doacaoService) {

        this.doacaoService = doacaoService;
    }

    // CREATE

    @PostMapping
    public ResponseEntity<DoacaoResponseDTO> salvar(
            @Valid
            @RequestBody DoacaoRequestDTO dto) {

        Doacao doacao =
                doacaoService.salvar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new DoacaoResponseDTO(doacao)
                );
    }

    // READ - LISTAR

    @GetMapping
    public ResponseEntity<List<DoacaoResponseDTO>> listar() {

        List<DoacaoResponseDTO> doacoes =
                doacaoService.listar()
                        .stream()
                        .map(DoacaoResponseDTO::new)
                        .toList();

        return ResponseEntity.ok(doacoes);
    }

    // READ - BUSCAR ID

    @GetMapping("/{id}")
    public ResponseEntity<DoacaoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        Doacao doacao =
                doacaoService.buscarPorId(id);

        return ResponseEntity.ok(
                new DoacaoResponseDTO(doacao)
        );
    }

    // UPDATE

    @PutMapping("/{id}")
    public ResponseEntity<DoacaoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid
            @RequestBody DoacaoRequestDTO dto) {

        Doacao doacao =
                doacaoService.atualizar(
                        id,
                        dto
                );

        return ResponseEntity.ok(
                new DoacaoResponseDTO(doacao)
        );
    }

    // DELETE

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        doacaoService.deletar(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    // PESQUISAR

    @GetMapping("/pesquisar")
    public ResponseEntity<List<DoacaoResponseDTO>> pesquisar(
            @RequestParam(required = false)
            String termo) {

        List<DoacaoResponseDTO> doacoes =
                doacaoService.pesquisar(termo)
                        .stream()
                        .map(DoacaoResponseDTO::new)
                        .toList();

        return ResponseEntity.ok(doacoes);
    }
}
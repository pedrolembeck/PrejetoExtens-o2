package com.doacoes.controller;

import com.doacoes.dto.LoginRequestDTO;
import com.doacoes.dto.UsuarioRequestDTO;
import com.doacoes.dto.UsuarioResponseDTO;
import com.doacoes.entity.Usuario;
import com.doacoes.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(
            UsuarioService usuarioService) {

        this.usuarioService =
                usuarioService;
    }

    // =====================================================
    // AUTENTICAR
    // =====================================================

    @PostMapping("/autenticar")
    public ResponseEntity<UsuarioResponseDTO> autenticar(
            @Valid
            @RequestBody LoginRequestDTO dto) {

        Usuario usuario =
                usuarioService.autenticar(
                        dto
                );

        return ResponseEntity.ok(
                new UsuarioResponseDTO(
                        usuario
                )
        );
    }

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> salvar(
            @Valid
            @RequestBody UsuarioRequestDTO dto) {

        Usuario usuario =
                usuarioService.salvar(
                        dto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new UsuarioResponseDTO(
                                usuario
                        )
                );
    }

    // =====================================================
    // READ - LISTAR
    // =====================================================

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {

        List<UsuarioResponseDTO> usuarios =
                usuarioService
                        .listar()
                        .stream()
                        .map(
                                UsuarioResponseDTO::new
                        )
                        .toList();

        return ResponseEntity.ok(
                usuarios
        );
    }

    // =====================================================
    // READ - BUSCAR POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(
            @PathVariable Long id) {

        Usuario usuario =
                usuarioService
                        .buscarPorId(id);

        return ResponseEntity.ok(
                new UsuarioResponseDTO(
                        usuario
                )
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid
            @RequestBody UsuarioRequestDTO dto) {

        Usuario usuario =
                usuarioService.atualizar(
                        id,
                        dto
                );

        return ResponseEntity.ok(
                new UsuarioResponseDTO(
                        usuario
                )
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        usuarioService.deletar(
                id
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}
package com.doacoes.dto;

import com.doacoes.entity.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String login;
    private String perfil;
    private List<String> permissoes;

    public UsuarioResponseDTO(
            Usuario usuario) {

        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.login = usuario.getLogin();
        this.perfil = usuario.getPerfil();

        this.permissoes =
                usuario.getPermissoes() == null
                        ? new ArrayList<>()
                        : new ArrayList<>(
                        usuario.getPermissoes()
                );
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public String getPerfil() {
        return perfil;
    }

    public List<String> getPermissoes() {
        return permissoes;
    }
}
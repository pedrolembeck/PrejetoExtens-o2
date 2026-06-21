package com.doacoes.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

    private Long id;
    private String nome;
    private String login;
    private String senha;
    private String perfil;
    private List<String> permissoes = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(Long id, String nome, String login, String senha, String perfil) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public List<String> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<String> permissoes) {
        this.permissoes = permissoes == null ? new ArrayList<>() : permissoes;
    }

    public boolean isAdmin() {
        return perfil != null && perfil.equalsIgnoreCase("ADMIN");
    }

    public boolean temPermissao(String tela) {
        if (isAdmin()) {
            return true;
        }

        return permissoes != null && permissoes.contains(tela);
    }
}
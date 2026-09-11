package com.doacoes.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "usuario",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_usuario_login",
                        columnNames = "login"
                )
        }
)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String perfil;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "usuario_permissao",
            joinColumns = @JoinColumn(name = "usuario_id")
    )
    @Column(name = "permissao")
    private List<String> permissoes =
            new ArrayList<>();

    public Usuario() {
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

    public void setPermissoes(
            List<String> permissoes) {

        this.permissoes = permissoes;
    }
}
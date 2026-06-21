package com.doacoes.dao;

import com.doacoes.connection.ConnectionFactory;
import com.doacoes.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void criarTabela() {
        String sqlUsuario = """
                CREATE TABLE IF NOT EXISTS usuario (
                    id SERIAL PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    login VARCHAR(50) NOT NULL UNIQUE,
                    senha VARCHAR(100) NOT NULL,
                    perfil VARCHAR(30) DEFAULT 'USUARIO'
                )
                """;

        String sqlPermissao = """
                CREATE TABLE IF NOT EXISTS usuario_permissao (
                    id SERIAL PRIMARY KEY,
                    id_usuario INTEGER NOT NULL,
                    tela VARCHAR(50) NOT NULL,
                    CONSTRAINT fk_usuario_permissao_usuario
                        FOREIGN KEY (id_usuario)
                        REFERENCES usuario(id)
                        ON DELETE CASCADE,
                    CONSTRAINT uk_usuario_permissao UNIQUE (id_usuario, tela)
                )
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statementUsuario = connection.prepareStatement(sqlUsuario);
                PreparedStatement statementPermissao = connection.prepareStatement(sqlPermissao)
        ) {
            statementUsuario.executeUpdate();
            statementPermissao.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Usuario buscarPorLoginESenha(String login, String senha) {
        String sql = """
                SELECT *
                FROM usuario
                WHERE login = ?
                  AND senha = ?
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, login);
            statement.setString(2, senha);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapearUsuario(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void salvar(Usuario usuario) {
        String sql = """
                INSERT INTO usuario(nome, login, senha, perfil)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getLogin());
            statement.setString(3, usuario.getSenha());
            statement.setString(4, usuario.getPerfil());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getLong(1));
                }
            }

            salvarPermissoes(usuario);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT * FROM usuario ORDER BY id";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                lista.add(mapearUsuario(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public Usuario buscarPorId(Long id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapearUsuario(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void atualizar(Usuario usuario) {
        String sql = """
                UPDATE usuario
                SET nome = ?, login = ?, senha = ?, perfil = ?
                WHERE id = ?
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getLogin());
            statement.setString(3, usuario.getSenha());
            statement.setString(4, usuario.getPerfil());
            statement.setLong(5, usuario.getId());

            statement.executeUpdate();

            deletarPermissoes(usuario.getId());
            salvarPermissoes(usuario);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void salvarPermissoes(Usuario usuario) {
        if (usuario.getId() == null ||
                usuario.getPermissoes() == null ||
                usuario.getPermissoes().isEmpty()) {
            return;
        }

        String sql = """
                INSERT INTO usuario_permissao(id_usuario, tela)
                VALUES (?, ?)
                ON CONFLICT (id_usuario, tela) DO NOTHING
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            for (String tela : usuario.getPermissoes()) {
                statement.setLong(1, usuario.getId());
                statement.setString(2, tela);
                statement.addBatch();
            }

            statement.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deletarPermissoes(Long idUsuario) {
        String sql = "DELETE FROM usuario_permissao WHERE id_usuario = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, idUsuario);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> buscarPermissoes(Long idUsuario) {
        List<String> permissoes = new ArrayList<>();

        String sql = """
                SELECT tela
                FROM usuario_permissao
                WHERE id_usuario = ?
                ORDER BY tela
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, idUsuario);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    permissoes.add(resultSet.getString("tela"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return permissoes;
    }

    private Usuario mapearUsuario(ResultSet resultSet) throws SQLException {
        Usuario usuario = new Usuario();

        usuario.setId(resultSet.getLong("id"));
        usuario.setNome(resultSet.getString("nome"));
        usuario.setLogin(resultSet.getString("login"));
        usuario.setSenha(resultSet.getString("senha"));
        usuario.setPerfil(resultSet.getString("perfil"));
        usuario.setPermissoes(buscarPermissoes(usuario.getId()));

        return usuario;
    }
}
package com.doacoes.dao;

import com.doacoes.connection.ConnectionFactory;
import com.doacoes.model.Receptor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReceptorDAO {

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS receptor (
                    id SERIAL PRIMARY KEY,
                    nome VARCHAR(150) NOT NULL,
                    cpf VARCHAR(14),
                    endereco VARCHAR(255),
                    telefone VARCHAR(20),
                    email VARCHAR(100),
                    observacao VARCHAR(300)
                )
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void salvar(Receptor receptor) {
        String sql = """
                INSERT INTO receptor(nome, cpf, endereco, telefone, email, observacao)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, receptor.getNome());
            statement.setString(2, receptor.getCpf());
            statement.setString(3, receptor.getEndereco());
            statement.setString(4, receptor.getTelefone());
            statement.setString(5, receptor.getEmail());
            statement.setString(6, receptor.getObservacao());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Receptor> listar() {
        List<Receptor> lista = new ArrayList<>();

        String sql = "SELECT * FROM receptor ORDER BY id";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                lista.add(mapearReceptor(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public Receptor buscarPorId(Long id) {
        String sql = "SELECT * FROM receptor WHERE id = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapearReceptor(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void atualizar(Receptor receptor) {
        String sql = """
                UPDATE receptor
                SET nome = ?,
                    cpf = ?,
                    endereco = ?,
                    telefone = ?,
                    email = ?,
                    observacao = ?
                WHERE id = ?
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, receptor.getNome());
            statement.setString(2, receptor.getCpf());
            statement.setString(3, receptor.getEndereco());
            statement.setString(4, receptor.getTelefone());
            statement.setString(5, receptor.getEmail());
            statement.setString(6, receptor.getObservacao());
            statement.setLong(7, receptor.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM receptor WHERE id = ?";

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

    public List<Receptor> pesquisar(String termo) {
        List<Receptor> lista = new ArrayList<>();

        String sql = """
                SELECT * FROM receptor
                WHERE UPPER(nome) LIKE UPPER(?)
                   OR UPPER(COALESCE(cpf, '')) LIKE UPPER(?)
                   OR UPPER(COALESCE(email, '')) LIKE UPPER(?)
                   OR UPPER(COALESCE(telefone, '')) LIKE UPPER(?)
                   OR UPPER(COALESCE(endereco, '')) LIKE UPPER(?)
                ORDER BY id
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            String termoPesquisa = "%" + termo + "%";

            statement.setString(1, termoPesquisa);
            statement.setString(2, termoPesquisa);
            statement.setString(3, termoPesquisa);
            statement.setString(4, termoPesquisa);
            statement.setString(5, termoPesquisa);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    lista.add(mapearReceptor(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    private Receptor mapearReceptor(ResultSet resultSet) throws SQLException {
        Receptor receptor = new Receptor(
                resultSet.getLong("id"),
                resultSet.getString("nome"),
                resultSet.getString("cpf"),
                resultSet.getString("endereco"),
                resultSet.getString("telefone"),
                resultSet.getString("email"),
                resultSet.getString("observacao")
        );

        return receptor;
    }
}
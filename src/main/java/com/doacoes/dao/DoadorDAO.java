package com.doacoes.dao;

import com.doacoes.connection.ConnectionFactory;
import com.doacoes.model.Doador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoadorDAO {

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS doador (
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

    public void salvar(Doador doador) {
        String sql = """
                INSERT INTO doador(nome, cpf, endereco, telefone, email, observacao)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, doador.getNome());
            statement.setString(2, doador.getCpf());
            statement.setString(3, doador.getEndereco());
            statement.setString(4, doador.getTelefone());
            statement.setString(5, doador.getEmail());
            statement.setString(6, doador.getObservacao());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Doador> listar() {
        List<Doador> lista = new ArrayList<>();

        String sql = "SELECT * FROM doador ORDER BY id";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                lista.add(mapearDoador(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public Doador buscarPorId(Long id) {
        String sql = "SELECT * FROM doador WHERE id = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapearDoador(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void atualizar(Doador doador) {
        String sql = """
                UPDATE doador
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
            statement.setString(1, doador.getNome());
            statement.setString(2, doador.getCpf());
            statement.setString(3, doador.getEndereco());
            statement.setString(4, doador.getTelefone());
            statement.setString(5, doador.getEmail());
            statement.setString(6, doador.getObservacao());
            statement.setLong(7, doador.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM doador WHERE id = ?";

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

    public List<Doador> pesquisar(String termo) {
        List<Doador> lista = new ArrayList<>();

        String sql = """
                SELECT * FROM doador
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
                    lista.add(mapearDoador(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    private Doador mapearDoador(ResultSet resultSet) throws SQLException {
        Doador doador = new Doador(
                resultSet.getLong("id"),
                resultSet.getString("nome"),
                resultSet.getString("cpf"),
                resultSet.getString("endereco"),
                resultSet.getString("telefone"),
                resultSet.getString("email"),
                resultSet.getString("observacao")
        );

        return doador;
    }
}
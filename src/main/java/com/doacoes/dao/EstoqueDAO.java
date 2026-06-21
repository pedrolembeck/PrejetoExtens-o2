package com.doacoes.dao;

import com.doacoes.connection.ConnectionFactory;
import com.doacoes.model.Estoque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstoqueDAO {

    public void criarTabela() {

        String sql = """
                CREATE TABLE IF NOT EXISTS estoque (
                    id SERIAL PRIMARY KEY,
                    descricao VARCHAR(200) NOT NULL,
                    quantidade INT NOT NULL DEFAULT 0,
                    categoria VARCHAR(100) NOT NULL
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

    public void salvar(Estoque estoque) {

        String sql = "INSERT INTO estoque(descricao, quantidade, categoria) VALUES (?, ?, ?)";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, estoque.getDescricao());
            statement.setInt(2, estoque.getQuantidade());
            statement.setString(3, estoque.getCategoria());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Estoque> listar() {

        List<Estoque> lista = new ArrayList<>();

        String sql = "SELECT * FROM estoque ORDER BY id";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Estoque estoque = new Estoque();

                estoque.setId(resultSet.getLong("id"));
                estoque.setDescricao(resultSet.getString("descricao"));
                estoque.setQuantidade(resultSet.getInt("quantidade"));
                estoque.setCategoria(resultSet.getString("categoria"));

                lista.add(estoque);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public Estoque buscarPorId(Long id) {

        String sql = "SELECT * FROM estoque WHERE id = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Estoque estoque = new Estoque();

                estoque.setId(resultSet.getLong("id"));
                estoque.setDescricao(resultSet.getString("descricao"));
                estoque.setQuantidade(resultSet.getInt("quantidade"));
                estoque.setCategoria(resultSet.getString("categoria"));

                return estoque;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public Estoque buscarPorDescricaoECategoria(String descricao, String categoria) {

        String sql = """
                SELECT * FROM estoque
                WHERE UPPER(descricao) = UPPER(?)
                  AND UPPER(categoria) = UPPER(?)
                LIMIT 1
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, descricao);
            statement.setString(2, categoria);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Estoque estoque = new Estoque();

                estoque.setId(resultSet.getLong("id"));
                estoque.setDescricao(resultSet.getString("descricao"));
                estoque.setQuantidade(resultSet.getInt("quantidade"));
                estoque.setCategoria(resultSet.getString("categoria"));

                return estoque;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void atualizar(Estoque estoque) {

        String sql = """
                UPDATE estoque
                SET descricao = ?, quantidade = ?, categoria = ?
                WHERE id = ?
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, estoque.getDescricao());
            statement.setInt(2, estoque.getQuantidade());
            statement.setString(3, estoque.getCategoria());
            statement.setLong(4, estoque.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletar(Long id) {

        String sql = "DELETE FROM estoque WHERE id = ?";

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

    public void adicionarQuantidade(Long id, int quantidade) {

        String sql = "UPDATE estoque SET quantidade = quantidade + ? WHERE id = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, quantidade);
            statement.setLong(2, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removerQuantidade(Long id, int quantidade) {

        String sql = "UPDATE estoque SET quantidade = quantidade - ? WHERE id = ? AND quantidade >= ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, quantidade);
            statement.setLong(2, id);
            statement.setInt(3, quantidade);

            int linhasAfetadas = statement.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new RuntimeException("Quantidade insuficiente em estoque!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
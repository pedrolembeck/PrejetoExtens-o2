package com.doacoes.dao;

import com.doacoes.connection.ConnectionFactory;
import com.doacoes.model.TamanhoRoupa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TamanhoRoupaDAO {

    public void criarTabela() {
        String sqlCriarTabela = """
                CREATE TABLE IF NOT EXISTS tamanho_roupa (
                    id SERIAL PRIMARY KEY,
                    tamanho VARCHAR(20) NOT NULL,
                    tipo VARCHAR(30) NOT NULL DEFAULT 'ADULTO'
                )
                """;

        String sqlRemoverCategoriaAntiga = """
                ALTER TABLE tamanho_roupa
                DROP COLUMN IF EXISTS categoria
                """;

        String sqlAdicionarTipo = """
                ALTER TABLE tamanho_roupa
                ADD COLUMN IF NOT EXISTS tipo VARCHAR(30) NOT NULL DEFAULT 'ADULTO'
                """;

        String sqlRemoverUniqueAntigo = """
                ALTER TABLE tamanho_roupa
                DROP CONSTRAINT IF EXISTS tamanho_roupa_tamanho_key
                """;

        String sqlRemoverUniqueNovo = """
                ALTER TABLE tamanho_roupa
                DROP CONSTRAINT IF EXISTS uk_tamanho_roupa_tamanho_tipo
                """;

        String sqlRemoverIndexAntigo = """
                DROP INDEX IF EXISTS tamanho_roupa_tamanho_key
                """;

        String sqlRemoverIndexNovo = """
                DROP INDEX IF EXISTS uk_tamanho_roupa_tamanho_tipo
                """;

        String sqlCriarUniqueNovo = """
                ALTER TABLE tamanho_roupa
                ADD CONSTRAINT uk_tamanho_roupa_tamanho_tipo UNIQUE (tamanho, tipo)
                """;

        try (Connection connection = ConnectionFactory.getConnection()) {
            connection.prepareStatement(sqlCriarTabela).executeUpdate();
            connection.prepareStatement(sqlRemoverCategoriaAntiga).executeUpdate();
            connection.prepareStatement(sqlAdicionarTipo).executeUpdate();
            connection.prepareStatement(sqlRemoverUniqueAntigo).executeUpdate();
            connection.prepareStatement(sqlRemoverUniqueNovo).executeUpdate();
            connection.prepareStatement(sqlRemoverIndexAntigo).executeUpdate();
            connection.prepareStatement(sqlRemoverIndexNovo).executeUpdate();
            connection.prepareStatement(sqlCriarUniqueNovo).executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void salvar(TamanhoRoupa tamanhoRoupa) {
        String sql = "INSERT INTO tamanho_roupa(tamanho, tipo) VALUES (?, ?)";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, tamanhoRoupa.getTamanho());
            statement.setString(2, tamanhoRoupa.getTipo());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TamanhoRoupa> listar() {
        List<TamanhoRoupa> lista = new ArrayList<>();

        String sql = "SELECT * FROM tamanho_roupa ORDER BY tipo, tamanho";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                TamanhoRoupa tamanho = new TamanhoRoupa();

                tamanho.setId(resultSet.getLong("id"));
                tamanho.setTamanho(resultSet.getString("tamanho"));
                tamanho.setTipo(resultSet.getString("tipo"));

                lista.add(tamanho);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public TamanhoRoupa buscarPorId(Long id) {
        String sql = "SELECT * FROM tamanho_roupa WHERE id = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                TamanhoRoupa tamanho = new TamanhoRoupa();

                tamanho.setId(resultSet.getLong("id"));
                tamanho.setTamanho(resultSet.getString("tamanho"));
                tamanho.setTipo(resultSet.getString("tipo"));

                return tamanho;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void atualizar(TamanhoRoupa tamanhoRoupa) {
        String sql = """
                UPDATE tamanho_roupa
                SET tamanho = ?, tipo = ?
                WHERE id = ?
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, tamanhoRoupa.getTamanho());
            statement.setString(2, tamanhoRoupa.getTipo());
            statement.setLong(3, tamanhoRoupa.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM tamanho_roupa WHERE id = ?";

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
}
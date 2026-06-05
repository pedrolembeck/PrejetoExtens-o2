package com.doacoes.dao;

import com.doacoes.connection.ConnectionFactory;
import com.doacoes.model.Doacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoacaoDAO {

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS doacao (
                    id SERIAL PRIMARY KEY,
                    nome_doador VARCHAR(150) NOT NULL,
                    descricao_item VARCHAR(200) NOT NULL,
                    quantidade INT NOT NULL,
                    categoria VARCHAR(100) NOT NULL,
                    tamanho VARCHAR(20),
                    data_doacao DATE NOT NULL,
                    observacao VARCHAR(300)
                )
                """;

        String sqlAlterTamanho = """
                ALTER TABLE doacao
                ADD COLUMN IF NOT EXISTS tamanho VARCHAR(20)
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                PreparedStatement statementAlter = connection.prepareStatement(sqlAlterTamanho)
        ) {
            statement.executeUpdate();
            statementAlter.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void salvar(Doacao doacao) {
        String sql = """
                INSERT INTO doacao(nome_doador, descricao_item, quantidade, categoria, tamanho, data_doacao, observacao)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, doacao.getNomeDoador());
            statement.setString(2, doacao.getDescricaoItem());
            statement.setInt(3, doacao.getQuantidade());
            statement.setString(4, doacao.getCategoria());
            statement.setString(5, doacao.getTamanho());
            statement.setDate(6, Date.valueOf(doacao.getDataDoacao()));
            statement.setString(7, doacao.getObservacao());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Doacao> listar() {
        List<Doacao> lista = new ArrayList<>();

        String sql = "SELECT * FROM doacao ORDER BY id";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                lista.add(mapearDoacao(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public Doacao buscarPorId(Long id) {
        String sql = "SELECT * FROM doacao WHERE id = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapearDoacao(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void atualizar(Doacao doacao) {
        String sql = """
                UPDATE doacao
                SET nome_doador = ?, descricao_item = ?, quantidade = ?, categoria = ?, tamanho = ?, data_doacao = ?, observacao = ?
                WHERE id = ?
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, doacao.getNomeDoador());
            statement.setString(2, doacao.getDescricaoItem());
            statement.setInt(3, doacao.getQuantidade());
            statement.setString(4, doacao.getCategoria());
            statement.setString(5, doacao.getTamanho());
            statement.setDate(6, Date.valueOf(doacao.getDataDoacao()));
            statement.setString(7, doacao.getObservacao());
            statement.setLong(8, doacao.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM doacao WHERE id = ?";

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

    private Doacao mapearDoacao(ResultSet resultSet) throws SQLException {
        Doacao doacao = new Doacao();

        doacao.setId(resultSet.getLong("id"));
        doacao.setNomeDoador(resultSet.getString("nome_doador"));
        doacao.setDescricaoItem(resultSet.getString("descricao_item"));
        doacao.setQuantidade(resultSet.getInt("quantidade"));
        doacao.setCategoria(resultSet.getString("categoria"));
        doacao.setTamanho(resultSet.getString("tamanho"));
        doacao.setDataDoacao(resultSet.getDate("data_doacao").toLocalDate());
        doacao.setObservacao(resultSet.getString("observacao"));

        return doacao;
    }
    public List<Doacao> listarPorDoador(String nomeDoador) {
        List<Doacao> lista = new ArrayList<>();

        String sql = """
            SELECT * FROM doacao
            WHERE UPPER(nome_doador) LIKE UPPER(?)
            ORDER BY id
            """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, "%" + nomeDoador + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    lista.add(mapearDoacao(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }
    public List<Doacao> pesquisar(String termo) {
        List<Doacao> lista = new ArrayList<>();

        String sql = """
            SELECT * FROM doacao
            WHERE UPPER(nome_doador) LIKE UPPER(?)
               OR UPPER(descricao_item) LIKE UPPER(?)
               OR UPPER(categoria) LIKE UPPER(?)
               OR UPPER(COALESCE(tamanho, '')) LIKE UPPER(?)
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

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    lista.add(mapearDoacao(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

}
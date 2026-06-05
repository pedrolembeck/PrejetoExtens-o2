package com.doacoes.dao;

import com.doacoes.connection.ConnectionFactory;
import com.doacoes.enums.CategoriaItem;
import com.doacoes.model.TipoDoacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoDoacaoDAO {

    public void salvar(TipoDoacao tipoDoacao) {

        String sql = "INSERT INTO tipo_doacao(nome, categoria) VALUES (?, ?)";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, tipoDoacao.getNome());
            statement.setString(2, tipoDoacao.getCategoria().name());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TipoDoacao> listar() {

        List<TipoDoacao> lista = new ArrayList<>();

        String sql = "SELECT * FROM tipo_doacao";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                TipoDoacao tipo = new TipoDoacao();

                tipo.setId(resultSet.getLong("id"));
                tipo.setNome(resultSet.getString("nome"));
                String categoriaStr = resultSet.getString("categoria");
                CategoriaItem categoria = resolverCategoria(categoriaStr);
                tipo.setCategoria(categoria);

                lista.add(tipo);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public TipoDoacao buscarPorId(Long id) {

        String sql = "SELECT * FROM tipo_doacao WHERE id = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                TipoDoacao tipo = new TipoDoacao();

                tipo.setId(resultSet.getLong("id"));
                tipo.setNome(resultSet.getString("nome"));
                String categoriaStr2 = resultSet.getString("categoria");
                tipo.setCategoria(resolverCategoria(categoriaStr2));

                return tipo;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void atualizar(TipoDoacao tipoDoacao) {

        String sql = """
                UPDATE tipo_doacao
                SET nome = ?, categoria = ?
                WHERE id = ?
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, tipoDoacao.getNome());
            statement.setString(2, tipoDoacao.getCategoria().name());
            statement.setLong(3, tipoDoacao.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletar(Long id) {

        String sql = "DELETE FROM tipo_doacao WHERE id = ?";

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

    /**
     * Converte o valor da coluna 'categoria' do banco para o enum CategoriaItem.
     * Aceita: nome ("VESTUARIO"), número ("1"), ou null/vazio (retorna ALIMENTOS como padrão).
     */
    private CategoriaItem resolverCategoria(String valor) {
        if (valor == null || valor.isBlank()) {
            return CategoriaItem.ALIMENTOS; // valor padrão se vier nulo do banco
        }
        try {
            return CategoriaItem.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            try {
                return CategoriaItem.porCodigo(Integer.parseInt(valor.trim()));
            } catch (Exception ex) {
                return CategoriaItem.ALIMENTOS; // fallback seguro
            }
        }
    }
}
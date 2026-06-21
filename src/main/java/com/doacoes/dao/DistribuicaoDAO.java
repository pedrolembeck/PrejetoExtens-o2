package com.doacoes.dao;

import com.doacoes.connection.ConnectionFactory;
import com.doacoes.model.Distribuicao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DistribuicaoDAO {

    public void criarTabela() {
        String sql = """
            CREATE TABLE IF NOT EXISTS distribuicao (
                id SERIAL PRIMARY KEY,
                id_receptor BIGINT NOT NULL,
                id_estoque BIGINT NOT NULL,
                quantidade INT NOT NULL,
                data_distribuicao TIMESTAMP NOT NULL
            )
        """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void salvar(Distribuicao d) {
        String sql = """
            INSERT INTO distribuicao (id_receptor, id_estoque, quantidade, data_distribuicao)
            VALUES (?, ?, ?, ?)
        """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {
            st.setLong(1, d.getReceptorId());
            st.setLong(2, d.getEstoqueId());
            st.setInt(3, d.getQuantidade());
            st.setTimestamp(4, Timestamp.valueOf(d.getData()));

            st.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Distribuicao d) {
        String sql = """
            UPDATE distribuicao
            SET id_receptor = ?,
                id_estoque = ?,
                quantidade = ?,
                data_distribuicao = ?
            WHERE id = ?
        """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {
            st.setLong(1, d.getReceptorId());
            st.setLong(2, d.getEstoqueId());
            st.setInt(3, d.getQuantidade());
            st.setTimestamp(4, Timestamp.valueOf(d.getData()));
            st.setLong(5, d.getId());

            st.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Distribuicao buscarPorId(Long id) {
        String sql = """
            SELECT 
                d.id,
                d.id_receptor,
                r.nome AS receptor_nome,
                r.cpf AS receptor_cpf,
                d.id_estoque,
                e.descricao AS estoque_descricao,
                d.quantidade,
                d.data_distribuicao
            FROM distribuicao d
            JOIN receptor r ON r.id = d.id_receptor
            JOIN estoque e ON e.id = d.id_estoque
            WHERE d.id = ?
        """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {
            st.setLong(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return mapearDistribuicao(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public List<Distribuicao> listar() {
        List<Distribuicao> lista = new ArrayList<>();

        String sql = """
            SELECT 
                d.id,
                d.id_receptor,
                r.nome AS receptor_nome,
                r.cpf AS receptor_cpf,
                d.id_estoque,
                e.descricao AS estoque_descricao,
                d.quantidade,
                d.data_distribuicao
            FROM distribuicao d
            JOIN receptor r ON r.id = d.id_receptor
            JOIN estoque e ON e.id = d.id_estoque
            ORDER BY d.id DESC
        """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement st = conn.prepareStatement(sql);
                ResultSet rs = st.executeQuery()
        ) {
            while (rs.next()) {
                lista.add(mapearDistribuicao(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM distribuicao WHERE id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {
            st.setLong(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Distribuicao mapearDistribuicao(ResultSet rs) throws SQLException {
        Distribuicao d = new Distribuicao();

        d.setId(rs.getLong("id"));
        d.setReceptorId(rs.getLong("id_receptor"));
        d.setNomeReceptor(rs.getString("receptor_nome"));
        d.setCpfReceptor(rs.getString("receptor_cpf"));
        d.setEstoqueId(rs.getLong("id_estoque"));
        d.setDescricaoEstoque(rs.getString("estoque_descricao"));
        d.setQuantidade(rs.getInt("quantidade"));
        d.setData(rs.getTimestamp("data_distribuicao").toLocalDateTime());

        return d;
    }
}
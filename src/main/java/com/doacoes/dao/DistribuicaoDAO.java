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

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
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

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            System.out.println(">>> ANTES DO EXECUTE");

            st.setLong(1, d.getReceptorId());
            st.setLong(2, d.getEstoqueId());
            st.setInt(3, d.getQuantidade());
            st.setTimestamp(4, Timestamp.valueOf(d.getData()));

            int rows = st.executeUpdate();

            System.out.println(">>> LINHAS INSERIDAS: " + rows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void atualizar(Distribuicao d) {
        String sql = """
        UPDATE distribuicao
        SET id_receptor = ?, id_estoque = ?, quantidade = ?, data_distribuicao = ?
        WHERE id = ?
    """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

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
        String sql = "SELECT * FROM distribuicao WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setLong(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Distribuicao d = new Distribuicao();
                    d.setId(rs.getLong("id"));
                    d.setReceptorId(rs.getLong("id_receptor"));
                    d.setEstoqueId(rs.getLong("id_estoque"));
                    d.setQuantidade(rs.getInt("quantidade"));
                    d.setData(rs.getTimestamp("data_distribuicao").toLocalDateTime());
                    return d;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }public List<Distribuicao> listar() {
        List<Distribuicao> lista = new ArrayList<>();

        String sql = """
        SELECT 
            d.id,
            d.id_receptor,
            r.cpf AS receptor_cpf,
            d.id_estoque,
            d.quantidade,
            d.data_distribuicao
        FROM distribuicao d
        JOIN receptor r ON r.id = d.id_receptor
        ORDER BY d.id DESC
    """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Distribuicao d = new Distribuicao();

                d.setId(rs.getLong("id"));
                d.setReceptorId(rs.getLong("id_receptor"));
                d.setEstoqueId(rs.getLong("id_estoque"));
                d.setQuantidade(rs.getInt("quantidade"));
                d.setData(rs.getTimestamp("data_distribuicao").toLocalDateTime());

                d.setCpfReceptor(rs.getString("receptor_cpf"));
                d.setCpfReceptor(rs.getString("receptor_cpf"));

                lista.add(d);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }
    public void deletar(Long id) {
        String sql = "DELETE FROM distribuicao WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setLong(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
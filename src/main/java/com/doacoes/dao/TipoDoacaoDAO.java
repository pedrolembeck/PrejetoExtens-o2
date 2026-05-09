package com.doacoes.dao;

import com.doacoes.connection.ConnectionFactory;
import com.doacoes.model.TipoDoacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TipoDoacaoDAO {

    public void criarTabela() {

        String sql = """
                CREATE TABLE IF NOT EXISTS tipo_doacao (
                    id SERIAL PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL
                )
                """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.execute();

            System.out.println("Tabela tipo_doacao criada com sucesso!");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void salvar(TipoDoacao tipo) {

        String sql = """
                INSERT INTO tipo_doacao(nome)
                VALUES (?)
                """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, tipo.getNome());

            stmt.executeUpdate();

            System.out.println("Tipo de doação salvo!");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public List<TipoDoacao> listar() {

        List<TipoDoacao> lista = new ArrayList<>();

        String sql = """
                SELECT * FROM tipo_doacao
                ORDER BY id
                """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                TipoDoacao tipo = new TipoDoacao();

                tipo.setId(rs.getInt("id"));
                tipo.setNome(rs.getString("nome"));

                lista.add(tipo);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }
}
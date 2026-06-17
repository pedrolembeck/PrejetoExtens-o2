package com.doacoes.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL =
            "jdbc:postgresql://tramway.proxy.rlwy.net:24848/railway";

    private static final String USER = "postgres";

    private static final String PASSWORD =
                "FsjprDHmahJGjMCXTVrbWhfsriLlYwiM";

    public static Connection getConnection() {

        try {

            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {

            throw new RuntimeException("Erro ao conectar com o banco", e);
        }
    }
}
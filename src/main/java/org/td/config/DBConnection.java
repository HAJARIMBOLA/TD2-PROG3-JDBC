package org.td.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private final Dotenv dotenv;

    public DBConnection() {
        this.dotenv = Dotenv.load();
    }

    public Connection getConnection() {
        try {
            String jdbcUrl = dotenv.get("DB_URL");
            String user = dotenv.get("DB_USERNAME");
            String password = dotenv.get("DB_PASSWORD");

            return DriverManager.getConnection(jdbcUrl, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion à la base de données", e);
        }
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(
                        "Erreur lors de la fermeture de la connexion",
                        e
                );
            }
        }
    }
}


package config;

// gere la connexion a la base de donnees PostgreSQL
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/hotely",
                "postgres",
                "azerty"
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

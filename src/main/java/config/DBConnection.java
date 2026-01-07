
package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/restaurant",
                "postgres",
                "postgres"
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

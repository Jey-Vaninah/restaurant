package hei.vaninah.devoir.repository.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseConnection {
    private Connection connection;
    private final String DB_USERNAME = System.getenv("DB_USERNAME");
    private final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    private final String DB_URL = System.getenv("DB_URL");

    @Bean
    public Connection getConnection() {
        if (connection != null) {
            return connection;
        }

        try {
            connection = DriverManager.getConnection(
                    DB_URL,
                    DB_USERNAME,
                    DB_PASSWORD
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error happened when try to connect to database", e);
        }
        return connection;
    }

    public void closeConnection() {
        if (connection == null) {
            return;
        }

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
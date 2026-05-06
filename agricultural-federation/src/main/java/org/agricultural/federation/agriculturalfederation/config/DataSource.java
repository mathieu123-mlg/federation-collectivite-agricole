package org.agricultural.federation.agriculturalfederation.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
@Profile("!test")
public class DataSource {

    @Bean
    public Connection getConnection() {
        try {
            Dotenv dotenv = Dotenv.load();
            String jdbc_url = dotenv.get("JDBC_URL");
            String user = dotenv.get("USER");
            String password = dotenv.get("PASSWORD");

            // Fallback to null if environment variables are missing (e.g., in tests)
            if (jdbc_url == null || user == null || password == null) {
                System.err.println("Warning: Database configuration not found in .env file. Returning null connection.");
                return null;
            }

            return DriverManager.getConnection(jdbc_url, user, password);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

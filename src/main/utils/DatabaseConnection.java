package com.sms.utils;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static DatabaseConnection instance;
    private Connection connection;
    private final String url;
    private final String username;
    private final String password;

    private DatabaseConnection() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String supabaseUrl = dotenv.get("SUPABASE_URL");
        String supabaseKey = dotenv.get("SUPABASE_ANON_KEY");

        if (supabaseUrl == null || supabaseUrl.isEmpty()) {
            logger.error("SUPABASE_URL not found in environment variables");
            throw new RuntimeException("Database configuration missing");
        }

        String projectRef = supabaseUrl.replace("https://", "").replace(".supabase.co", "");
        this.url = String.format("jdbc:postgresql://aws-0-ap-south-1.pooler.supabase.com:6543/postgres?sslmode=require");
        this.username = String.format("postgres.%s", projectRef);
        this.password = supabaseKey;
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(url, username, password);
                logger.info("Database connection established successfully");
            } catch (ClassNotFoundException e) {
                logger.error("PostgreSQL Driver not found", e);
                throw new SQLException("Database driver not found", e);
            } catch (SQLException e) {
                logger.error("Failed to establish database connection", e);
                throw e;
            }
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Database connection closed");
            } catch (SQLException e) {
                logger.error("Error closing database connection", e);
            }
        }
    }
}

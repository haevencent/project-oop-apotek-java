package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String url = System.getenv("DB_URL");
        if (url == null || url.isEmpty()) {
            throw new SQLException("Environment variable DB_URL belum diatur.");
        }
        return DriverManager.getConnection(url);
    }
}
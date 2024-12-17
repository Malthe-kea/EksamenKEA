package org.example.eksamenkea.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    private static Connection connection;

    private ConnectionManager() {
    }

    public static Connection getConnection() throws SQLException {
        synchronized (ConnectionManager.class) { //sikrer, at kun én tråd ad gangen kan oprette en ny forbindelse
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
            return connection;
        }
    }
}



package ru.nosova.flowershop.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DButils {
    private static Connection connection;
    static final String url ="jdbc:postgresql://localhost:5433/FlowerShop";
    final static String user = "postgres";
    final static String password = "admin";

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
    public static void close() throws SQLException {
        connection.close();
    }
}

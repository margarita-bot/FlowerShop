package ru.nosova.flowershop.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBHelper {
    private static Connection connection;
    private static String dbUrlBase;
    private static String dbName;
    private static String dbUser;
    private static String dbPassword;

    static {
        try {
            URL url = DBHelper.class.getResource("/config.properties");
            if (url == null) {
                throw new RuntimeException("config.properties не найден!");
            }

            Properties prop = new Properties();
            try (FileInputStream fis = new FileInputStream(url.getFile())) {
                prop.load(fis);
                dbUrlBase = prop.getProperty("db.url");
                dbName = prop.getProperty("db.name");
//                dbUser = prop.getProperty("db.user");
//                dbPassword = prop.getProperty("db.password");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Для авторизации - подключаемся с переданным пользователем и паролем
    public static void initConnection(String user, String password) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            closeConnection();
        }

        String fullUrl = dbUrlBase + dbName;
        System.out.println("Подключение к " + fullUrl + " пользователем " + user);
        connection = DriverManager.getConnection(fullUrl, user, password);
        System.out.println("Соединение установлено");
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Соединение закрыто");
            } catch (SQLException ex) {
                System.err.println("Ошибка закрытия соединения: " + ex.getMessage());
            }
        }
    }
}

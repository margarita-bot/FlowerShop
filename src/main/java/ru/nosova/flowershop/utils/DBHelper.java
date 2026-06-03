package ru.nosova.flowershop.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBHelper {
    private static final Logger logger = LoggerFactory.getLogger(DBHelper.class);
    private static Connection connection;
    private static String dbUrlBase;
    private static String dbName;

    static {
        try {
            URL url = DBHelper.class.getResource("/config.properties");
            if (url == null) {
                logger.error("config.properties не найден!");
            }
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream(url.getFile())) {
                properties.load(fis);
                dbUrlBase = properties.getProperty("db.url");
                dbName = properties.getProperty("db.name");
            }
        } catch (IOException ex) {
            logger.error("Ошибка чтения config.properties", ex);
        }
    }

    public static void initConnection(String user, String password) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            closeConnection();
        }
        String fullUrl = dbUrlBase + dbName;
        connection = DriverManager.getConnection(fullUrl, user, password);
        logger.info("Подключение к БД установлено пользователем " + user);
    }
    public static Connection getConnection() {
        if (connection == null) {
            logger.error("config.properties не найден");
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Соединение с БД закрыто");
            } catch (SQLException ex) {
                logger.error("Ошибка закрытия соединения", ex);
            }
        }
    }
}

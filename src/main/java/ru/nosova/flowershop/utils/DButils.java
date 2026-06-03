package ru.nosova.flowershop.utils;

import java.sql.Connection;
import java.sql.SQLException;

public class DButils {
public static Connection getConnection() {
    return DBHelper.getConnection();
}

    public static void close() throws SQLException {
        DBHelper.closeConnection();
    }
}

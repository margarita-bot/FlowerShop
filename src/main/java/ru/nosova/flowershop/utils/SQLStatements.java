package ru.nosova.flowershop.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class SQLStatements {
    private final Properties property = new Properties();

    public SQLStatements() {
        try {
            URL url = this.getClass().getResource("/statements.properties");
            if (url == null) {
                throw new RuntimeException("statements.properties не найден!");
            }
            try (FileInputStream fis = new FileInputStream(url.getFile())) {
                property.load(fis);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка загрузки statements.properties", e);
        }
    }

    public String getQuery(String key) {
        String query = property.getProperty(key);
        if (query == null) {
            throw new IllegalArgumentException("Запрос с ключом '" + key + "' не найден!");
        }
        return query;
    }
}

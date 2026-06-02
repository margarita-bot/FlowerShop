package ru.nosova.flowershop.utils;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;


public class LocaleSettings {
    @Getter
    private static Locale currentLocale;
    private static ResourceBundle bundle;
    private static final String CONFIG = "config.properties";

    public static final Locale RUSSIAN = new Locale("ru", "RU");
    public static final Locale GERMAN = new Locale("de", "DE");
    public static final Locale ENGLISH = new Locale("en", "EN");

    static {
        loadSavedLocale();
    }
    private static void loadSavedLocale(){
        try (FileInputStream file = new FileInputStream(CONFIG)){
            Properties properties = new Properties();
            properties.load(file);
            String lang = properties.getProperty("app.language", "en");
            String country = properties.getProperty("app.country", "EN");
            currentLocale = new Locale(lang, country);
        } catch (IOException e) {
            currentLocale = LocaleSettings.ENGLISH;
            saveLocale(currentLocale);
        }
        bundle = ResourceBundle.getBundle("location", currentLocale);
    }

    public static void saveLocale(Locale locale){
        Properties properties = new Properties();
        properties.setProperty("app.language", locale.getLanguage());
        properties.setProperty("app.country", locale.getCountry());
        try (FileOutputStream out = new FileOutputStream(CONFIG)) {
            properties.store(out, "Application Configuration");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("location", currentLocale);
        saveLocale(locale);
    }

    public static ResourceBundle getCurrentBundle() {
        return bundle;
    }
}

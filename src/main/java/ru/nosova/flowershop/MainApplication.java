package ru.nosova.flowershop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import ru.nosova.flowershop.controller.MainController;
import ru.nosova.flowershop.dao.OrdersDao;
import ru.nosova.flowershop.utils.LocaleSettings;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainApplication extends Application {
    @Getter
    private static Stage stage;
    private static final Logger logger = Logger.getLogger(String.valueOf(MainApplication.class));

    @Override
    public void start(Stage stage) throws IOException {
        ResourceBundle bundle = LocaleSettings.getCurrentBundle();

        MainApplication.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("user-view.fxml"), bundle);
//        OrdersDao ordersDao = new OrdersDao();
//        fx mlLoader.setControllerFactory(param -> new MainController(ordersDao));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Цветочный магазин.");
        stage.setScene(scene);
        stage.show();
    }

    public static void showMainWindow() {
        try {
            ResourceBundle bundle = LocaleSettings.getCurrentBundle();
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("FlowerShop-view.fxml"), bundle);
            Scene scene = new Scene(loader.load());
            stage.setTitle("Цветочный магазин");
            stage.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
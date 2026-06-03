package ru.nosova.flowershop;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;

public class MainTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws IOException {
        new MainApplication().start(stage);
    }
    @Test
    @SneakyThrows
    @DisplayName("авторизация")
    void testAv(){
        String log = "test";
        String pass = "123";
        clickOn("#login").write(log);
        clickOn("#pass").write(pass);
        clickOn("Войти");
    }
}

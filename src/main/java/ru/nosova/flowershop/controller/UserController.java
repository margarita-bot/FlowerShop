package ru.nosova.flowershop.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.nosova.flowershop.MainApplication;
import ru.nosova.flowershop.utils.DBHelper;

import java.sql.SQLException;

public class UserController {

    @FXML private TextField login;
    @FXML private TextField pass;

    @Setter
    private Stage dialogStage;

    @FXML
    void onClose(ActionEvent event) {
        Platform.exit();
    }

    public void onOk(ActionEvent event) {
        String u = login.getText().trim();
        String p = pass.getText().trim();
        if(u.isEmpty() || p.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка ввода");
            alert.setHeaderText(null);
            alert.setContentText("Введите логин и пароль.");
            alert.showAndWait();
            return;
        }
        try {
            DBHelper.initConnection(u, p);
            MainApplication.showMainWindow();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка авторизации");
            alert.setHeaderText("Не удалось выполнить вход");
            alert.setContentText("Неверный логин или пароль.");
            alert.showAndWait();
        }
    }

    public LoginResult getLoginResult() {
            return new LoginResult(login.getText().trim(), pass.getText().trim());
    }
    @Getter
    @AllArgsConstructor
    public static class LoginResult {
        private final String username;
        private final String password;
    }
}

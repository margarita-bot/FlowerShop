package ru.nosova.flowershop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.nosova.flowershop.MainApplication;
import ru.nosova.flowershop.utils.DBHelper;

import java.sql.SQLException;

public class UserController {
    @Getter
    private boolean okClicked = false;

    @FXML private TextField login;
    @FXML private TextField pass;

    @Setter
    private Stage dialogStage;

    @FXML
    void onClose(ActionEvent event) {
        okClicked = false;
        dialogStage.close();
    }

    public void onOk(ActionEvent event) {
        String u = login.getText().trim();
        String p = pass.getText().trim();
        if(u.isEmpty() || p.isEmpty()){
            return;
        }
        try {
            DBHelper.initConnection(u, p);

            okClicked = true;
            MainApplication.showMainWindow();

        } catch (SQLException e) {
            // Ошибка подключения — неверный логин/пароль
            System.err.println("Ошибка подключения: " + e.getMessage());
        }
    }

    public LoginResult getLoginResult() {
        if (okClicked) {
            return new LoginResult(login.getText().trim(), pass.getText().trim());
        }
        return null;
    }
    @Getter
    @AllArgsConstructor
    public static class LoginResult {
        private final String username;
        private final String password;
    }
}

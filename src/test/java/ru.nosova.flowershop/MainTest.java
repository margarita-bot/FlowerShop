package ru.nosova.flowershop;

import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import ru.nosova.flowershop.dao.ClientDao;
import ru.nosova.flowershop.dao.impl.ClientDaoImpl;
import ru.nosova.flowershop.model.Client;
import ru.nosova.flowershop.utils.DBHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MainTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws IOException {
        new MainApplication().start(stage);
    }
    @Test
    @SneakyThrows
    @DisplayName("авторизация с ошибкой")
    void tesАtuthorizationError(){
        String log = "test";
        String pass = "123";
        clickOn("Войти");
    }

    @Test
    @SneakyThrows
    @DisplayName("авторизация")
    void testAuthorization(){
        String log = "florist";
        String pass = "1234567";
        clickOn("#login").write(log);
        clickOn("#pass").write(pass);
        clickOn("Войти");
    }

    @Test
    @SneakyThrows
    @DisplayName("Добавление с ошибкой")
    void testAddClientError(){
        String log = "florist";
        String pass = "1234567";
        clickOn("#login").write(log);
        clickOn("#pass").write(pass);
        clickOn("Войти");
        clickOn("#idMenuBox");
        clickOn("Клиенты");
        clickOn("Добавить");
    }

    List<Client> clientToClean = new ArrayList<>();
    ClientDaoImpl<Client, Long> clientDao = new ClientDao();
    @Test
    @SneakyThrows
    @DisplayName("Добавление")
    void testAddClient(){
        String log = "florist";
        String pass = "1234567";
        String name = "Валера";
        String lastname = "Киркоров";
        String phone = "89347236518";
        String email = "wersd@gmail.com";
        clickOn("#login").write(log);
        clickOn("#pass").write(pass);
        clickOn("Войти");
        clickOn("#idMenuBox");
        clickOn("Клиенты");
        clickOn("#addName").write(name);
        clickOn("#addLastname").write(lastname);
        clickOn("#addPhone").write(phone);
        clickOn("#addEmail").write(email);
        clickOn("Добавить");
        sleep(200, TimeUnit.MILLISECONDS);
        boolean find = false;
        TableView<Client> tb = lookup("#ClientTable").queryTableView();
        for(Client client : tb.getItems())
            if (Objects.equals(client.getLastName(), lastname)) {
                find = true;
                break;
            }
        assertThat(find).isTrue();
        List<Client> del = clientDao.findClient(name, lastname, phone);
        if(!del.isEmpty()){
            clientToClean.addAll(del);
        }

    }
    @AfterEach
    void cleanUp() throws SQLException {
        List<Client> existing = clientDao.findClient("Валера", "Киркоров", "89347236518");
        for (Client c : existing) {
            clientDao.delete(c);
        }
        clientToClean.clear();
    }
}

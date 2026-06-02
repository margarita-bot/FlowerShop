package ru.nosova.flowershop.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nosova.flowershop.MainApplication;
import ru.nosova.flowershop.dao.ClientDao;
import ru.nosova.flowershop.dao.impl.ClientDaoImpl;
import ru.nosova.flowershop.model.Client;
import ru.nosova.flowershop.utils.LocaleSettings;
import ru.nosova.flowershop.utils.Validation;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @FXML private Label idErrorFind;
    @FXML private Label idErrorEdit;
    @FXML private TextField addEmail;
    @FXML private TextField addLastname;
    @FXML private TextField addMiidllename;
    @FXML private TextField addName;
    @FXML private TextField addPhone;
    @FXML private ComboBox<String> boxMenu;
    @FXML private Label idErrorAdd;
    @FXML private TableView<Client> ClientTable;
    @FXML private TableColumn<Client, String> nameColumn;
    @FXML private TableColumn<Client, String> lastnameColumn;
    @FXML private TableColumn<Client, String> midllenameColumn;
    @FXML private TableColumn<Client, String> phoneColumn;
    @FXML private TableColumn<Client, String> emailColumn;
    @FXML private TextField textLastname;
    @FXML private TextField textName;
    @FXML private TextField textPhone;

    @FXML private TextField editName;
    @FXML private TextField editLastname;
    @FXML private TextField editMiidllename;
    @FXML private TextField editEmail;
    @FXML private TextField editPhone;

    private final ObservableList<Client> clients = FXCollections.observableArrayList();
    private ClientDaoImpl<Client, Long> clientDaoImpl;
    public ClientController(){
    }
    @FXML
    void initialize(){
        clientDaoImpl = new ClientDao();
        clients.addAll(clientDaoImpl.getClient());
        nameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getFirstName()));
        lastnameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getLastName()));
        midllenameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getMiddleName()));
        phoneColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getPhone()));
        emailColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getEmail()));
        ClientTable.setItems(clients);

        ClientTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Client>() {
            @Override
            public void changed(ObservableValue<? extends Client> observableValue, Client oldClient, Client current) {
                if (current != null) {
                    editName.setText(current.getFirstName());
                    editLastname.setText(current.getLastName());
                    editMiidllename.setText(current.getMiddleName());
                    editEmail.setText(current.getEmail());
                    editPhone.setText(current.getPhone());
                }
            }
        });

        boxMenu.getItems().addAll("Букет", "Склад");
    }

    private void openWindow(String path) {
        logger.info("Open window: " + path);
        ResourceBundle bundle = LocaleSettings.getCurrentBundle();
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(path), bundle);
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            logger.warn("Window opening error " + path + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
        Stage stage = MainApplication.getStage();

        //stage.setFullScreen(true);
        stage.setScene(scene);

        stage.show();
    }

    @FXML
    void onBackMain(ActionEvent event) {
        openWindow("/ru/nosova/flowershop/FlowerShop-view.fxml");
    }

    @FXML
    void onBoxMenu(ActionEvent event) {
        String selected = boxMenu.getValue();
        switch (selected) {
            case "Букет": openWindow("/ru/nosova/flowershop/bouquet-view.fxml");break;

            case "Склад": openWindow("/ru/nosova/flowershop/flowers-view.fxml");break;
        }
    }

    @FXML
    void onButtonAdd(ActionEvent event) {
        String name = addName.getText().trim();
        String lastName = addLastname.getText().trim();
        String middleName = addMiidllename.getText().trim();
        String email = addEmail.getText().trim();
        String phone = addPhone.getText().trim();

        if (name.isBlank() || lastName.isBlank() || phone.isBlank()){
            idErrorAdd.setText("Заполните обязательные поля.");
            return;
        }

        if(!Validation.isValidName(name)){
            idErrorAdd.setText("Проверьте корректность имени.");
            return;
        }
        if (!Validation.isValidName(lastName)) {
            idErrorAdd.setText("Проверьте корректность фамилии.");
            return;
        }
        if (!middleName.isBlank() && !Validation.isValidName(middleName)) {
            idErrorAdd.setText("Проверьте корректность отчества.");
            return;
        }
        if (!Validation.isValidPhone(phone)) {
            idErrorAdd.setText("Проверьте корректность телефона.");
            return;
        }
        if (!email.isBlank() && !Validation.isValidEmail(email)) {
            idErrorAdd.setText("Проверьте корректность email.");
            return;
        }

        if (middleName.isBlank()) {
            middleName = null;
        }
        if (email.isBlank()) {
            email = null;
        }
        Client clientS = Client.builder()
                .firstName(name)
                .lastName(lastName)
                .middleName(middleName)
                .email(email)
                .phone(phone)
                .build();
        Client save = clientDaoImpl.insert(clientS);
        clients.add(save);

        ClientTable.setItems(clients);

        ClientTable.getSelectionModel().select(save);

        addName.clear();
        addLastname.clear();
        addMiidllename.clear();
        addEmail.clear();
        addPhone.clear();
    }

    @FXML
    void onButtonFind(ActionEvent event) {
        String name = textName.getText().trim();
        String lastname = textLastname.getText().trim();
        String phone = textPhone.getText().trim();

        if (name.isBlank() && lastname.isBlank() && phone.isBlank()) {
            idErrorFind.setText("Введите хотя бы один параметр поиска.");
            return;
        }

        if(name.isBlank()){name=null;}
        if (lastname.isBlank()){lastname=null;}
        if(phone.isBlank()){phone=null;}

        List<Client> find = clientDaoImpl.findClient(name, lastname, phone);

        clients.clear();
        clients.addAll(find);
        ClientTable.setItems(clients);

        textName.clear();
        textLastname.clear();
        textPhone.clear();
    }

    @FXML
    void onReset(ActionEvent event) {
        textName.clear();
        textLastname.clear();
        textPhone.clear();

        clients.clear();
        clients.addAll(clientDaoImpl.getClient());
    }

    @FXML
    void onDelete(ActionEvent event) {
        Client clientD = ClientTable.getSelectionModel().getSelectedItem();
        if (clientD != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Подтверждение удаления");
            confirm.setHeaderText("Удаление клиента");
            confirm.setContentText(
                    "Вы действительно хотите удалить клиента "
                            + clientD.getLastName() + " "
                            + clientD.getFirstName() + "?"
            );

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                clientDaoImpl.delete(clientD);
                clients.remove(clientD);

                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Удаление");
                success.setHeaderText(null);
                success.setContentText("Клиент успешно удален.");
                success.showAndWait();
            }

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Удаление клиента");
                alert.setHeaderText(null);
                alert.setContentText("Выберете клиента.");
                alert.showAndWait();
            }
    }

    @FXML
    void onEdit(ActionEvent event) {
        Client client = ClientTable.getSelectionModel().getSelectedItem();
        if (client != null){
            String name = editName.getText().trim();
            String lastname = editLastname.getText().trim();
            String middleName = editMiidllename.getText().trim();
            String email = editEmail.getText().trim();
            String phone = editPhone.getText().trim();

            if (name.isBlank() || lastname.isBlank() || phone.isBlank()) {
                idErrorEdit.setText("Заполните обязательные поля.");
                return;
            }
            if(!Validation.isValidName(name)){
                idErrorEdit.setText("Проверьте корректность имени.");
                return;
            }
            if (!Validation.isValidName(lastname)) {
                idErrorEdit.setText("Проверьте корректность фамилии.");
                return;
            }
            if (!Validation.isValidName(middleName)) {
                idErrorEdit.setText("Проверьте корректность отчества.");
                return;
            }
            if (!Validation.isValidPhone(phone)) {
                idErrorEdit.setText("Проверьте корректность телефона.");
                return;
            }
            if (!Validation.isValidEmail(email)) {
                idErrorEdit.setText("Проверьте корректность email.");
                return;
            }

            client.setFirstName(editName.getText());
            client.setLastName(editLastname.getText());
            client.setMiddleName(editMiidllename.getText());
            client.setEmail(editEmail.getText());
            client.setPhone(editPhone.getText());

            clientDaoImpl.update(client);
            ClientTable.refresh();

            editName.clear();
            editLastname.clear();
            editMiidllename.clear();
            editEmail.clear();
            editPhone.clear();

        }else {
            idErrorEdit.setText("Выберете клиента.");
        }
    }

    @FXML
    void onExit(ActionEvent event) {Platform.exit();}

}

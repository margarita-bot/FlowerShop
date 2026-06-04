package ru.nosova.flowershop.controller;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.nosova.flowershop.MainApplication;
import ru.nosova.flowershop.dao.OrdersDao;
import ru.nosova.flowershop.dao.impl.OrdersDaoImpl;
import ru.nosova.flowershop.model.Orders;
import ru.nosova.flowershop.utils.LocaleSettings;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainController {

    @FXML private Label idErrors;
    @FXML private  ComboBox<String> boxSetting;
    @FXML private TableView<Orders> TableOrder;
    @FXML private TableColumn<Orders, LocalDate> columnDate;
    @FXML private TableColumn<Orders, String> columnLastName;
    @FXML private TableColumn<Orders, String> columnMiddliName;
    @FXML private TableColumn<Orders, String> columnName;
    @FXML private TableColumn<Orders, Double> columnPrice;
    @FXML private TableColumn<Orders, String> columnStatus;
    @FXML private TextField findDate;
    @FXML private TextField findLastName;
    @FXML private TextField findName;
    @FXML private TextField findStatus;
    @FXML private TextArea idBouquetText;
    @FXML private Label idCity;
    @FXML private Label idHouse;
    @FXML private Label idLastName;
    @FXML private ComboBox<String> idMenuBox;
    @FXML private Label idMiddliName;
    @FXML private Label idName;
    @FXML private Label idNameFlowers;
    @FXML private Label idPhone;
    @FXML private Label idPriceFlowers;
    @FXML private Label idStreet;

    private final ObservableList<Orders> orders = FXCollections.observableArrayList();
    private OrdersDaoImpl<Orders, Long> ordersDaoImpl;
    public MainController(){
    }

    @FXML
    void initialize(){
        ordersDaoImpl = new OrdersDao();
        orders.addAll(ordersDaoImpl.getOrders());
        columnName.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getClient().getFirstName()));
        columnLastName.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getClient().getLastName()));
        columnMiddliName.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getClient().getMiddleName()));
        columnPrice.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getPrice()));
        columnDate.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getDate()));
        columnStatus.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getStatus().getText()));
        TableOrder.setItems(orders);
        idMenuBox.getItems().addAll("Клиенты",
                "Букет",
                "Склад");

        boxSetting.getItems().addAll(
                "Русский",
                "English",
                "Deutsch"
        );

        Locale current = LocaleSettings.getCurrentLocale();

        if(current.equals(LocaleSettings.RUSSIAN))
            boxSetting.setValue("Русский");

        else if(current.equals(LocaleSettings.GERMAN))
            boxSetting.setValue("Deutsch");

        else
            boxSetting.setValue("English");
    }

    private void openWindow(String path) {
        ResourceBundle bundle = LocaleSettings.getCurrentBundle();
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(path), bundle);
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = MainApplication.getStage();
        //stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();

    }

    @FXML void clikButtonFind(ActionEvent event) {
        String name = findName.getText().trim();
        String lastname = findLastName.getText().trim();
        String status = findStatus.getText().trim();
        LocalDate date = null;
        if (name.isBlank() && lastname.isBlank() && status.isBlank()){
            idErrors.setText("Напишите хотя бы один параметр поиска.");
            return;
        }
        if(!findDate.getText().isEmpty()){
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd.MM.yyyy");

            date = LocalDate.parse(findDate.getText().trim(), formatter);
        }
        if (name.isBlank()){name=null;}
        if(lastname.isBlank()){lastname=null;}
        if(status.isBlank()){status=null;}
        List<Orders> find = ordersDaoImpl.find(name, lastname, status, date);
        orders.clear();
        orders.addAll(find);
        findName.clear();
        findLastName.clear();
        findStatus.clear();
        findDate.clear();
    }



    @FXML
    void clikDelete(ActionEvent event) {
        Orders orderD =
                TableOrder.getSelectionModel().getSelectedItem();

        if (orderD != null) {

            Alert confirm =
                    new Alert(Alert.AlertType.CONFIRMATION);

            confirm.setTitle("Подтверждение удаления");
            confirm.setHeaderText("Удаление заказа");
            confirm.setContentText(
                    "Вы действительно хотите удалить заказ №"
                            + orderD.getOrder_id() + "?"
            );

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent()
                    && result.get() == ButtonType.OK) {

                ordersDaoImpl.delete(orderD);
                orders.remove(orderD);
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Удаление");
                success.setHeaderText(null);
                success.setContentText(
                        "Заказ успешно удален."
                );
                success.showAndWait();
            }

        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Удаление заказа");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Выберите заказ."
            );

            alert.showAndWait();
        }
    }


    @FXML
    void clikExit() {
        Platform.exit();
    }

    @FXML
    void clikFindAll(ActionEvent event) {
        orders.clear();;
        orders.addAll(ordersDaoImpl.getOrders());
    }


    @FXML
    void clikMenuBox() {
            String selected = idMenuBox.getValue();
            switch (selected) {
                case "Клиенты": openWindow("/ru/nosova/flowershop/client-view.fxml");break;

                case "Букет": openWindow("/ru/nosova/flowershop/bouquet-view.fxml");break;

                case "Склад": openWindow("/ru/nosova/flowershop/flowers-view.fxml");break;
            }

    }

    @FXML
    void clikNewOrder(ActionEvent event) {
        ResourceBundle bundle = LocaleSettings.getCurrentBundle();
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/ru/nosova/flowershop/order-view.fxml"), bundle);
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), 1000, 600);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(MainApplication.getStage());

        stage.setScene(scene);
        OrderController controller = loader.getController();
        controller.setStage(stage);

        stage.showAndWait();
    }

    public void onSetting(ActionEvent event) {
        String selected = boxSetting.getValue();

        if(selected == null)
            return;

        switch (selected) {

            case "Русский" ->
                    LocaleSettings.setLocale(LocaleSettings.RUSSIAN);

            case "Deutsch" ->
                    LocaleSettings.setLocale(LocaleSettings.GERMAN);

            case "English" ->
                    LocaleSettings.setLocale(LocaleSettings.ENGLISH);
        }

        MainApplication.showMainWindow();
    }
}

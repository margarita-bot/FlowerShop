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
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.nosova.flowershop.MainApplication;
import ru.nosova.flowershop.dao.BouquetDao;
import ru.nosova.flowershop.dao.impl.BouquetDaoImpl;
import ru.nosova.flowershop.model.Bouquet;
import ru.nosova.flowershop.utils.LocaleSettings;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class BouquetController {

    @FXML private TextField findPriceMax;
    @FXML private Label idErrorFind;
    @FXML private TableView<Bouquet> TableBouguet;
    @FXML private ComboBox<String> boxMenu;
    @FXML private Button buttonAdd;
    @FXML private Button buttonBack;
    @FXML private Button buttonDelete;
    @FXML private Button buttonEdit;
    @FXML private Button buttonExit;
    @FXML private Button buttonFind;
    @FXML private Button buttonReset;
    @FXML private TableColumn<Bouquet, String> columnName;
    @FXML private TableColumn<Bouquet, Double> columnPrice;
    @FXML private TableColumn<Bouquet, String> columnText;
    @FXML private TextField findName;
    @FXML private TextField findPrice;
    @FXML private TextArea idBouquetText;
    @FXML private Label idPriceBouquet;
    @FXML private Label idNameBouquet;

    private final ObservableList<Bouquet> bouquets = FXCollections.observableArrayList();
    private BouquetDaoImpl<Bouquet, Long> bouquetDao;


    @FXML
    void initialize(){
        bouquetDao = new BouquetDao();
        bouquets.addAll(bouquetDao.select());
        columnName.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getName()));
        columnPrice.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getPrice()));
        columnText.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getDescription()));
        TableBouguet.setItems(bouquets);
        boxMenu.getItems().addAll("Клиенты", "Склад");

        TableBouguet.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Bouquet>() {
            @Override
            public void changed(ObservableValue<? extends Bouquet> observableValue, Bouquet oldBouquet, Bouquet current) {
                if (current != null){
                    idNameBouquet.setText(current.getName());
                    idPriceBouquet.setText(String.valueOf(current.getPrice()));
                    idBouquetText.setText(current.getDescription());

                }
            }
        });
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

        stage.setFullScreen(true);
        stage.setScene(scene);

        stage.show();
    }

    private void openFormBouquet(Bouquet bouquet){
        ResourceBundle bundle = LocaleSettings.getCurrentBundle();
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/ru/nosova/flowershop/bouquetCreate-view.fxml"), bundle);
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), 600, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(MainApplication.getStage());

        stage.setScene(scene);
        BouquetCreateController controller = loader.getController();
        controller.setStage(stage);
        controller.setBouquet(bouquet);

        stage.showAndWait();
    }

    @FXML
    void onAdd(ActionEvent event) {
        openFormBouquet(null);
    }

    @FXML
    void onDelete(ActionEvent event) {
        Bouquet bouquet = TableBouguet.getSelectionModel().getSelectedItem();
        if(bouquet != null){
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Подтверждение удаления");
            confirm.setHeaderText("Удаление букета");
            confirm.setContentText(
                    "Вы действительно хотите удалить клиента "
                            + bouquet.getName() + " ?"
            );

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                bouquetDao.delete(bouquet);
                bouquets.remove(bouquet);

                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Удаление");
                success.setHeaderText(null);
                success.setContentText("Букет успешно удален.");
                success.showAndWait();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Удаление букета");
            alert.setHeaderText(null);
            alert.setContentText("Выберете букет.");
            alert.showAndWait();
        }
    }

    @FXML
    void onEdit(ActionEvent event) {
        Bouquet selected = TableBouguet.getSelectionModel().getSelectedItem();
        if (selected == null){
            return;
        }
        openFormBouquet(selected);
    }

    @FXML
    void onExit(ActionEvent event) {Platform.exit();}

    @FXML
    void onFind(ActionEvent event) {
        String name = findName.getText().trim();
        double minPrice = Double.parseDouble(findPrice.getText().isEmpty() ? "0" : findPrice.getText());
        double maxPrice = Double.parseDouble(findPriceMax.getText().isEmpty() ? "0" : findPriceMax.getText());
        if (name.isBlank() && maxPrice == 0 && minPrice == 0){
            idErrorFind.setText("Введите хотя бы один параметр поиска");
            return;
        }
        bouquets.clear();
        List<Bouquet> find = bouquetDao.find(name, minPrice, maxPrice);
        bouquets.addAll(find);
        findName.clear();
        findPrice.clear();
        findPriceMax.clear();
    }

    @FXML
    void onMenu(ActionEvent event) {
        String selected = boxMenu.getValue();
        switch (selected) {
            case "Клиенты": openWindow("/ru/nosova/flowershop/client-view.fxml");break;

            case "Склад": openWindow("/ru/nosova/flowershop/flowers-view.fxml");break;
        }
    }

    @FXML
    void onReset(ActionEvent event) {
        findName.clear();
        findPrice.clear();

        bouquets.clear();
        bouquets.addAll(bouquetDao.select());
    }

    public void onBack(ActionEvent event) {
        openWindow("/ru/nosova/flowershop/FlowerShop-view.fxml");
    }
}


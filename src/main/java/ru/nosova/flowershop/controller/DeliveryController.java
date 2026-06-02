package ru.nosova.flowershop.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;
import ru.nosova.flowershop.dao.DeliveryDao;
import ru.nosova.flowershop.dao.FlowersDao;
import ru.nosova.flowershop.dao.impl.DeliveryDaoImpl;
import ru.nosova.flowershop.dao.impl.FlowersDaoImpl;
import ru.nosova.flowershop.model.Delivery;
import ru.nosova.flowershop.model.Flowers;
import ru.nosova.flowershop.utils.Validation;

import java.time.LocalDate;

public class DeliveryController {

    @FXML private Label idError;
    @FXML private TextField idCount;
    @FXML private TableView<Flowers> DeliveryTable;
    @FXML private TableColumn<Flowers, Integer> columnCount;
    @FXML private TableColumn<Flowers, String> columnName;
    @FXML private Label labelNameFlowers;
    @Setter
    private Stage stage;
    private final ObservableList<Flowers> flowers = FXCollections.observableArrayList();
    private FlowersDaoImpl<Flowers, Long> flowersDao;
    private DeliveryDaoImpl deliveryDao;

    @FXML
    void initialize(){
        flowersDao = new FlowersDao();
        deliveryDao = new DeliveryDao();
        flowers.addAll(flowersDao.find());
        columnName.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getName()));
        columnCount.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getQuantity()));
        DeliveryTable.setItems(flowers);
        DeliveryTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Flowers>() {
            @Override
            public void changed(ObservableValue<? extends Flowers> observableValue, Flowers oldFlowers, Flowers current) {
                if(current != null){
                    labelNameFlowers.setText(current.getName());
                }
            }
        });
    }

    @FXML
    void onClose(ActionEvent event) {
        stage.close();
    }

    @FXML
    void onOk(ActionEvent event) {
        Flowers flower = DeliveryTable.getSelectionModel().getSelectedItem();
        if(flower != null){
            int quantity = Integer.parseInt(idCount.getText());
            if(!Validation.isValidQuantity(quantity) || quantity <= 0){
                idError.setText("Проверьте количество");
                return;
            }
            Delivery delivery = Delivery.builder()
                    .flowers(flower)
                    .quantity(quantity)
                    .date(LocalDate.now())
                    .build();
            idCount.clear();
            deliveryDao.insert(delivery);
            flowersDao.updateQuantity(flower.getFlower_id(), quantity);
            flower.setQuantity(flower.getQuantity() + quantity);
            DeliveryTable.refresh();
        }else {
            idError.setText("Выберете цветок");
        }
    }

}

package ru.nosova.flowershop.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import ru.nosova.flowershop.dao.BouquetCompositionDao;
import ru.nosova.flowershop.dao.FlowersDao;
import ru.nosova.flowershop.dao.impl.BouquetCompositionDaoImpl;
import ru.nosova.flowershop.dao.impl.FlowersDaoImpl;
import ru.nosova.flowershop.model.Bouquet;
import ru.nosova.flowershop.model.BouquetComposition;
import ru.nosova.flowershop.model.Flowers;

import java.util.List;
import java.util.Optional;

public class BouquetCompositionController {

    @FXML private Label idError;
    @FXML private Label LabelPrice;
    @FXML private Spinner<Integer> editCountFlowers;
    @FXML private Label LabelNameFlowers;
    @FXML private TableView<BouquetComposition> TableBouquetComposition;
    @FXML private TableView<Flowers> TableFlowers;
    @FXML private Button buttonAddFlowers;
    @FXML private Button buttonClose;
    @FXML private Button buttonSave;
    @FXML private TableColumn<Flowers, Integer> columnCount;
    @FXML private TableColumn<BouquetComposition, Integer> columnCountFlowers;
    @FXML private TableColumn<BouquetComposition, String> columnFlowers;
    @FXML private TableColumn<Flowers, String> columnName;
    @FXML private TableColumn<Flowers, Double> columnPrice;
    @FXML private TableColumn<BouquetComposition, Double> columnPriceAll;
    @FXML private Spinner<Integer> countFlowers;
    @Setter
    private Stage stage;
    @Getter
    private final ObservableList<BouquetComposition> compositions = FXCollections.observableArrayList();
    private final ObservableList<Flowers> flowers = FXCollections.observableArrayList();
    private FlowersDaoImpl<Flowers, Long> flowersDao;

    private BouquetCompositionDaoImpl bouquetCompositionDao;
    @Getter
    private boolean save = false;
    private Bouquet bouquet;
    public void setBouquet(Bouquet bouquet) {
        this.bouquet = bouquet;
        bouquetCompositionDao = new BouquetCompositionDao();
        compositions.clear();
        if(bouquet != null){
            List<BouquetComposition> list = bouquetCompositionDao.select(bouquet.getBouquet_id());
            compositions.addAll(list);
        }

    }

    @FXML
    public void initialize(){
        flowersDao = new FlowersDao();
        flowers.addAll(flowersDao.find());

        columnName.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getName()));
        columnCount.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getQuantity()));
        columnPrice.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getPrice()));
        TableFlowers.setItems(flowers);
        TableFlowers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Flowers>() {
            @Override
            public void changed(ObservableValue<? extends Flowers> observableValue, Flowers oldFlowers, Flowers current) {
                if (current != null){
                    LabelNameFlowers.setText(current.getName());
                }
            }
        });

        columnFlowers.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getFlowers().getName()));
        columnCountFlowers.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getQuantity()).asObject());
        columnPriceAll.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getPrice()));
        TableBouquetComposition.setItems(compositions);

        TableBouquetComposition.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BouquetComposition>() {
            @Override
            public void changed(ObservableValue<? extends BouquetComposition> observableValue, BouquetComposition oldBouquetComposition, BouquetComposition current) {
                if (current != null) {
                    editCountFlowers.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, current.getQuantity()));
                }
            }
        });

        countFlowers.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 1)
        );

    }

    @FXML
    void onAddFlowers(ActionEvent event) {
        Flowers selectedFlower = TableFlowers.getSelectionModel().getSelectedItem();
        if(selectedFlower== null){
            return;
        }
        int quantity = countFlowers.getValue();
        if(quantity <= 0){
            idError.setText("Число не может быть отрицательным.");
            return;
        }
        for (BouquetComposition composition : compositions) {
            if (composition.getFlowers().getFlower_id() == selectedFlower.getFlower_id()) {
                int newQuantity = composition.getQuantity() + quantity;
                composition.setQuantity(newQuantity);
                composition.setPrice(selectedFlower.getPrice() * newQuantity);
                TableBouquetComposition.refresh();
                updateTotalPrice();
                return;
            }
        }

        BouquetComposition composition = BouquetComposition.builder()
                        .flowers(selectedFlower)
                        .quantity(quantity)
                        .price(selectedFlower.getPrice() * quantity)
                        .build();
        compositions.add(composition);
        updateTotalPrice();
        countFlowers.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 1));
    }

    @FXML
    void onClose(ActionEvent event) {
        save = false;
        stage.close();
    }

    @FXML
    void onSave(ActionEvent event) {
        save = true;
        stage.close();
    }
    private void updateTotalPrice() {
        double total = compositions.stream().mapToDouble(BouquetComposition::getPrice).sum();
        LabelPrice.setText(
                String.format("%.2f руб.", total)
        );
    }

    public void onEditCount(ActionEvent event) {
        BouquetComposition currentFlower = TableBouquetComposition.getSelectionModel().getSelectedItem();
        if(currentFlower == null){
            idError.setText("Выберете цветок.");
            return;
        }
        int newQuantity = editCountFlowers.getValue();
        if(newQuantity <= 0){
            idError.setText("Число не может быть отрицательным.");
            return;
        }
        currentFlower.setQuantity(newQuantity);
        currentFlower.setPrice(currentFlower.getFlowers().getPrice()*newQuantity);
        TableBouquetComposition.refresh();
        updateTotalPrice();

    }

    public void onDelete(ActionEvent event) {
        BouquetComposition selected = TableBouquetComposition.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Удаление цветка");
            alert.setHeaderText(null);
            alert.setContentText("Выберете цветок.");
            alert.showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение удаления");
        confirm.setHeaderText("Удаление цветка");
        confirm.setContentText(
                "Вы действительно хотите удалить цветок "
                        + selected.getFlowers().getName()+ " ?"
        );

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            compositions.remove(selected);
            editCountFlowers.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 1));
            updateTotalPrice();

            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Удаление");
            success.setHeaderText(null);
            success.setContentText("Цветок успешно удален.");
            success.showAndWait();
        }
    }
}


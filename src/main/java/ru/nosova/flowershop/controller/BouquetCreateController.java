package ru.nosova.flowershop.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;
import ru.nosova.flowershop.MainApplication;
import ru.nosova.flowershop.dao.BouquetCompositionDao;
import ru.nosova.flowershop.dao.BouquetDao;
import ru.nosova.flowershop.dao.impl.BouquetCompositionDaoImpl;
import ru.nosova.flowershop.dao.impl.BouquetDaoImpl;
import ru.nosova.flowershop.model.Bouquet;
import ru.nosova.flowershop.model.BouquetComposition;
import ru.nosova.flowershop.utils.LocaleSettings;
import ru.nosova.flowershop.utils.Validation;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;


public class BouquetCreateController {

    @FXML private Label idError;
    @FXML private TextField addNameBouquet;
    @FXML private Label addPrice;
    @FXML private TextArea addText;
    @FXML private Button buttonBouquetComposition;
    @FXML private Button buttonCancel;
    @FXML private Button buttonCreate;
    @Setter
    private Stage stage;
    private double totalPrice;
    private final ObservableList<BouquetComposition> compositions = FXCollections.observableArrayList();
    private BouquetDaoImpl<Bouquet, Long> bouquetDao;
    private BouquetCompositionDaoImpl bouquetCompositionDao;
    private Bouquet bouquet;

    public void setBouquet(Bouquet bouquet) {
        this.bouquet = bouquet;
        if (bouquet != null) {
            addNameBouquet.setText(bouquet.getName());
            addPrice.setText(String.valueOf(bouquet.getPrice()));
            addText.setText(bouquet.getDescription());
        }
    }

    @FXML
    void onBouquetComposition(ActionEvent event) {
        ResourceBundle bundle = LocaleSettings.getCurrentBundle();
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/ru/nosova/flowershop/BouquetComposition-view.fxml"), bundle);
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), 900, 500);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(MainApplication.getStage());

        //stage.setFullScreen(true);
        stage.setScene(scene);
        BouquetCompositionController controller = loader.getController();
        controller.setStage(stage);
        controller.setBouquet(bouquet);

        stage.showAndWait();

        if(controller.isSave()){
            compositions.clear();
            compositions.addAll(controller.getCompositions());
            updatePrice();
        }
    }
    private void updatePrice() {
        totalPrice = compositions.stream().mapToDouble(BouquetComposition::getPrice).sum();
        addPrice.setText(String.format("%.2f руб.", totalPrice));
    }

    @FXML
    void onCancel(ActionEvent event) {
        stage.close();
    }

    @FXML
    void onCreate(ActionEvent event) {
        bouquetDao = new BouquetDao();
        bouquetCompositionDao = new BouquetCompositionDao();
        if(bouquet == null){
            String name = addNameBouquet.getText().trim();
            String description = addText.getText().trim();
            if(name.isBlank() || !Validation.isValidName(name)){
                idError.setText("Проверьте корректность названия.");
                return;
            }

            Bouquet bouquet = Bouquet.builder()
                    .name(name)
                    .price(totalPrice)
                    .description(description)
                    .build();
            Bouquet save = bouquetDao.insert(bouquet);

            for(BouquetComposition composition : compositions){
                composition.setBouquet(save);
                System.out.println(composition.getBouquet().getBouquet_id());
                bouquetCompositionDao.insert(composition);
            }
        }else {
            String name = addNameBouquet.getText().trim();
            if(name.isBlank() || !Validation.isValidName(name)){
                idError.setText("Проверьте корректность названия.");
                return;
            }
            bouquet.setName(addNameBouquet.getText());
            bouquet.setDescription(addText.getText());
            bouquet.setPrice(totalPrice);
            bouquetDao.update(bouquet);

            bouquetCompositionDao.delete(bouquet.getBouquet_id());
            for (BouquetComposition composition : compositions) {
                composition.setBouquet(bouquet);
                bouquetCompositionDao.insert(composition);
            }
        }

        stage.close();
    }

}

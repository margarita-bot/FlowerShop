package ru.nosova.flowershop.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import ru.nosova.flowershop.MainApplication;
import ru.nosova.flowershop.dao.FlowersDao;
import ru.nosova.flowershop.model.Flowers;
import ru.nosova.flowershop.utils.LocaleSettings;
import ru.nosova.flowershop.utils.Validation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class FlowersController {

    @FXML private  Label idErrorAdd;
    @FXML private  Label idErrorEdit;
    @FXML private  Label idErrorFind;
    @FXML private ImageView addImg;
    @FXML private ComboBox<String> boxMenu;
    @FXML private TextField findFlowefsName;
    @FXML private TextField findPriceMax;
    @FXML private TextField findPriceMin;
    @FXML private FlowPane idFlowPane;
    @FXML private TextField editFnlowefsName;
    @FXML private ImageView editImg;
    @FXML private TextField addCount;
    @FXML private TextField addFnlowefsName;
    @FXML private TextField addPrice;

    private FlowersDao flowersDao;
    private FlowersPaneController selectedController;
    private Flowers selectedFlower;
    private String selectedAddImage;
    private String selectedEditImage;
    List<Flowers> flowers;

    @FXML
    void initialize(){
        flowersDao = new FlowersDao();
        showFlowers();
        boxMenu.getItems().addAll("Клиенты", "Букет");
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

    private void loadImageToEdit(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            editImg.setImage(null);
            return;
        }
        String path = "/ru/nosova/flowershop/images/" + imageName;
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream != null) {
            editImg.setImage(new Image(stream));
        } else {
            System.out.println("Изображение не найдено: " + imageName); editImg.setImage(null);
        }
    }

    private void selectFlower(Flowers flower) {
        this.selectedFlower = flower;
        editFnlowefsName.setText(flower.getName());
        loadImageToEdit(flower.getImgPath());
    }

    private void showFlowers() {
        idFlowPane.getChildren().clear();
        flowers = flowersDao.find();
        for (Flowers flower : flowers) {
            try {
                ResourceBundle bundle = LocaleSettings.getCurrentBundle();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                                        "/ru/nosova/flowershop/flowersBlock-view.fxml"), bundle);

                Parent pane = loader.load();
                FlowersPaneController controller = loader.getController();
                controller.setData(flower);

                controller.setOnSelect(flowers1 -> {
                    if(selectedController != null){
                        selectedController.setSelected(false);
                    }
                    selectedController = controller;
                    controller.setSelected(true);
                    selectFlower(flowers1);

                });
                idFlowPane.getChildren().add(pane);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void onAdd(ActionEvent event) {
        String name = addFnlowefsName.getText().trim();
        double price = Double.parseDouble(addPrice.getText().trim());
        int quantity = Integer.parseInt(addCount.getText().trim());
        if(name.isBlank() || price <= 0 || quantity <= 0){
            idErrorAdd.setText("Проверьте заполненность полей.");
            return;
        }
        if (!Validation.isValidPrice(price)){
            idErrorAdd.setText("Цена не может быть отрицательной или 0.");
            return;
        }
        if (!Validation.isValidName(name)){
            idErrorAdd.setText("Проверьте корректность названия.");
            return;
        }
        if (!Validation.isValidQuantity(quantity)){
            idErrorAdd.setText("Количество не может быть отрицательным или 0.");
            return;
        }
        Flowers flowers = Flowers.builder()
                .name(name)
                .price(price)
                .quantity(quantity)
                .imgPath(selectedAddImage)
                .build();
        flowersDao.insert(flowers);
        showFlowers();
        addFnlowefsName.clear();
        addPrice.clear();
        addCount.clear();
        addImg.setImage(null);
        selectedAddImage = null;
    }

    @FXML
    void onBack(ActionEvent event) {
        openWindow("/ru/nosova/flowershop/FlowerShop-view.fxml");
    }


    @FXML
    void onDelete(ActionEvent event) {
        if(selectedFlower != null){
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Подтверждение удаления");
            confirm.setHeaderText("Удаление цветка");
            confirm.setContentText(
                    "Вы действительно хотите удалить цветок "
                            + selectedFlower.getName() + " ?"
            );

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                flowersDao.delete(selectedFlower);
                showFlowers();
                selectedFlower = null;
                selectedController = null;

                editFnlowefsName.clear();
                editImg.setImage(null);

                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Удаление");
                success.setHeaderText(null);
                success.setContentText("Цветок успешно удален.");
                success.showAndWait();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Удаление цветка");
            alert.setHeaderText(null);
            alert.setContentText("Выберете цветок.");
            alert.showAndWait();
        }
    }

    @FXML
    void onEdit() {
        if(selectedFlower != null){
            selectedFlower.setName(editFnlowefsName.getText());
            selectedFlower.setImgPath(selectedEditImage);
            if(editFnlowefsName.getText().isBlank() || !Validation.isValidName(editFnlowefsName.getText())){
                idErrorEdit.setText("Проверьте корректность названия.");
                return;
            }
            if (selectedEditImage.isBlank()){
                idErrorEdit.setText("Выберете фото.");
                return;
            }

           flowersDao.update(selectedFlower);
           showFlowers();

           editFnlowefsName.clear();
           editImg.setImage(null);
           selectedFlower = null;
           selectedEditImage = null;
        }else {
            idErrorEdit.setText("Выберете цветок.");
        }
    }

    @FXML
    void onExit(ActionEvent event) {Platform.exit();}

    @FXML
    void onFind(ActionEvent event) {
        String name = findFlowefsName.getText().trim();
        double minPrice = Double.parseDouble(findPriceMin.getText().isEmpty() ? "0" : findPriceMin.getText());
        double maxPrice = Double.parseDouble(findPriceMax.getText().isEmpty() ? "0" : findPriceMax.getText());
        if (name.isBlank() && maxPrice == 0 && minPrice == 0){
            idErrorFind.setText("Введите хотя бы один параметр поиска");
            return;
        }
        flowers.clear();
        idFlowPane.getChildren().clear();
        List<Flowers> find = flowersDao.findByCriteria(name, minPrice, maxPrice);
        flowers.addAll(find);

        for (Flowers flower : find) {
            try {
                ResourceBundle bundle = LocaleSettings.getCurrentBundle();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/ru/nosova/flowershop/flowersBlock-view.fxml"), bundle);

                Parent pane = loader.load();
                FlowersPaneController controller = loader.getController();
                controller.setData(flower);

                controller.setOnSelect(flowers1 -> {
                    if(selectedController != null){
                        selectedController.setSelected(false);
                    }
                    selectedController = controller;
                    controller.setSelected(true);
                    selectFlower(flowers1);

                });
                idFlowPane.getChildren().add(pane);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        findFlowefsName.clear();
        findPriceMin.clear();
        findPriceMax.clear();
    }

    @FXML
    void onMenu(ActionEvent event) {
        String selected = boxMenu.getValue();
        switch (selected) {
            case "Клиенты": openWindow("/ru/nosova/flowershop/client-view.fxml");break;

            case "Букет": openWindow("/ru/nosova/flowershop/bouquet-view.fxml");break;

        }
    }

    private void chooseImage(boolean isEdit){
        FileChooser chooser = new FileChooser();

        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Изображения",
                "*.jpg",
                "*.png",
                "*.jpeg"));
        try {
            File dir = new File(getClass().getResource("/ru/nosova/flowershop/images").toURI());
            chooser.setInitialDirectory(dir);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        File file = chooser.showOpenDialog(MainApplication.getStage());
        if(file != null){
            String fileName = file.getName();
            Image image = new Image(file.toURI().toString());

            if(isEdit){
                selectedEditImage = fileName;
                editImg.setImage(image);
            }else{
                selectedAddImage = fileName;
                addImg.setImage(image);
            }
        }
    }

    public void onChooseEditPhoto() {
        chooseImage(true);
    }

    public void onChooseAddPhoto() {
        chooseImage(false);
    }

    public void onDelivery(ActionEvent event) {
        ResourceBundle bundle = LocaleSettings.getCurrentBundle();
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/ru/nosova/flowershop/delivery-view.fxml"), bundle);
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), 600, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(MainApplication.getStage());

        stage.setFullScreen(true);
        stage.setScene(scene);
        DeliveryController controller = loader.getController();
        controller.setStage(stage);

        stage.showAndWait();
    }


    public void onReset(ActionEvent event) {
        showFlowers();

    }
}
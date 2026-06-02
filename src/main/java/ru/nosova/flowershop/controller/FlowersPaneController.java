package ru.nosova.flowershop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;
import ru.nosova.flowershop.model.Flowers;

import java.io.InputStream;
import java.util.function.Consumer;

public class FlowersPaneController {

    @FXML private ImageView idImg;
    @FXML private Label labelCount;
    @FXML private Label labelName;
    @FXML private Label labelPrice;
    @FXML private Pane rootPane;

    @Getter
    private Flowers current;
    private Consumer<Flowers> onSelect;

    public void setData(Flowers flowers){
        current = flowers;
        labelName.setText(flowers.getName());
        labelPrice.setText(String.valueOf(flowers.getPrice()));
        labelCount.setText(String.valueOf(flowers.getQuantity()));

        String path = "/ru/nosova/flowershop/images/" + flowers.getImgPath();

        InputStream stream = getClass().getResourceAsStream(path);
        if(stream != null){
            Image img = new Image(stream);
            idImg.setImage(img);
        }else {
            System.out.println("Изображение не найдено: " + flowers.getImgPath());
        }

    }

    public void setSelected(boolean selected){
        if(selected){
            rootPane.setStyle("""
            -fx-border-color:#4a90e2;
            -fx-border-width:3;
            -fx-background-color:#dceeff;
        """);
        }else{
            rootPane.setStyle("""
            -fx-border-width:0;
        """);
        }
    }
    public void setOnSelect(Consumer<Flowers> onSelect){
        this.onSelect = onSelect;
    }

    public void handleClick(MouseEvent mouseEvent) {
        if(onSelect != null){
            onSelect.accept(current);
        }
    }
}


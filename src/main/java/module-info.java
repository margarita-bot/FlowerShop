module ru.nosova.flowershop {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.sql;
    requires java.desktop;
    requires org.slf4j;


    opens ru.nosova.flowershop to javafx.fxml;
    exports ru.nosova.flowershop;
    exports ru.nosova.flowershop.controller;
    opens ru.nosova.flowershop.controller to javafx.fxml;
}
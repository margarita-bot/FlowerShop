package ru.nosova.flowershop.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;
import ru.nosova.flowershop.dao.*;
import ru.nosova.flowershop.dao.impl.*;
import ru.nosova.flowershop.model.*;

import java.sql.Time;
import java.util.List;

public class OrderController {

    @FXML private Label idErrorsFindBouquet;
    @FXML private Label idErrorFindClient;
    @FXML private Label idErrorsFindAddress;
    @FXML private TextField addAddressCity;
    @FXML private TextField addAddressStret;
    @FXML private TextField addAddressHome;
    @FXML private TextField addAddressEntrance;
    @FXML private TextField addDeliveryTime;
    @FXML private Label DelivereTime;
    @FXML private TextField BouquetPriceMaxFind;
    @FXML private ComboBox boolDeliver;
    @FXML private TableView<Address> AddressTaible;
    @FXML private TableColumn<Address, String> AddresCetiColumn;
    @FXML private TableColumn<Address, String> AddresStretColumn;
    @FXML private TableColumn<Address, String> AddressHomeColumn;
    @FXML private TableColumn<Address, String> AddressNumberColumn;

    @FXML private TextField AddressHomeFind;
    @FXML private TextField AddressStretFind;

    @FXML private TableView<Bouquet> BouquetTable;
    @FXML private TableColumn<Bouquet, String> BouquetNameColumn;
    @FXML private TableColumn<Bouquet, Double> BouquetPriceColumn;
    @FXML private TableColumn<Bouquet, String> BouquetTextColumn;

    @FXML private TextField bouquetNameFind;
    @FXML private TextField BouquetPriceFind;
    @FXML private TextField BouquetOrder;

    @FXML private TableView<Client> ClientTable;
    @FXML private TableColumn<Client, String> ClientLastNameColumn;
    @FXML private TableColumn<Client, String> ClientMiddleNameColumn;
    @FXML private TableColumn<Client, String> ClientNameColumn;
    @FXML private TableColumn<Client, String> ClientEmailColumn;
    @FXML private TableColumn<Client, String> ClientFhoneColumn;

    @FXML private TextField ClientFindFhone;
    @FXML private TextField ClientFindLastName;
    @FXML private TextField ClientFindName;

    @FXML private TableView<Florist> FloristTable;
    @FXML private TableColumn<Florist, String > FloristLastNameColumn;
    @FXML private TableColumn<Florist, String> FloristMidlleNameColumn;
    @FXML private TableColumn<Florist, String> FloristNameColumn;


    @FXML private TextField ClientOrder;
    @FXML private TextField DelivereOrder;
    @FXML private TextField FloristOrder;
    @FXML private TextField DateOrder;
    @FXML private Label PriceOrder;

    @Setter
    private Stage stage;

    private final ObservableList<Client> clients = FXCollections.observableArrayList();
    private ClientDaoImpl<Client, Long> clientDao;

    private final ObservableList<Florist> florists = FXCollections.observableArrayList();
    private FloristDaoImpl floristDao;

    private final ObservableList<Bouquet> bouquets = FXCollections.observableArrayList();
    private BouquetDaoImpl<Bouquet, Long> bouquetDao;

    private final ObservableList<Address> addresses = FXCollections.observableArrayList();
    private AddressDaoImpl addressDao;

    private Client saveClient;
    private Florist saveFlorist;
    private Bouquet saveBouquet;
    private Address saveAddress;

    private OrdersDaoImpl<Orders, Long> ordersDao;

    @FXML
    void initialize(){
        ordersDao = new OrdersDao();

        clientDao = new ClientDao();
        clients.addAll(clientDao.getClient());
        ClientNameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getFirstName()));
        ClientLastNameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getLastName()));
        ClientMiddleNameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getMiddleName()));
        ClientFhoneColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getPhone()));
        ClientEmailColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getEmail()));
        ClientTable.setItems(clients);

        floristDao = new FloristDao();
        florists.addAll(floristDao.find());
        FloristNameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getFirstname()));
        FloristLastNameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getLastName()));
        FloristMidlleNameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getMiddleName()));
        FloristTable.setItems(florists);

        bouquetDao = new BouquetDao();
        bouquets.addAll(bouquetDao.select());
        BouquetNameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getName()));
        BouquetPriceColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getPrice()));
        BouquetTextColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getDescription()));
        BouquetTable.setItems(bouquets);

        addressDao = new AddressDao();
        addresses.addAll(addressDao.find());
        AddresCetiColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getCity()));
        AddresStretColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getStreet()));
        AddressHomeColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getHouse()));
        AddressNumberColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getEntrance()));
        AddressTaible.setItems(addresses);

        boolDeliver.getItems().addAll("Да", "Нет");
    }

    @FXML
    void onAddressFind(ActionEvent event) {

    }

    @FXML
    void onAddressSave(ActionEvent event) {
        saveAddress = AddressTaible.getSelectionModel().getSelectedItem();
        if(saveAddress == null){
            return;
        }
        DelivereOrder.setText(saveAddress.getCity() + " " +
                saveAddress.getStreet() + " " +
                saveAddress.getHouse() + " " +
                saveAddress.getEntrance());
    }

    @FXML
    void onBouquetFind(ActionEvent event) {
        String name = bouquetNameFind.getText().trim();
        double minPrice = Double.parseDouble(BouquetPriceFind.getText().isEmpty() ? "0" : BouquetPriceFind.getText());
        double maxPrice = Double.parseDouble(BouquetPriceMaxFind.getText().isEmpty() ? "0" : BouquetPriceMaxFind.getText());
        if (name.isBlank() && maxPrice == 0 && minPrice == 0){
            idErrorsFindBouquet.setText("Введите хотя бы один параметр поиска");
            return;
        }
        bouquets.clear();
        List<Bouquet> find = bouquetDao.find(name, minPrice, maxPrice);
        bouquets.addAll(find);
        bouquetNameFind.clear();
        BouquetPriceFind.clear();
        BouquetPriceMaxFind.clear();
    }

    @FXML
    void onBouquetSave(ActionEvent event) {
        saveBouquet = BouquetTable.getSelectionModel().getSelectedItem();
        if(saveBouquet == null){
            return;
        }
        BouquetOrder.setText(saveBouquet.getName());
        PriceOrder.setText(String.valueOf(saveBouquet.getPrice()));
    }

    @FXML
    void onFindClient(ActionEvent event) {
        String name = ClientFindName.getText().trim();
        String lastname = ClientFindLastName.getText().trim();
        String phone = ClientFindFhone.getText().trim();

        if (name.isBlank() && lastname.isBlank() && phone.isBlank()) {
            idErrorFindClient.setText("Введите хотя бы один параметр поиска.");
            return;
        }

        if(name.isBlank()){name=null;}
        if (lastname.isBlank()){lastname=null;}
        if(phone.isBlank()){phone=null;}

        List<Client> find = clientDao.findClient(name, lastname, phone);

        clients.clear();
        clients.addAll(find);
        ClientTable.setItems(clients);

        ClientFindName.clear();
        ClientFindLastName.clear();
        ClientFindFhone.clear();
    }

    private void showError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    @FXML
    void onOk(ActionEvent event) {
        if (saveClient == null) {
            showError("Выберите клиента");
            return;
        }

        if (saveFlorist == null) {
            showError("Выберите флориста");
            return;
        }

        if (saveBouquet == null) {
            showError("Выберите букет");
            return;
        }

        boolean delivery =
                "Да".equals(boolDeliver.getValue());

        Long addressId = null;
        Time deliveryTime = null;

        if (delivery) {
            if (saveAddress == null) {
                showError("Выберите адрес доставки");
                return;
            }
            if (addDeliveryTime.getText().isBlank()) {
                showError("Укажите время доставки");
                return;
            }
            addressId = saveAddress.getAddress_id();
            try {
                deliveryTime = Time.valueOf(addDeliveryTime.getText().trim() + ":00");
            } catch (Exception e) {
                showError("Неверный формат времени. Используйте ЧЧ:ММ");
                return;
            }
        }

        long statusId = 1L;

        ordersDao.createOrders(
                saveClient.getClient_id(),
                saveFlorist.getFlorist_id(),
                saveBouquet.getBouquet_id(),
                delivery,
                addressId,
                statusId,
                deliveryTime
        );

        stage.close();
    }

    @FXML
    void onOtm(ActionEvent event) {
        stage.close();
    }

    @FXML
    void onSaveClient(ActionEvent event) {
        saveClient = ClientTable.getSelectionModel().getSelectedItem();
        if(saveClient == null){
            return;
        }
        ClientOrder.setText(saveClient.getFirstName() + " " + saveClient.getLastName()+
                " " + saveClient.getMiddleName() + " "
        + saveClient.getPhone() + " " + saveClient.getEmail());
    }

    @FXML
    void onSaveFlorist(ActionEvent event) {
        saveFlorist = FloristTable.getSelectionModel().getSelectedItem();
        if(saveFlorist == null){
            return;
        }
        FloristOrder.setText(saveFlorist.getFirstname() + " " +
                saveFlorist.getLastName() + " " + saveFlorist.getMiddleName());
    }

    public void onBoolDeliveri(ActionEvent event) {
        String deliveri = (String) boolDeliver.getValue();
        if("Нет".equals(deliveri)){
            AddressTaible.setDisable(true);
            AddressTaible.getSelectionModel().clearSelection();
            DelivereOrder.clear();
            saveAddress = null;
            DelivereTime.setText(null);
        }else {
            AddressTaible.setDisable(false);
        }
    }

    public void onAddressAdd(ActionEvent event) {
        String city = addAddressCity.getText().trim();
        String street = addAddressStret.getText().trim();
        String home = addAddressHome.getText().trim();
        String entrance = addAddressEntrance.getText().trim();
        if(entrance.isBlank()){
            entrance = null;
        }
        Address addressIn = Address.builder()
                .city(city)
                .house(home)
                .street(street)
                .entrance(entrance)
                .build();
        Address save = addressDao.insert(addressIn);
        addresses.add(save);
        AddressTaible.setItems(addresses);
        AddressTaible.getSelectionModel().select(save);

        addAddressCity.clear();
        addAddressStret.clear();
        addAddressHome.clear();
        addAddressEntrance.clear();
    }

    public void onDeliveryTime(ActionEvent event) {
        DelivereTime.setText(addDeliveryTime.getText());
    }
}


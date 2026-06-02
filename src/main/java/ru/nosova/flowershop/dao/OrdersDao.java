package ru.nosova.flowershop.dao;

import ru.nosova.flowershop.dao.impl.OrdersDaoImpl;
import ru.nosova.flowershop.model.*;
import ru.nosova.flowershop.utils.DButils;
import ru.nosova.flowershop.utils.SQLStatements;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/*
CREATE TABLE flowershop.orders (
 order_id SERIAL PRIMARY KEY,
 bouquet_id INTEGER NOT NULL,
 client_id INTEGER NOT NULL,
 florist_id INTEGER NOT NULL,
 address_id INTEGER NOT NULL,
 price NUMERIC(10,2) NOT NULL,
 date DATE NOT NULL,
 status VARCHAR(50) NOT NULL
);

 */
public class OrdersDao implements OrdersDaoImpl<Orders, Long> {
    SQLStatements sql = new SQLStatements();
    @Override
    public List<Orders> getOrders() {
        String query = sql.getQuery("order.get_all");
        List<Orders> list = new ArrayList<>();
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)){
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Client client = Client.builder()
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .middleName(rs.getString("middle_name"))
                        .build();
                Status status = Status.builder()
                        .text(rs.getString("status_text"))
                        .build();
                Orders orders = Orders.builder()
                        .client(client)
                        .status(status)
                        .price(rs.getDouble("price"))
                        .date(rs.getDate("order_date").toLocalDate())
                        .build();
                list.add(orders);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Orders createOrders(Long clientID, Long floristId, Long bouquetId, Boolean delivery, Long addressId, Long statusId, Time deliveryTime) {
        String query = sql.getQuery("order.create");
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)){
            statement.setInt(1, clientID.intValue());
            statement.setInt(2, floristId.intValue());
            statement.setInt(3, bouquetId.intValue());
            statement.setInt(4, statusId.intValue());
            statement.setBoolean(5, delivery);

            if(addressId == null){
                statement.setNull(6, Types.INTEGER);
            }else {
                statement.setInt(6, addressId.intValue());
            }
            if (deliveryTime == null) {
                statement.setNull(7, Types.TIME);
            } else {
                statement.setTime(7, deliveryTime);
            }
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Orders> find(String name, String lastname, String status, LocalDate date) {
        String query = sql.getQuery("order.find");
        List<Orders> orders = new ArrayList<>();

        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)) {
            statement.setString(1, name);

            statement.setString(2, lastname);
            statement.setString(3, status);
            if (date != null) {
                statement.setDate(4, Date.valueOf(date));
            } else {
                statement.setNull(4, Types.DATE);
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Client client = Client.builder()
                        .client_id(rs.getLong("client_id"))
                        .firstName(rs.getString("client_first_name"))
                        .lastName(rs.getString("client_last_name"))
                        .middleName(rs.getString("client_middle_name"))
                        .email(rs.getString("client_email"))
                        .phone(rs.getString("client_phone"))
                        .build();
                Bouquet bouquet = Bouquet.builder()
                        .bouquet_id(rs.getLong("bouquet_id"))
                        .name(rs.getString("bouquet_name"))
                        .price(rs.getDouble("bouquet_price"))
                        .description(rs.getString("bouquet_description"))
                        .build();
                Florist florist = Florist.builder()
                        .florist_id(rs.getLong("florist_id"))
                        .firstname(rs.getString("florist_first_name"))
                        .lastName(rs.getString("florist_last_name"))
                        .middleName(rs.getString("florist_middle_name"))
                        .phone(rs.getString("florist_phone"))
                        .build();
                Address address = Address.builder()
                        .address_id(rs.getLong("address_id"))
                        .city(rs.getString("city"))
                        .street(rs.getString("street"))
                        .house(rs.getString("house"))
                        .entrance(rs.getString("entrance"))
                        .build();
                Status statusO = Status.builder()
                        .status_id(rs.getLong("status_id"))
                        .text(rs.getString("status_text"))
                        .build();
                Orders order = Orders.builder()
                        .order_id(rs.getLong("order_id"))
                        .client(client)
                        .bouquet(bouquet)
                        .florist(florist)
                        .address(address)
                        .status(statusO)
                        .price(rs.getDouble("order_price"))
                        .date(rs.getDate("order_date").toLocalDate())
                        .delivery(rs.getBoolean("delivery"))
                        .delivereTime(rs.getTime("delivere_time"))
                        .build();
                orders.add(order);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orders;
    }

    @Override
    public List<Orders> find() {

        return List.of();
    }

    @Override
    public Orders insert(Orders client) {
        return null;
    }

    @Override
    public Orders update(Orders client) {
        return null;
    }

    @Override
    public void delete(Orders client) {

    }

    @Override
    public void deleteID(Long aLong) {

    }
}

package ru.nosova.flowershop.dao;

import ru.nosova.flowershop.dao.impl.DeliveryDaoImpl;
import ru.nosova.flowershop.model.Delivery;
import ru.nosova.flowershop.model.Flowers;
import ru.nosova.flowershop.utils.DButils;
import ru.nosova.flowershop.utils.SQLStatements;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
CREATE TABLE flowershop.delivery (
delivery_id SERIAL PRIMARY KEY,
 flower_id INTEGER NOT NULL,
 quantity INTEGER NOT NULL CHECK (quantity > 0),
date DATE NOT NULL
);

 */
public class DeliveryDao implements DeliveryDaoImpl {
    SQLStatements sql = new SQLStatements();
    @Override
    public Delivery insert(Delivery delivery) {
        String query = sql.getQuery("delivery.insert");
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query, new String[]{"delivery_id"})){
            statement.setLong(1, delivery.getFlowers().getFlower_id());
            statement.setInt(2, delivery.getQuantity());
            statement.setDate(3, Date.valueOf(delivery.getDate()));
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                delivery.setDelivery_id(rs.getLong("delivery_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return delivery;
    }

}

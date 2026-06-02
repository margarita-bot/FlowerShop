package ru.nosova.flowershop.dao;

import ru.nosova.flowershop.dao.impl.AddressDaoImpl;
import ru.nosova.flowershop.model.Address;
import ru.nosova.flowershop.utils.DButils;
import ru.nosova.flowershop.utils.SQLStatements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/*
CREATE TABLE flowershop.address (
 address_id SERIAL PRIMARY KEY,
 city VARCHAR(100) NOT NULL,
 street VARCHAR(100) NOT NULL,
 house VARCHAR(20) NOT NULL,
 entrance VARCHAR(10)
);
 */
public class AddressDao implements AddressDaoImpl{
    SQLStatements sql = new SQLStatements();
    @Override
    public List<Address> find() {
        String query = sql.getQuery("address.select");
            List<Address> list = new ArrayList<>();
            try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)) {
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    Address address = Address.builder()
                            .address_id(rs.getLong("address_id"))
                            .city(rs.getString("city"))
                            .street(rs.getString("street"))
                            .house(rs.getString("house"))
                            .entrance(rs.getString("entrance"))
                            .build();
                    list.add(address);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return list;
    }

    @Override
    public Address insert(Address address) {
        String query = sql.getQuery("address.insert");
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)){
            statement.setString(1, address.getCity());
            statement.setString(2, address.getStreet());
            statement.setString(3, address.getHouse());
            statement.setString(4, address.getEntrance());
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                address.setAddress_id(rs.getLong("address_id"));
            }
            return address;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //TODO сделать проверки и сообщение об ошибке. Возможно изменить
    }
}

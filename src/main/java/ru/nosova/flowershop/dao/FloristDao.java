package ru.nosova.flowershop.dao;

import ru.nosova.flowershop.dao.impl.FloristDaoImpl;
import ru.nosova.flowershop.model.Florist;
import ru.nosova.flowershop.utils.DButils;
import ru.nosova.flowershop.utils.SQLStatements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FloristDao implements FloristDaoImpl {
    SQLStatements sql = new SQLStatements();
    @Override
    public List<Florist> find() {
        String query = sql.getQuery("florist.select");
        List<Florist> list = new ArrayList<>();
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Florist florist = Florist.builder()
                        .florist_id(rs.getLong("florist_id"))
                        .firstname(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .middleName(rs.getString("middle_name"))
                        .phone(rs.getString("phone"))
                        .build();
                list.add(florist);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}

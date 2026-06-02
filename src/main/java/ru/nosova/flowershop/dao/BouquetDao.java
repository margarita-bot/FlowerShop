package ru.nosova.flowershop.dao;

import ru.nosova.flowershop.dao.impl.BouquetDaoImpl;
import ru.nosova.flowershop.model.Bouquet;
import ru.nosova.flowershop.utils.DButils;
import ru.nosova.flowershop.utils.SQLStatements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
/*
CREATE TABLE flowershop.bouquet(
 bouquet_id SERIAL PRIMARY KEY,
 name VARCHAR(100) NOT NULL,
 price NUMERIC(10,2) NOT NULL,
 description TEXT
);

 */
public class BouquetDao implements BouquetDaoImpl<Bouquet, Long> {
    SQLStatements sql = new SQLStatements();

    @Override
    public List<Bouquet> select() {
        String query = sql.getQuery("bouquet.select");
        List<Bouquet> list = new ArrayList<>();
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)){
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Bouquet bouquet = Bouquet.builder()
                        .bouquet_id(rs.getLong("bouquet_id"))
                        .name(rs.getString("name"))
                        .price(rs.getDouble("price"))
                        .description(rs.getString("description"))
                        .build();
                list.add(bouquet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public List<Bouquet> find(String name, double minPrise, double maxPrise) {
        String query = sql.getQuery("bouquet.find");
        List<Bouquet> list = new ArrayList<>();
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)){
            statement.setString(1, name);
            if(maxPrise != 0 || minPrise != 0){
                statement.setDouble(2, minPrise);
                statement.setDouble(3, maxPrise);
            }else{
                statement.setNull(2, Types.NUMERIC);
                statement.setNull(3, Types.NUMERIC);
            }

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Bouquet bouquet = Bouquet.builder()
                        .bouquet_id(rs.getLong("bouquet_id"))
                        .name(rs.getString("name"))
                        .price(rs.getDouble("price"))
                        .description(rs.getString("description"))
                        .build();
                list.add(bouquet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Bouquet insert(Bouquet bouquet) {
        String query = sql.getQuery("bouquet.insert");
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)){
            statement.setString(1, bouquet.getName());
            statement.setDouble(2, bouquet.getPrice());
            statement.setString(3, bouquet.getDescription());
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                bouquet.setBouquet_id(rs.getLong("bouquet_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bouquet;
    }

    @Override
    public Bouquet update(Bouquet bouquet) {
        String query = sql.getQuery("bouquet.update");
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)){
            statement.setString(1, bouquet.getName());
            statement.setDouble(2, bouquet.getPrice());
            statement.setString(3, bouquet.getDescription());
            statement.setLong(4, bouquet.getBouquet_id());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bouquet;
    }

    @Override
    public void delete(Bouquet bouquet) {
        deleteBy(bouquet.getBouquet_id());
    }

    @Override
    public void deleteBy(Long aLong) {
        String query = sql.getQuery("bouquet.delete");
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)){
            statement.setInt(1, aLong.intValue());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

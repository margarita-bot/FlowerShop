package ru.nosova.flowershop.dao;

import ru.nosova.flowershop.dao.impl.FlowersDaoImpl;
import ru.nosova.flowershop.model.Flowers;
import ru.nosova.flowershop.utils.DButils;
import ru.nosova.flowershop.utils.SQLStatements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
/*
CREATE TABLE flowershop.flowers(
flower_id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
 price NUMERIC(10,2) NOT NULL,
 quantity INTEGER NOT NULL DEFAULT 0
);
 */
public class FlowersDao implements FlowersDaoImpl <Flowers, Long>{
    SQLStatements sql = new SQLStatements();

    @Override
    public List<Flowers> find() {
        String query = sql.getQuery("flowers.select");
        List<Flowers> list = new ArrayList<>();
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)){
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Flowers flowers = Flowers.builder()
                        .flower_id(rs.getLong("flower_id"))
                        .name(rs.getString("name"))
                        .price(rs.getDouble("price"))
                        .quantity(rs.getInt("quantity"))
                        .imgPath(rs.getString("img_path"))
                        .build();
                list.add(flowers);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public List<Flowers> findByCriteria(String name, double minPrise, double maxPrise) {
        String query = sql.getQuery("flowers.find");
        List<Flowers> list = new ArrayList<>();

        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)){
            statement.setString(1, name);
            if(maxPrise != 0 || minPrise != 0) {
                statement.setDouble(2, minPrise);
                statement.setDouble(3, maxPrise);
            }else {
                statement.setNull(2, Types.NUMERIC);
                statement.setNull(3, Types.NUMERIC);
            }

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Flowers flowers = Flowers.builder()
                        .flower_id(rs.getLong("flower_id"))
                        .name(rs.getString("name"))
                        .price(rs.getDouble("price"))
                        .quantity(rs.getInt("quantity"))
                        .imgPath(rs.getString("img_path"))
                        .build();
                list.add(flowers);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public Flowers insert(Flowers flowers) {
        String query = sql.getQuery("flowers.insert");
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query, new String[] {"flower_id"})){
            statement.setString(1, flowers.getName());
            statement.setDouble(2, flowers.getPrice());
            statement.setInt(3, flowers.getQuantity());
            statement.setString(4, flowers.getImgPath());
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                flowers.setFlower_id(rs.getLong("flower_id"));
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return flowers;
    }

    @Override
    public Flowers update(Flowers flowers) {
        String query = sql.getQuery("flowers.update");
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)){
            statement.setString(1, flowers.getName());
            statement.setDouble(2, flowers.getPrice());
            statement.setInt(3, flowers.getQuantity());
            statement.setString(4, flowers.getImgPath());
            statement.setLong(5, flowers.getFlower_id());
            statement.execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return flowers;
    }

    @Override
    public void updateQuantity(Long aLong, int quantity) {
        String query = sql.getQuery("flowers.updateQuantity");
         try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)){
             statement.setInt(1, quantity);
             statement.setLong(2, aLong);
             statement.executeUpdate();
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
    }

    @Override
    public void delete(Flowers flowers) {
        deleteID(flowers.getFlower_id());
    }

    @Override
    public void deleteID(Long aLong) {
        String query = sql.getQuery("flowers.delete");
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)){
            statement.setInt(1, aLong.intValue());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
            //TODO добавить сообщение об ошибке
        }
    }
}

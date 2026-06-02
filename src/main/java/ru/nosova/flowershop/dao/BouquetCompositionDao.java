package ru.nosova.flowershop.dao;

import ru.nosova.flowershop.dao.impl.BouquetCompositionDaoImpl;
import ru.nosova.flowershop.model.Bouquet;
import ru.nosova.flowershop.model.BouquetComposition;
import ru.nosova.flowershop.model.Flowers;
import ru.nosova.flowershop.utils.DButils;
import ru.nosova.flowershop.utils.SQLStatements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/*
CREATE TABLE flowershop.bouquet_composition(
bouquet_id INTEGER NOT NULL,
 flower_id INTEGER NOT NULL,
 quantity INTEGER NOT NULL CHECK (quantity > 0),
PRIMARY KEY (bouquet_id, flower_id)
);

 */
public class BouquetCompositionDao implements BouquetCompositionDaoImpl{
    SQLStatements sql = new SQLStatements();

    @Override
    public List<BouquetComposition> select(Long bouquetId) {
        String query = sql.getQuery("bouquet_composition.select");
        List<BouquetComposition> list = new ArrayList<>();
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)){
            statement.setLong(1, bouquetId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                Flowers flowers = Flowers.builder()
                        .flower_id(rs.getLong("flower_id"))
                        .name(rs.getString("name"))
                        .price(rs.getDouble("flower_price"))
                        .quantity(rs.getInt("flower_quantity"))
                        .imgPath(rs.getString("img_path"))
                        .build();
                BouquetComposition bouquetComposition = BouquetComposition.builder()
                        .flowers(flowers)
                        .quantity(rs.getInt("composition_quantity"))
                        .price(rs.getDouble("composition_price"))
                        .build();
                list.add(bouquetComposition);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public BouquetComposition insert(BouquetComposition composition) {
        String query = sql.getQuery("bouquet_composition.insert");
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)) {
            statement.setLong(1, composition.getBouquet().getBouquet_id());
            statement.setLong(2, composition.getFlowers().getFlower_id());
            statement.setInt(3, composition.getQuantity());
            statement.setDouble(4, composition.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return composition;
    }

    @Override
    public BouquetComposition update(BouquetComposition composition) {
        String query = sql.getQuery("bouquet_composition.update");
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)) {
            statement.setInt(1, composition.getQuantity());
            statement.setDouble(2, composition.getPrice());
            statement.setLong(3, composition.getBouquet().getBouquet_id());
            statement.setLong(4, composition.getFlowers().getFlower_id());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return composition;
    }

    @Override
    public void delete(Long bouquetId) {
        String query = sql.getQuery("bouquet_composition.delete");
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)){
            statement.setInt(1, Math.toIntExact(bouquetId));
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

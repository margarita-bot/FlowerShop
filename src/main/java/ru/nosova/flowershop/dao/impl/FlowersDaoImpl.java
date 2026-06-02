package ru.nosova.flowershop.dao.impl;

import ru.nosova.flowershop.model.Flowers;
import java.util.List;

public interface FlowersDaoImpl<T, ID> {
    List<Flowers> find();
    List<Flowers> findByCriteria(String name, double minPrise, double maxPrise);
    Flowers insert(Flowers flowers);
    Flowers update (Flowers flowers);
    void updateQuantity(ID id, int quantity);
    void delete (Flowers flowers);
    void deleteID (ID id);
}

package ru.nosova.flowershop.dao.impl;

import ru.nosova.flowershop.model.Bouquet;
import java.util.List;

public interface BouquetDaoImpl<T, ID> {
    List<Bouquet> select();
    List<Bouquet> find(String name, double minPrise, double maxPrise);
    Bouquet insert(Bouquet bouquet);
    Bouquet update (Bouquet bouquet);
    void delete (Bouquet bouquet);
    void deleteBy(ID id);
}

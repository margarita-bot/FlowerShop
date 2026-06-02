package ru.nosova.flowershop.dao.impl;

import ru.nosova.flowershop.model.BouquetComposition;
import java.util.List;

public interface BouquetCompositionDaoImpl {
    List<BouquetComposition> select(Long bouquetId);
    BouquetComposition insert(BouquetComposition bouquetComposition);
    BouquetComposition update (BouquetComposition bouquetComposition);
    void delete (Long bouquetId);
}

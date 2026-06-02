package ru.nosova.flowershop.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class BouquetComposition {
    private int quantity;
    private Bouquet bouquet;
    private Flowers flowers;
    private double price;
}

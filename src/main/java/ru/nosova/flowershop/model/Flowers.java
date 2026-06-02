package ru.nosova.flowershop.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class Flowers {
    private long flower_id;
    private String name;
    private double price;
    private int quantity;
    private String imgPath;
}

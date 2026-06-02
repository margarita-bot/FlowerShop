package ru.nosova.flowershop.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter

public class Bouquet {
    private long bouquet_id;
    private String name;
    private double price;
    private String description;
}

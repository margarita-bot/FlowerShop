package ru.nosova.flowershop.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Builder
public class Delivery {
    private long delivery_id;
    private int quantity;
    private LocalDate date;
    private Flowers flowers;
}

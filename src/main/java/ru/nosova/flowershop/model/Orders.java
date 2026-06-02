package ru.nosova.flowershop.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
@Builder
@Setter
@Getter
public class Orders {
    private long order_id;
    private double price;
    private LocalDate date;

    private Bouquet bouquet;
    private Client client;
    private Florist florist;
    private Address address;
    private Status status;
    private boolean delivery;
    private Time delivereTime;
}
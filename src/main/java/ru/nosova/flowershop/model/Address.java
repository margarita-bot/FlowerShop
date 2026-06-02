package ru.nosova.flowershop.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class Address {
    private long address_id;
    @Builder.Default private String city = "Муром";
    private String street;
    private String house;
    private String entrance;
}

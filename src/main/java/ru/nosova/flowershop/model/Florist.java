package ru.nosova.flowershop.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Florist {
    private long florist_id;
    private String firstname;
    private String lastName;
    private String middleName;
    private String phone;
}

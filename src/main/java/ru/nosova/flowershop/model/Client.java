package ru.nosova.flowershop.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Client {
    private Long client_id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String phone;
}

package ru.nosova.flowershop.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Status {
    private long status_id;
    private String text;
}

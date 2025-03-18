package ru.sber.bookingservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ResourceDTO {
    private Long id;
    private String name;
    private String description;
}

package ru.sber.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phone;
}

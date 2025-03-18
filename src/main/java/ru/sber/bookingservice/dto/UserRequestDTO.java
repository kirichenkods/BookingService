package ru.sber.bookingservice.dto;

import lombok.*;

@Setter
@Getter
@ToString
public class UserRequestDTO {
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String login;
    private String password;
}

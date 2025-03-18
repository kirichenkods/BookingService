package ru.sber.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequestDTO {
    private String login;
    private String password;
}

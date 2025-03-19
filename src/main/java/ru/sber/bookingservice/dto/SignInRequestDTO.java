package ru.sber.bookingservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequestDTO {
    @NotBlank(message = "Логин обязателен")
    private String login;
    @NotBlank(message = "Пароль обязателен")
    private String password;
}

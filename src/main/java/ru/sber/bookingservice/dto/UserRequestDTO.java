package ru.sber.bookingservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Setter
@Getter
@ToString
public class UserRequestDTO {
    @NotBlank(message = "Имя обязательно")
    private String name;

    @NotBlank(message = "Фамилия обязательна")
    private String surname;

    @NotBlank(message = "почта обязательна")
    @Email(message = "Некорректный адрес электронной почты")
    private String email;

    @NotBlank(message = "телефон обязателен")
    @Pattern(regexp = "^(\\+7|8)[0-9]{10}$", message = "Некорректный номер телефона")
    private String phone;

    @NotBlank(message = "Логин обязателен")
    private String login;

    @NotBlank(message = "Пароль обязателен")
    private String password;
}

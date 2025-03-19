package ru.sber.bookingservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ResourceDTO {
    @NotNull(message = "Идентификатор ресурса обязателен")
    private Long id;

    @NotBlank(message = "Название ресурса обязательно")
    @Size(min = 3, max = 100, message = "Длина названия должна быть от 3 до 100 символов")
    private String name;

    @NotBlank(message = "Описание ресурса обязательно")
    private String description;
}

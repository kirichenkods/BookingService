package ru.sber.bookingservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingInfoResponseDTO {
    @NotNull(message = "ID не может быть пустым")
    @Min(value = 1, message = "ID должен быть положительным числом")
    private Long id;

    @NotNull(message = "ID ресурса не может быть пустым")
    @Min(value = 1, message = "ID ресурса должен быть положительным числом")
    private Long resourceId;

    @NotBlank(message = "Название ресурса не может быть пустым")
    @Size(min = 2, max = 100, message = "Название должно содержать от 2 до 100 символов")
    private String resourceName;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @NotNull(message = "Дата начала бронирования обязательна")
    private LocalDateTime dateStart;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @NotNull(message = "Дата окончания бронирования обязательна")
    private LocalDateTime dateEnd;
}

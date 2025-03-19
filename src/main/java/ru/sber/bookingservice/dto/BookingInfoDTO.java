package ru.sber.bookingservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import ru.sber.bookingservice.model.Resource;
import ru.sber.bookingservice.model.User;
import ru.sber.bookingservice.model.enums.DurationType;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingInfoDTO {
    @NotNull(message = "Идентификатор бронирования обязателен")
    private Long id;

    @NotNull(message = "Информация о пользователе обязательна")
    private User user;

    @NotNull(message = "Информация о ресурсе обязательна")
    private Resource resource;

    @NotNull(message = "Тип длительности обязателен")
    private DurationType durationType;

    @PositiveOrZero(message = "Продолжительность должна быть положительной или нулевой")
    @Range(min = 1, max = 1440, message = "Продолжительность должна быть от 1 до 1440 минут")
    private Integer duration;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @NotNull(message = "Дата начала бронирования обязательна")
    private LocalDateTime dateStart;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @NotNull(message = "Дата окончания бронирования обязательна")
    private LocalDateTime dateEnd;
}

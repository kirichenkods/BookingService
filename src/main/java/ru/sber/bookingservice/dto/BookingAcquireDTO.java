package ru.sber.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;
import ru.sber.bookingservice.model.enums.DurationType;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingAcquireDTO {
    private Long userId;
    private Long resourceId;
    private DurationType durationType;
    private Integer duration;
    private LocalDateTime dateStart;
}

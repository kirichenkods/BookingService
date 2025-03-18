package ru.sber.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;
import ru.sber.bookingservice.model.Resource;
import ru.sber.bookingservice.model.User;
import ru.sber.bookingservice.model.enums.DurationType;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingInfoDTO {
    private Long id;
    private User user;
    private Resource resource;
    private DurationType durationType;
    private Integer duration;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
}

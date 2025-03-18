package ru.sber.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingInfoResponseDTO {
    private Long id;
    private Long resourceId;
    private String resourceName;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
}

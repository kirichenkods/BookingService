package ru.sber.bookingservice.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sber.bookingservice.dto.BookingInfoResponseDTO;
import ru.sber.bookingservice.model.BookingInfo;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class BookingInfoMapper {
    private final ModelMapper mapper;

    public BookingInfoResponseDTO toResponseDTO(BookingInfo bookingInfo) {
        BookingInfoResponseDTO result = Objects.isNull(bookingInfo) ?
                null :
                mapper.map(bookingInfo, BookingInfoResponseDTO.class);
        result.setResourceId(bookingInfo.getResource().getId());
        result.setResourceName(bookingInfo.getResource().getName());

        return result;
    }
}
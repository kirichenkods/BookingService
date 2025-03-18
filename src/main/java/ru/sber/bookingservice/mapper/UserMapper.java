package ru.sber.bookingservice.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sber.bookingservice.dto.UserResponseDTO;
import ru.sber.bookingservice.model.User;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class UserMapper {
    private final ModelMapper mapper;

    public UserResponseDTO toUserResponseDTO(User user) {
        return Objects.isNull(user) ?
                null :
                mapper.map(user, UserResponseDTO.class);
    }
}

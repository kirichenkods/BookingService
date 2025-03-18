package ru.sber.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.sber.bookingservice.model.enums.Role;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class JwtAuthenticationResponseDTO {
    private Long userId;
    private Role userRole;
    private String token;
}

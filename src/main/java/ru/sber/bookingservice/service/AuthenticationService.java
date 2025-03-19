package ru.sber.bookingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sber.bookingservice.dto.JwtAuthenticationResponseDTO;
import ru.sber.bookingservice.dto.SignInRequestDTO;
import ru.sber.bookingservice.dto.UserRequestDTO;
import ru.sber.bookingservice.model.User;
import ru.sber.bookingservice.model.enums.Role;
import ru.sber.bookingservice.model.userdetails.CustomUserDetails;

/**
 * Сервис для управления аутентификацией и регистрацией пользователя.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Регистрация пользователя
     * @param userRequestDTO данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponseDTO signUp(UserRequestDTO userRequestDTO) {
        CustomUserDetails user = CustomUserDetails.builder()
                .name(userRequestDTO.getName())
                .surname(userRequestDTO.getSurname())
                .username(userRequestDTO.getLogin())
                .email(userRequestDTO.getEmail())
                .phone(userRequestDTO.getPhone())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .role(Role.ROLE_USER)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialsNotExpired(true)
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponseDTO(user.getId(), user.getRole(), jwt);
    }

    /**
     * Аутентификация пользователя
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponseDTO signIn(SignInRequestDTO request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getLogin(),
                request.getPassword()
        );

        UserDetails userDetails = userService
                .userDetailsService()
                .loadUserByUsername(request.getLogin());
        String jwt = jwtService.generateToken(userDetails);
        User user = userService.getUserByLogin(request.getLogin());

        return new JwtAuthenticationResponseDTO(
                user.getId(),
                user.getRole(),
                jwt);
    }
}

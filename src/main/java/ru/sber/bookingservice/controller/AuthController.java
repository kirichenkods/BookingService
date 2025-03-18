package ru.sber.bookingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sber.bookingservice.dto.JwtAuthenticationResponseDTO;
import ru.sber.bookingservice.dto.SignInRequestDTO;
import ru.sber.bookingservice.dto.UserRequestDTO;
import ru.sber.bookingservice.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponseDTO signUp(@RequestBody UserRequestDTO request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponseDTO signIn(@RequestBody SignInRequestDTO request) {
        return authenticationService.signIn(request);
    }
}

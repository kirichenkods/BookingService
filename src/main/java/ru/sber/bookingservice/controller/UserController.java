package ru.sber.bookingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.bookingservice.dto.UserResponseDTO;
import ru.sber.bookingservice.exceptions.UserNotFoundException;
import ru.sber.bookingservice.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "Управление пользователями")
public class UserController {
    private final UserService service;

    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) throws UserNotFoundException {
        return ResponseEntity.ok(service.getUserDtoById(id));
    }

    @GetMapping("delete/{id}")
    @Operation(summary = "Удаление пользователя")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws UserNotFoundException {
        service.getUserById(id);
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("update")
    @Operation(summary = "Обновление пользователя")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserResponseDTO userResponseDTO)
            throws UserNotFoundException {
        return ResponseEntity.ok(service.update(userResponseDTO));
    }

    @GetMapping
    @Operation(summary = "Получить список всех пользователей")
    public ResponseEntity<List<UserResponseDTO>> getAllResources() {
        return ResponseEntity.ok(service.getAllUser());
    }
}

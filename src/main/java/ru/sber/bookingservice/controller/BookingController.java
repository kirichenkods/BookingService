package ru.sber.bookingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.bookingservice.dto.BookingAcquireDTO;
import ru.sber.bookingservice.dto.BookingInfoResponseDTO;
import ru.sber.bookingservice.exceptions.ResourceNotFoundException;
import ru.sber.bookingservice.exceptions.ResourceUnavailableException;
import ru.sber.bookingservice.exceptions.UserNotFoundException;
import ru.sber.bookingservice.service.BookingInfoService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
@Tag(name = "Резервирование ресурсов")
public class BookingController {
    private final BookingInfoService service;
    private final String messageError = "id пользователя должно быть больше 0";

    @GetMapping("/find/{userId}")
    @Operation(summary = "Получение зарезервированных ресурсов для пользователя")
    public ResponseEntity<List<BookingInfoResponseDTO>> getResourcesByUser(
            @PathVariable @Min(value = 1, message = messageError) Long userId)
            throws UserNotFoundException {
        return ResponseEntity.ok(service.getBookingInfosByUserId(userId));
    }

    @PostMapping
    @Operation(summary = "Бронирование ресурса")
    public ResponseEntity<Long> acquire(@Valid @RequestBody BookingAcquireDTO bookingAcquireDTO)
            throws UserNotFoundException, ResourceUnavailableException, ResourceNotFoundException {
            return ResponseEntity.ok(service.acquire(bookingAcquireDTO));
    }

    @GetMapping("delete/{id}")
    @Operation(summary = "Освободить бронь")
    public ResponseEntity<Void> delete(@PathVariable @Min(value = 1, message = messageError) Long id) {
        service.release(id);
        return ResponseEntity.noContent().build();
    }
}

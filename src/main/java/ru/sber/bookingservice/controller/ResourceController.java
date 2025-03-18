package ru.sber.bookingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.bookingservice.dto.ResourceDTO;
import ru.sber.bookingservice.exceptions.ResourceNotFoundException;
import ru.sber.bookingservice.service.BookingInfoService;
import ru.sber.bookingservice.service.ResourceService;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/resources")
@Tag(name = "Управление ресурсами")
public class ResourceController {
    private final ResourceService resourceService;
    private final BookingInfoService bookingInfoService;

    @GetMapping
    @Operation(summary = "Получить все ресурсы")
    public ResponseEntity<List<ResourceDTO>> getAllResources() {
        return ResponseEntity.ok(resourceService.getAllResources());
    }

    @GetMapping("/{name}")
    @Operation(summary = "Поиск ресурса по имени (или по части имени)")
    public ResponseEntity<List<ResourceDTO>> getAllResourceByName(@PathVariable String name) {
        return ResponseEntity.ok(resourceService.findAllByName(name));
    }

    @GetMapping("/find-between/{dateStart}/{dateEnd}")
    @Operation(summary = "Поиск незанятого ресурса по датам")
    public ResponseEntity<List<ResourceDTO>> findNearest(
            @PathVariable("dateStart") LocalDateTime dateStart,
            @PathVariable("dateEnd") LocalDateTime dateEnd) {

        return ResponseEntity.ok(bookingInfoService.findFreeResourceBetweenDates(dateStart, dateEnd));
    }

    @PostMapping("update")
    @Operation(summary = "Обновление ресурса")
    public ResponseEntity<ResourceDTO> updateResource(@RequestBody ResourceDTO resourceDTO)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(resourceService.update(resourceDTO));
    }

    @GetMapping("delete/{id}")
    @Operation(summary = "Удаление ресурса")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id)
            throws ResourceNotFoundException {
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("create")
    @Operation(summary = "Создание ресурса")
    public ResponseEntity<ResourceDTO> createResource(@RequestBody ResourceDTO resourceDTO) {
        return ResponseEntity.ok(resourceService.createResource(resourceDTO));
    }
}

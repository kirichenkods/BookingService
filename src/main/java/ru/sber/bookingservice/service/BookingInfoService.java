package ru.sber.bookingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sber.bookingservice.dto.BookingAcquireDTO;
import ru.sber.bookingservice.dto.BookingInfoResponseDTO;
import ru.sber.bookingservice.dto.ResourceDTO;
import ru.sber.bookingservice.exceptions.ResourceNotFoundException;
import ru.sber.bookingservice.exceptions.ResourceUnavailableException;
import ru.sber.bookingservice.exceptions.UserNotFoundException;
import ru.sber.bookingservice.mapper.BookingInfoMapper;
import ru.sber.bookingservice.mapper.ResourceMapper;
import ru.sber.bookingservice.model.BookingInfo;
import ru.sber.bookingservice.model.Resource;
import ru.sber.bookingservice.model.User;
import ru.sber.bookingservice.model.enums.DurationType;
import ru.sber.bookingservice.repository.BookingInfoRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления информацией о бронировании ресурсов.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class BookingInfoService {
    private final BookingInfoRepository repository;
    private final BookingInfoMapper bookingInfoMapper;
    private final ResourceMapper resourceMapper;
    private final UserService userService;
    private final ResourceService resourceService;

    /**
     * Метод для бронирования ресурса.
     * @param bookingAcquireDTO Данные для бронирования ресурса.
     * @return Идентификатор забронированного ресурса.
     * @throws ResourceUnavailableException Если ресурс уже занят в указанный период.
     * @throws UserNotFoundException Если пользователь с указанным идентификатором не найден.
     * @throws ResourceNotFoundException Если ресурс с указанным идентификатором не найден.
     */
    @Transactional
    public Long acquire(BookingAcquireDTO bookingAcquireDTO)
            throws ResourceUnavailableException, UserNotFoundException, ResourceNotFoundException {
        BookingInfo bi = BookingInfo.builder()
                .user(userService.getUserById(bookingAcquireDTO.getUserId()))
                .resource(resourceService.getResourceById(bookingAcquireDTO.getResourceId()))
                .durationType(bookingAcquireDTO.getDurationType())
                .duration(bookingAcquireDTO.getDuration())
                .dateStart(bookingAcquireDTO.getDateStart())
                .dateEnd(calculateEndDate(
                        bookingAcquireDTO.getDateStart(),
                        bookingAcquireDTO.getDuration(),
                        bookingAcquireDTO.getDurationType()))
                .build();
        checkResourceAvailability(bi.getResource(), bi.getDateStart(), bi.getDateEnd());
        repository.save(bi);
        return bi.getId();
    }

    /**
     * Метод для проверки доступности ресурса в указанный период.
     * @param resource Ресурс, который нужно проверить.
     * @param dateStart Начальная дата периода.
     * @param dateEnd Конечная дата периода.
     * @throws ResourceUnavailableException Если ресурс занят в указанный период.
     */
    private void checkResourceAvailability(Resource resource,
                                           LocalDateTime dateStart,
                                           LocalDateTime dateEnd) throws ResourceUnavailableException {
        // Проверка, не занят ли ресурс в указанный период
        int count = repository.countByResourceAndDateStartBetween(
                resource,
                dateStart,
                dateEnd);

        if (count > 0) {
            String message = "Ресурс занят в этот период";
            log.error(message);
            throw new ResourceUnavailableException(message);
        }
    }

    /**
     * Метод для расчета конечной даты бронирования.
     * @param dateStart Дата начала бронирования.
     * @param duration Продолжительность бронирования.
     * @param durationType Тип продолжительности.
     * @return Конечная дата бронирования.
     */
    private LocalDateTime calculateEndDate(LocalDateTime dateStart, int duration, DurationType durationType) {
        return dateStart.plus(
                duration,
                ChronoUnit.valueOf(durationType.getDisplayValue()));
    }

    /**
     * Метод для освобождения забронированного ресурса.
     * @param id Идентификатор бронирования.
     */
    public void release(Long id) {
        repository.deleteById(id);
    }

    /**
     * Метод для получения списка всех бронирований для указанного пользователя.
     * @param userId Идентификатор пользователя.
     * @return Список бронирований в формате DTO.
     * @throws UserNotFoundException Если пользователь с указанным идентификатором не найден.
     */
    public List<BookingInfoResponseDTO> getBookingInfosByUserId(Long userId) throws UserNotFoundException {
        List<BookingInfoResponseDTO> responseDTOList = new ArrayList<>();

        User user = userService.getUserById(userId);
        List<BookingInfo> bookingInfos = repository.findAllByUser(user);
        bookingInfos.forEach(bookingInfo -> responseDTOList.add(
                bookingInfoMapper.toResponseDTO(bookingInfo)));
        return responseDTOList;
    }

    /**
     * Метод для поиска свободных ресурсов между указанными датами.
     * @param dateStart Начальная дата периода.
     * @param dateEnd Конечная дата периода.
     * @return Список свободных ресурсов.
     */
    public List<ResourceDTO> findFreeResourceBetweenDates(LocalDateTime dateStart, LocalDateTime dateEnd) {
        if (dateEnd.isBefore(dateStart)) {
            throw new IllegalArgumentException("Дата начала должна бронирования быть меньше даты конца");
        }

        return resourceService.findAll().stream()
                .filter(resource ->
                        repository.countByResourceAndDateStartBetween(resource, dateStart, dateEnd) < 1)
                .map(resourceMapper::toDTO)
                .collect(Collectors.toList());
    }
}

package ru.sber.bookingservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sber.bookingservice.dto.BookingAcquireDTO;
import ru.sber.bookingservice.dto.BookingInfoResponseDTO;
import ru.sber.bookingservice.dto.ResourceDTO;
import ru.sber.bookingservice.exceptions.ResourceNotFoundException;
import ru.sber.bookingservice.exceptions.ResourceUnavailableException;
import ru.sber.bookingservice.exceptions.UserNotFoundException;
import ru.sber.bookingservice.mapper.BookingInfoMapper;
import ru.sber.bookingservice.mapper.ResourceMapper;
import ru.sber.bookingservice.mapper.UserMapper;
import ru.sber.bookingservice.model.BookingInfo;
import ru.sber.bookingservice.model.Resource;
import ru.sber.bookingservice.model.User;
import ru.sber.bookingservice.model.enums.DurationType;
import ru.sber.bookingservice.model.enums.Role;
import ru.sber.bookingservice.repository.BookingInfoRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingInfoServiceTest {
    @Mock
    private BookingInfoRepository repository;
    @Mock
    private UserService userService;
    @Mock
    private ResourceService resourceService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ResourceMapper resourceMapper;
    @Mock
    private BookingInfoMapper bookingInfoMapper;

    @InjectMocks
    private BookingInfoService service;

    private User user;
    private Resource resource;
    private BookingAcquireDTO dto;

    @BeforeEach
    void setup() {
        user = User.builder()
                .id(1L)
                .name("Test")
                .surname("Testov")
                .phone("89997773355")
                .email("test@mail.com")
                .login("login")
                .password("password")
                .role(Role.ROLE_USER)
                .build();

        resource = new Resource();
        resource.setId(1L);
        resource.setName("resource_name");
        resource.setDescription("resource_desr");

        dto = new BookingAcquireDTO();
        dto.setUserId(1L);
        dto.setResourceId(1L);
        dto.setDurationType(DurationType.DAYS);
        dto.setDuration(1);
        dto.setDateStart(LocalDateTime.now());
    }

    @Test
    void givenUserAndResourceExist_whenAcquire_thenReturnBookingId() throws Exception {
        when(userService.getUserById(dto.getUserId())).thenReturn(user);
        when(resourceService.getResourceById(dto.getResourceId())).thenReturn(resource);
        when(repository.countByResourceAndDateStartBetween(any(), any(), any())).thenReturn(0);

        service.acquire(dto);

        verify(repository).save(any(BookingInfo.class));
    }

    @Test
    void givenUserNotFound_whenAcquire_thenThrowUserNotFoundException() throws UserNotFoundException {
        when(userService.getUserById(dto.getUserId())).thenThrow(
                new UserNotFoundException("Пользователь с id = " + dto.getUserId() + " не найден"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> service.acquire(dto));

        assertTrue(exception.getMessage().contains("Пользователь с id = " +
                dto.getUserId() + " не найден"));
    }

    @Test
    void givenResourceNotFound_whenAcquire_thenThrowResourceNotFoundException()
            throws UserNotFoundException, ResourceNotFoundException {
        when(userService.getUserById(dto.getUserId())).thenReturn(user);
        when(resourceService.getResourceById(dto.getResourceId()))
                .thenThrow(new ResourceNotFoundException("Ресурс с id = " + dto.getUserId() + " не найден"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.acquire(dto));

        assertTrue(exception.getMessage().contains("Ресурс с id = " + dto.getUserId() + " не найден"));
    }

    @Test
    void givenResourceAlreadyBooked_whenAcquire_thenThrowResourceUnavailableException()
            throws UserNotFoundException, ResourceNotFoundException {
        when(userService.getUserById(dto.getUserId())).thenReturn(user);
        when(resourceService.getResourceById(dto.getResourceId())).thenReturn(resource);
        when(repository.countByResourceAndDateStartBetween(any(), any(), any())).thenReturn(1);

        ResourceUnavailableException exception = assertThrows(
                ResourceUnavailableException.class,
                () -> service.acquire(dto));

        assertTrue(exception.getMessage().contains("Ресурс занят в этот период"));
    }

    @Test
    void givenBookingExists_whenRelease_thenDeleteBooking() {
        doNothing().when(repository).deleteById(anyLong());
        service.release(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void givenUserHasBookings_whenGetBookingInfosByUserId_thenReturnBookingInfos() throws UserNotFoundException {
        List<BookingInfo> bookings = Arrays.asList(new BookingInfo(), new BookingInfo());
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(repository.findAllByUser(user)).thenReturn(bookings);

        List<BookingInfoResponseDTO> result = service.getBookingInfosByUserId(1L);

        assertFalse(result.isEmpty());
        verify(userService).getUserById(1L);
        verify(repository).findAllByUser(user);
    }

    @Test
    void givenResourcesAvailable_whenFindFreeResourceBetweenDates_thenReturnFreeResources() {
        List<Resource> resources = Arrays.asList(new Resource(), new Resource());
        when(resourceService.findAll()).thenReturn(resources);
        when(repository.countByResourceAndDateStartBetween(any(), any(), any())).thenReturn(0);

        List<ResourceDTO> freeResources = service.findFreeResourceBetweenDates(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1));

        assertFalse(freeResources.isEmpty());
        verify(resourceService).findAll();
        verify(repository, times(2))
                .countByResourceAndDateStartBetween(any(), any(), any());
    }
  
}
package ru.sber.bookingservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.sber.bookingservice.dto.UserResponseDTO;
import ru.sber.bookingservice.exceptions.UserNotFoundException;
import ru.sber.bookingservice.model.User;
import ru.sber.bookingservice.model.enums.Role;
import ru.sber.bookingservice.model.userdetails.CustomUserDetails;
import ru.sber.bookingservice.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    User user;
    CustomUserDetails customUserDetails;
    UserResponseDTO userResponseDTO;

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

        customUserDetails = CustomUserDetails.builder()
                .name("Test")
                .surname("Testov")
                .email("test@mail.com")
                .phone("89997773355")
                .username("login")
                .password("password")
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    void givenExistingUserId_whenDeleteUser_thenDeleteFromRepository() {
        long userId = 1L;

        userService.deleteUser(userId);

        verify(repository, times(1)).deleteById(userId);
    }

    @Test
    void givenNonExistingUserId_whenGetUserDtoById_thenThrowUserNotFoundException() {
        long userId = 99L;
        when(repository.findById(userId)).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserDtoById(userId));

        verify(repository, times(1)).findById(userId);
    }


    @Test
    void givenExistingUserId_whenGetUserById_thenReturnUser() throws UserNotFoundException {
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(repository.findById(userId)).thenReturn(java.util.Optional.of(user));

        User result = userService.getUserById(userId);

        assertNotNull(result);
        verify(repository, times(1)).findById(userId);
    }

    @Test
    void givenNonExistingUserId_whenGetUserById_thenThrowUserNotFoundException() {
        long userId = 99L;
        when(repository.findById(userId)).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));

        verify(repository, times(1)).findById(userId);
    }

    @Test
    void givenNonExistingUser_whenUpdate_thenThrowUserNotFoundException() {
        long userId = 99L;
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(userId);
        when(repository.findById(userId)).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(userResponseDTO));

        verify(repository, times(1)).findById(userId);
    }

    @Test
    void givenNewUserDetails_whenCreate_thenSaveUserToRepository() {
        userService.create(customUserDetails);

        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void givenExistingUserLogin_whenGetUserByLogin_thenReturnUser() {
        String login = "login";
        when(repository.findUserByLogin(login)).thenReturn(java.util.Optional.of(user));

        User result = userService.getUserByLogin(login);

        assertNotNull(result);
        verify(repository, times(1)).findUserByLogin(login);
    }

    @Test
    void givenNonExistingUserLogin_whenGetUserByLogin_thenThrowUsernameNotFoundException() {
        String login = "nonexistentuser";
        when(repository.findUserByLogin(login)).thenReturn(java.util.Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByLogin(login));

        verify(repository, times(1)).findUserByLogin(login);
    }


}
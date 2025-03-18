package ru.sber.bookingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sber.bookingservice.dto.UserResponseDTO;
import ru.sber.bookingservice.exceptions.UserNotFoundException;
import ru.sber.bookingservice.mapper.UserMapper;
import ru.sber.bookingservice.model.User;
import ru.sber.bookingservice.repository.UserRepository;
import ru.sber.bookingservice.model.userdetails.CustomUserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для управления пользователями.
 */
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserMapper mapper;
    private final UserRepository repository;

    /**
     * Метод для удаления пользователя по идентификатору.
     * @param id Идентификатор пользователя.
     */
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    /**
     * Метод для получения информации о пользователе в формате DTO по идентификатору.
     * @param id Идентификатор пользователя.
     * @return Информация о пользователе в формате DTO.
     * @throws UserNotFoundException Если пользователь с указанным идентификатором не найден.
     */
    public UserResponseDTO getUserDtoById(Long id) throws UserNotFoundException {
        return mapper.toUserResponseDTO(getUserById(id));
    }

    /**
     * Метод для получения сущности пользователя по идентификатору.
     * @param id Идентификатор пользователя.
     * @return Сущность пользователя.
     * @throws UserNotFoundException Если пользователь с указанным идентификатором не найден.
     */
    public User getUserById(Long id) throws UserNotFoundException {
        return repository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id = " + id + " не найден"));
    }

    /**
     * Метод для получения списка всех пользователей в формате DTO.
     * @return Список всех пользователей в формате DTO.
     */
    public List<UserResponseDTO> getAllUser() {
        List<User> userList = repository.findAll();
        List<UserResponseDTO> userResponseDTOS = new ArrayList<>();
        userList.forEach(user -> userResponseDTOS.add(mapper.toUserResponseDTO(user)));
        return userResponseDTOS;
    }

    /**
     * Метод для обновления информации о пользователе.
     * @param userResponseDTO Обновленные данные пользователя в формате DTO.
     * @return Обновленная информация о пользователе в формате DTO.
     * @throws UserNotFoundException Если пользователь с указанным идентификатором не найден.
     */
    public UserResponseDTO update(UserResponseDTO userResponseDTO) throws UserNotFoundException {
        // Проверяем, существует ли пользователь с указанным id
        User user = repository.findById(userResponseDTO.getId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        // Обновляем данные пользователя
        user.setName(userResponseDTO.getName());
        user.setSurname(userResponseDTO.getSurname());
        user.setEmail(userResponseDTO.getEmail());
        user.setPhone(userResponseDTO.getPhone());

        // Сохраняем изменения и возвращаем обновленного пользователя
        return mapper.toUserResponseDTO(repository.save(user));
    }

    /**
     * Метод для создания нового пользователя.
     * @param customUserDetails
     */
    public void create(CustomUserDetails customUserDetails) {
        User user = User.builder()
                .name(customUserDetails.getName())
                .surname(customUserDetails.getSurname())
                .email(customUserDetails.getEmail())
                .phone(customUserDetails.getPhone())
                .login(customUserDetails.getUsername())
                .password(customUserDetails.getPassword())
                .role(customUserDetails.getRole())
                .build();
        repository.save(user);
    }

    /**
     * Метод для получения сущности пользователя по логину.
     * @param login Логин пользователя.
     * @return Сущность пользователя.
     * @throws UsernameNotFoundException Если пользователь с указанным логином не найден.
     */
    User getUserByLogin(String login) {
        return repository.findUserByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    public UserDetailsService userDetailsService() {
        return this::getCustomUserByLogin;
    }

    /**
     * Метод для получения CustomUserDetails по логину.
     * @param login Логин пользователя.
     * @return CustomUserDetails
     */
    private CustomUserDetails getCustomUserByLogin(String login) {
        User user = getUserByLogin(login);
        return CustomUserDetails.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .username(user.getLogin())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }
}

package ru.sber.bookingservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.sber.bookingservice.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @NonNull
    List<User> findAll();

    Optional<User> findByEmailAndPhone(String email, String phone);

    Optional<User> findUserByLogin(String login);
}

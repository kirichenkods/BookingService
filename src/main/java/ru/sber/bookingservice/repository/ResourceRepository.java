package ru.sber.bookingservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.sber.bookingservice.model.Resource;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository extends CrudRepository<Resource, Long> {
    @NonNull
    Optional<Resource> findById(Long id);

    Optional<Resource> findByName(String name);

    List<Resource> findResourceByNameContainingIgnoreCase(String name);

    @NonNull
    List<Resource> findAll();
}

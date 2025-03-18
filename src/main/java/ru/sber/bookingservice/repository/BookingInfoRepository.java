package ru.sber.bookingservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.sber.bookingservice.model.BookingInfo;
import ru.sber.bookingservice.model.Resource;
import ru.sber.bookingservice.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingInfoRepository extends CrudRepository<BookingInfo, Long> {
    @NonNull
    Optional<BookingInfo> findById(@NonNull Long id);

    List<BookingInfo> findAllByUser(User user);

    @Override
    void deleteById(@NonNull Long id);

    int countByResourceAndDateStartBetween(Resource resource,
                                           LocalDateTime dateStart,
                                           LocalDateTime dateEnd);
}

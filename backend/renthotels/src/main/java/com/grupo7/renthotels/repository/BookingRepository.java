package com.grupo7.renthotels.repository;

import com.grupo7.renthotels.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingSku(Long bookingSku);
    List<Booking> findAllByBookingStartDateBetween(LocalDate BookingStartDate, LocalDate BookingEndDate);

    List<Booking> findByUserUserSku(Long userSku);

    List<Booking> findByProductProductSku(Long productSku);
}

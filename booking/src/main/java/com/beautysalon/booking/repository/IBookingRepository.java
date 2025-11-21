package com.beautysalon.booking.repository;

import com.beautysalon.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.time.LocalTime;

public interface IBookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByClientUserIdOrderByBookingDateDesc(UUID userId);
    List<Booking> findByMasterMasterIdOrderByBookingDateDesc(UUID masterId);
    List<Booking> findByMasterMasterIdAndBookingDate(UUID masterId, LocalDate date);
    Optional<Booking> findByMasterMasterIdAndBookingDateAndBookingTime(UUID masterId, LocalDate date, LocalTime time);
}
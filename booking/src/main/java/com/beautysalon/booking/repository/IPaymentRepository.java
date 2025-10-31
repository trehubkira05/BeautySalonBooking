package com.beautysalon.booking.repository;

import com.beautysalon.booking.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface IPaymentRepository extends JpaRepository<Payment, UUID> {
    Payment findByBookingBookingId(UUID bookingId);
}
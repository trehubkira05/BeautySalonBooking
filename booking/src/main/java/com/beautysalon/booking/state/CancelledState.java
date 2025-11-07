package com.beautysalon.booking.state;

import com.beautysalon.booking.entity.Booking;
import org.springframework.stereotype.Service;

@Service
public class CancelledState implements BookingState {
    @Override
    public void confirm(Booking booking) {
        throw new IllegalStateException("Бронювання скасовано");
    }

    @Override
    public void pay(Booking booking) {
        throw new IllegalStateException("Бронювання скасовано");
    }

    @Override
    public void cancel(Booking booking) {
    }

    @Override
    public void complete(Booking booking) {
        throw new IllegalStateException("Бронювання скасовано");
    }
}
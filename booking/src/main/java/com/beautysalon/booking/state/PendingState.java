package com.beautysalon.booking.state;

import com.beautysalon.booking.entity.Booking;
import com.beautysalon.booking.entity.BookingStatus;
import org.springframework.stereotype.Service;

@Service
public class PendingState implements BookingState {

    @Override
    public void confirm(Booking booking) {
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setState(new ConfirmedState());
    }

    @Override
    public void pay(Booking booking) {
        throw new IllegalStateException("Неможливо оплатити без підтвердження");
    }

    @Override
    public void cancel(Booking booking) {
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setState(new CancelledState());
    }

    @Override
    public void complete(Booking booking) {
        throw new IllegalStateException("Неможливо завершити без оплати");
    }
}
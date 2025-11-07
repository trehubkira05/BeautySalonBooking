package com.beautysalon.booking.state;

import com.beautysalon.booking.entity.Booking;
import com.beautysalon.booking.entity.BookingStatus;
import org.springframework.stereotype.Service;

@Service
public class PaidState implements BookingState {

    @Override
    public void confirm(Booking booking) {
        throw new IllegalStateException("Вже підтверджено");
    }

    @Override
    public void pay(Booking booking) {
        throw new IllegalStateException("Вже оплачено");
    }

    @Override
    public void cancel(Booking booking) {
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setState(new CancelledState());
    }

    @Override
    public void complete(Booking booking) {
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setState(new CompletedState()); 
    }
}
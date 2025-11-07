package com.beautysalon.booking.state;

import com.beautysalon.booking.entity.Booking;
import com.beautysalon.booking.entity.BookingStatus;
import org.springframework.stereotype.Service;

@Service
public class ConfirmedState implements BookingState {
    @Override

    public void pay(Booking booking) {
        booking.setStatus(BookingStatus.PAID);
        booking.setState(new PaidState());
    }

    @Override
    public void cancel(Booking booking) {
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setState(new CancelledState());
    }

    @Override
    public void confirm(Booking booking) { /* вже підтверджено */ }
    @Override
    public void complete(Booking booking) { /* не оплачено */ }
}
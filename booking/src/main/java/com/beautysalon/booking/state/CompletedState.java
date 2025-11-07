package com.beautysalon.booking.state;

import com.beautysalon.booking.entity.Booking;
import org.springframework.stereotype.Service;
@Service
public class CompletedState implements BookingState {
    @Override
    public void complete(Booking booking) { /* вже завершено */ }
    @Override
    public void confirm(Booking booking) { throw new IllegalStateException("Завершено"); }
    @Override
    public void pay(Booking booking) { throw new IllegalStateException("Завершено"); }
    @Override
    public void cancel(Booking booking) { throw new IllegalStateException("Завершено"); }
}
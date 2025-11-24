package com.beautysalon.booking.state;

import com.beautysalon.booking.entity.Booking;

public interface BookingState {

    void confirm(Booking booking);
    
    void pay(Booking booking);
    
    void cancel(Booking booking);
    
    void complete(Booking booking);
}
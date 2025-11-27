package com.beautysalon.booking.observer;

import com.beautysalon.booking.entity.Booking;

public interface IBookingObserver {
    
    /**
     * @param booking Об'єкт бронювання, в якому відбулися зміни.
     */
    void update(Booking booking);
}
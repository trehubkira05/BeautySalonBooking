package com.beautysalon.booking.observer;

import com.beautysalon.booking.entity.Booking;
import org.springframework.stereotype.Component;

@Component
public class EmailObserver implements IBookingObserver {

    @Override
    public void update(Booking booking) {
        System.out.println(
            "--- [EmailObserver] ---" +
            "\nНадсилаємо email клієнту: " + booking.getClient().getEmail() +
            "\nТема: Статус вашого бронювання змінено" +
            "\nНовий статус: " + booking.getStatus() +
            "\n-----------------------"
        );
    }
}
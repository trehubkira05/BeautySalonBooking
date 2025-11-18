// src/main/java/com/beautysalon/booking/StateTest.java
package com.beautysalon.booking;

import com.beautysalon.booking.entity.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StateTest implements CommandLineRunner {

    @Autowired
    private Booking booking;

    public static void main(String[] args) {
        SpringApplication.run(StateTest.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("1. Початок: " + booking.getStatus());
        booking.confirm();
        System.out.println("2. Після confirm(): " + booking.getStatus());
        booking.pay();
        System.out.println("3. Після pay(): " + booking.getStatus());
        booking.complete();
        System.out.println("4. Після complete(): " + booking.getStatus());
        try { booking.cancel(); }
        catch (Exception e) { System.out.println("5. Спроба cancel(): " + e.getMessage()); }
    }
}
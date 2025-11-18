package com.beautysalon.booking;

import com.beautysalon.booking.entity.Booking;
import com.beautysalon.booking.entity.BookingStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingApplicationTests {

    @Test
    void testSuccessfulStateFlow() {
        System.out.println("--- Початок тесту 'testSuccessfulStateFlow' ---");

        Booking booking = new Booking();

        assertEquals(BookingStatus.PENDING, booking.getStatus());
        System.out.println("Стан (1): " + booking.getStatus());

        booking.confirm();
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        System.out.println("Стан (2) після confirm(): " + booking.getStatus());

        booking.pay();
        assertEquals(BookingStatus.PAID, booking.getStatus());
        System.out.println("Стан (3) після pay(): " + booking.getStatus());

        booking.complete();
        assertEquals(BookingStatus.COMPLETED, booking.getStatus());
        System.out.println("Стан (4) після complete(): " + booking.getStatus());

        System.out.println("--- Тест 'testSuccessfulStateFlow' успішний ---");
    }

    @Test
    void testInvalidStateTransitions() {
        System.out.println("--- Початок тесту 'testInvalidStateTransitions' ---");

        Booking pendingBooking = new Booking();
        assertEquals(BookingStatus.PENDING, pendingBooking.getStatus());

        Exception ex1 = assertThrows(IllegalStateException.class, () -> {
            pendingBooking.pay();
        });

        assertEquals("Бронювання не може бути оплачено, поки воно не підтверджено.", ex1.getMessage());
        System.out.println("Перевірка (1) 'pay() on PENDING' успішна: " + ex1.getMessage());

        Booking completedBooking = new Booking();
        completedBooking.confirm();
        completedBooking.pay();
        completedBooking.complete();
        assertEquals(BookingStatus.COMPLETED, completedBooking.getStatus());

        Exception ex2 = assertThrows(IllegalStateException.class, () -> {
            completedBooking.cancel();
        });

        assertEquals("Бронювання не може бути скасовано, оскільки воно вже завершено.", ex2.getMessage());
        System.out.println("Перевірка (2) 'cancel() on COMPLETED' успішна: " + ex2.getMessage());

        System.out.println("--- Тест 'testInvalidStateTransitions' успішний ---");
    }
}
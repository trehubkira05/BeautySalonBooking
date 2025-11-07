package com.beautysalon.booking;
import com.beautysalon.booking.entity.Booking;
import com.beautysalon.booking.entity.BookingStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingApplicationTests {

    @Autowired
    private Booking booking; 

    @Test
    void testStatePattern() {
        assertEquals(BookingStatus.PENDING, booking.getStatus());
        System.out.println("1. Початок: " + booking.getStatus());

        booking.confirm();
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        System.out.println("2. Після confirm(): " + booking.getStatus());

        booking.pay();
        assertEquals(BookingStatus.PAID, booking.getStatus());
        System.out.println("3. Після pay(): " + booking.getStatus());

        booking.complete();
        assertEquals(BookingStatus.COMPLETED, booking.getStatus());
        System.out.println("4. Після complete(): " + booking.getStatus());

        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            booking::cancel
        );
        System.out.println("5. Спроба cancel(): " + exception.getMessage());
    }
}
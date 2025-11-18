package com.beautysalon.booking;

import com.beautysalon.booking.entity.Booking;
import com.beautysalon.booking.entity.BookingStatus;
import com.beautysalon.booking.entity.Master;
import com.beautysalon.booking.entity.Service;
import com.beautysalon.booking.entity.User;
import com.beautysalon.booking.repository.IMasterRepository;
import com.beautysalon.booking.repository.IServiceRepository;
import com.beautysalon.booking.repository.IUserRepository;
import com.beautysalon.booking.service.BookingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional 
class BookingApplicationTests {
    
    @Test
    void testSuccessfulStateFlow() {
        System.out.println("--- Тест ЛР4: testSuccessfulStateFlow ---");
        Booking booking = new Booking();
        booking.confirm();
        booking.pay();
        booking.complete();
        assertEquals(BookingStatus.COMPLETED, booking.getStatus());
        System.out.println("--- Тест ЛР4: testSuccessfulStateFlow [УСПІХ] ---");
    }

    @Test
    void testInvalidStateTransitions() {
        System.out.println("--- Тест ЛР4: testInvalidStateTransitions ---");
        Booking pendingBooking = new Booking();
        assertThrows(IllegalStateException.class, pendingBooking::pay);
        System.out.println("--- Тест ЛР4: testInvalidStateTransitions [УСПІХ] ---");
    }

    @Autowired
    private BookingService bookingService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IMasterRepository masterRepository;
    @Autowired
    private IServiceRepository serviceRepository;
    
    private UUID validClientId;
    private UUID validMasterId;
    private UUID validServiceId;
    private LocalDateTime time = LocalDateTime.now();

    @BeforeEach
    void setUpRealDatabaseData() {
        User client = new User("Test Client", "client@test.com", "pass", "123");
        userRepository.saveAndFlush(client);
        this.validClientId = client.getUserId();

        Master master = new Master(client, "Test Spec", 5);
        masterRepository.saveAndFlush(master);
        this.validMasterId = master.getMasterId();

        com.beautysalon.booking.entity.Service service = 
            new com.beautysalon.booking.entity.Service("Test Service", "Desc", 100, 30);
        serviceRepository.saveAndFlush(service);
        this.validServiceId = service.getServiceId();
    }

    @Test
    void testChainOfResponsibility_Failure_RealDB() {
        System.out.println("--- Тест ЛР5: testChainOfResponsibility_Failure (Real DB) ---");
        
        UUID invalidClientId = UUID.randomUUID(); 
        
        Exception ex = assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(invalidClientId, validMasterId, validServiceId, time);
        });

        assertEquals("Клієнт не знайдений.", ex.getMessage());
        System.out.println("--- Тест ЛР5: testChainOfResponsibility_Failure [УСПІХ] ---");
    }
    
    @Test
    void testChainOfResponsibility_Success_RealDB() {
        System.out.println("--- Тест ЛР5: testChainOfResponsibility_Success (Real DB) ---");
        
        assertDoesNotThrow(() -> {
            bookingService.createBooking(validClientId, validMasterId, validServiceId, time);
        });
        
        System.out.println("--- Тест ЛР5: testChainOfResponsibility_Success [УСПІХ] ---");
    }
}
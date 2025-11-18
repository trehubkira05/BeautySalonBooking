package com.beautysalon.booking.service;

import com.beautysalon.booking.entity.Booking;
import com.beautysalon.booking.entity.Payment;
import com.beautysalon.booking.repository.IBookingRepository;
import com.beautysalon.booking.repository.IPaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentFacade {

    private final IBookingRepository bookingRepository;
    private final IPaymentRepository paymentRepository;
    private final BookingService bookingService;

    public PaymentFacade(
            IBookingRepository bookingRepository, 
            IPaymentRepository paymentRepository,
            BookingService bookingService
    ) {
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.bookingService = bookingService;
    }

    @Transactional
    public Booking payForBooking(UUID bookingId, String paymentMethod) {
        
        System.out.println("--- [PaymentFacade] ---");
        System.out.println("Фасад отримав запит на оплату бронювання: " + bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Бронювання не знайдено."));

        System.out.println("Фасад: Крок 1. Виклик патерну State (booking.pay())...");
        booking.pay();

        System.out.println("Фасад: Крок 2. Симуляція виклику API (" + paymentMethod + ")...");
        boolean paymentSuccess = simulateExternalPayment(booking.getTotalPrice());

        if (!paymentSuccess) {
            throw new RuntimeException("Зовнішній платіж не вдалося виконати.");
        }

        System.out.println("Фасад: Крок 3. Створення запису Payment в БД...");
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        Booking savedBooking = bookingRepository.save(booking);

        System.out.println("Фасад: Крок 4. Виклик патерну Observer...");
        bookingService.notifyPaymentObservers(savedBooking);

        System.out.println("--- [PaymentFacade] Завершено ---");
        return savedBooking;
    }

    private boolean simulateExternalPayment(double amount) {
        System.out.println("...З'єднання з API... обробка " + amount + " грн... Успіх.");
        return true;
    }
}
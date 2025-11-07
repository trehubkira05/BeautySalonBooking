package com.beautysalon.booking.service;

import com.beautysalon.booking.entity.Booking;
import com.beautysalon.booking.entity.Master;
import com.beautysalon.booking.entity.User;
import com.beautysalon.booking.entity.BookingStatus;
import com.beautysalon.booking.repository.IBookingRepository;
import com.beautysalon.booking.repository.IMasterRepository;
import com.beautysalon.booking.repository.IServiceRepository;
import com.beautysalon.booking.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    private final IBookingRepository bookingRepository;
    private final IUserRepository userRepository;
    private final IServiceRepository serviceRepository;
    private final IMasterRepository masterRepository;

    // Конструктор для ін'єкції залежностей
    public BookingService(
            IBookingRepository bookingRepository,
            IUserRepository userRepository,
            IServiceRepository serviceRepository,
            IMasterRepository masterRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.masterRepository = masterRepository;
    }

     //Створення бронювання
     
    public Booking createBooking(UUID clientId, UUID serviceId, UUID masterId, LocalDateTime desiredDateTime) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Клієнт не знайдений."));
        com.beautysalon.booking.entity.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Послугу не знайдено."));
        Master master = masterRepository.findById(masterId)
                .orElseThrow(() -> new RuntimeException("Майстер не знайдений."));

        Booking newBooking = new Booking();
        newBooking.setClient(client);
        newBooking.setMaster(master);
        newBooking.setService(service);
        newBooking.setBookingDate(desiredDateTime.toLocalDate());
        newBooking.setBookingTime(desiredDateTime.toLocalTime());
        newBooking.setTotalPrice(service.getPrice());
        newBooking.setStatus(BookingStatus.PENDING); // Використовуйте enum

        return bookingRepository.save(newBooking);
    }

    
     //Підтвердження бронювання.
     
    public Booking confirmBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Бронювання не знайдено."));
        booking.confirm(); // Виклик методу через State
        return bookingRepository.save(booking);
    }
    
     // Оплата бронювання.
     
    public Booking payBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Бронювання не знайдено."));
        booking.pay(); // Виклик методу через State
        return bookingRepository.save(booking);
    }

     //Скасування бронювання.
    
    public Booking cancelBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Бронювання не знайдено."));
        booking.cancel(); // Виклик методу через State
        return bookingRepository.save(booking);
    }

     //Завершення бронювання.
     
    public Booking completeBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Бронювання не знайдено."));
        booking.complete(); // Виклик методу через State
        return bookingRepository.save(booking);
    }

    // Отримання бронювань клієнта.
     
    public List<Booking> getBookingsByClient(UUID clientId) {
        return bookingRepository.findByClientUserIdOrderByBookingDateDesc(clientId);
    }
}
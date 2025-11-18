package com.beautysalon.booking.service;

import com.beautysalon.booking.entity.*;
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

    public BookingService(IBookingRepository bookingRepository, IUserRepository userRepository, IServiceRepository serviceRepository, IMasterRepository masterRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.masterRepository = masterRepository;
    }

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
        newBooking.setStatus(BookingStatus.PENDING);

        return bookingRepository.save(newBooking);
    }

    public Booking confirmBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Бронювання не знайдено."));

        booking.confirm();

        return bookingRepository.save(booking);
    }

    public Booking payBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Бронювання не знайдено."));

        booking.pay();

        return bookingRepository.save(booking);
    }

    public Booking completeBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Бронювання не знайдено."));

        booking.complete();

        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Бронювання не знайдено."));

        booking.cancel();

        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByClient(UUID clientId) {
        return bookingRepository.findByClientUserIdOrderByBookingDateDesc(clientId);
    }
}
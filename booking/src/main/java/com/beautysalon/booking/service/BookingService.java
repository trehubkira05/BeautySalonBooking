package com.beautysalon.booking.service;

import com.beautysalon.booking.entity.Booking;
import com.beautysalon.booking.entity.Master;
import com.beautysalon.booking.entity.User;
import com.beautysalon.booking.repository.IBookingRepository;
import com.beautysalon.booking.repository.IMasterRepository;
import com.beautysalon.booking.repository.IServiceRepository;
import com.beautysalon.booking.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// Використовуємо анотацію Spring
@Service
public class BookingService {

    private final IBookingRepository bookingRepository;
    private final IUserRepository userRepository;
    private final IServiceRepository serviceRepository;
    private final IMasterRepository masterRepository;

    // Конструктор для ін'єкції залежностей
    public BookingService(IBookingRepository bookingRepository, IUserRepository userRepository, IServiceRepository serviceRepository, IMasterRepository masterRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.masterRepository = masterRepository;
    }

    /**
     * Сценарій: Створення бронювання
     * Реалізує логіку перевірки сутностей та збереження бронювання.
     */
    public Booking createBooking(UUID clientId, UUID serviceId, UUID masterId, LocalDateTime desiredDateTime) {
        // 1. Перевірка передумов (Клієнт, Послуга, Майстер існують)
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Клієнт не знайдений."));

        // Використовуємо повне ім'я для Entity Service, щоб уникнути конфлікту з @Service
        com.beautysalon.booking.entity.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Послугу не знайдено."));
        
        Master master = masterRepository.findById(masterId)
                .orElseThrow(() -> new RuntimeException("Майстер не знайдений."));

        // TODO: Тут потрібна складна логіка перевірки розкладу (ScheduleRepository)
        // if (!isSlotAvailable(...)) {
        //    throw new RuntimeException("Обраний час недоступний для майстра.");
        // }

        // 2. Формування нового бронювання
        Booking newBooking = new Booking();
        newBooking.setClient(client);
        newBooking.setMaster(master);
        newBooking.setService(service);
        newBooking.setBookingDate(desiredDateTime.toLocalDate());
        newBooking.setBookingTime(desiredDateTime.toLocalTime());
        newBooking.setTotalPrice(service.getPrice());
        newBooking.setStatus("Pending");
        
        // 3. Збереження (Крок 7 сценарію)
        return bookingRepository.save(newBooking);
    }

    /**
     * Сценарій: Переглянути/скасувати бронювання (Перегляд)
     * ЦЕЙ МЕТОД ВИРІШУЄ ПОМИЛКУ КОМПІЛЯЦІЇ В КОНТРОЛЕРІ.
     */
    public List<Booking> getBookingsByClient(UUID clientId) {
        // Викликає спеціалізований метод репозиторію для пошуку бронювань клієнта
        return bookingRepository.findByClientUserIdOrderByBookingDateDesc(clientId);
    }
    
    /**
     * Сценарій: Переглянути/скасувати бронювання (Скасування)
     */
    public Booking cancelBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Бронювання не знайдено."));

        if (booking.getStatus().equals("Confirmed") || booking.getStatus().equals("Pending")) {
            booking.setStatus("Cancelled");
            // TODO: Додати логіку повернення коштів, якщо статус був "Paid"
            return bookingRepository.save(booking);
        } else {
            throw new RuntimeException("Бронювання не може бути скасоване. Поточний статус: " + booking.getStatus());
        }
    }
}
package com.beautysalon.booking.service;

import com.beautysalon.booking.entity.*;
import com.beautysalon.booking.repository.IBookingRepository;
import com.beautysalon.booking.repository.IMasterRepository;
import com.beautysalon.booking.repository.IServiceRepository;
import com.beautysalon.booking.repository.IUserRepository;
import com.beautysalon.booking.validation.*;
import com.beautysalon.booking.composite.BookableItem;
import com.beautysalon.booking.composite.ServicePackage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    private final IBookingRepository bookingRepository;
    private final IBookingValidationHandler validationChain;
    private final BookingEventPublisher eventPublisher;
    private final PaymentFacade paymentFacade;

    public BookingService(
            IBookingRepository bookingRepository,
            IUserRepository userRepository,
            IServiceRepository serviceRepository,
            IMasterRepository masterRepository,
            BookingEventPublisher eventPublisher,
            @Lazy PaymentFacade paymentFacade) {
        this.bookingRepository = bookingRepository;
        this.eventPublisher = eventPublisher;
        this.paymentFacade = paymentFacade;


        IBookingValidationHandler clientHandler = new ClientExistenceHandler(userRepository);
        IBookingValidationHandler masterHandler = new MasterExistenceHandler(masterRepository);
        IBookingValidationHandler serviceHandler = new ServiceExistenceHandler(serviceRepository);
        clientHandler.setNext(masterHandler);
        masterHandler.setNext(serviceHandler);
        this.validationChain = clientHandler;
    }

    public Booking createBooking(UUID clientId, UUID serviceId, UUID masterId, LocalDateTime desiredDateTime) {
        BookingValidationContext context = new BookingValidationContext(
                clientId, masterId, serviceId, desiredDateTime);
        validationChain.handle(context);
        if (context.hasError()) {
            throw new RuntimeException(context.getErrorMessage());
        }

        
        System.out.println("\n--- [Composite Demo] ---");

        
        BookableItem service1 = context.getService();
        System.out.println("Клієнт бронює (Листок): " + service1.getName());
        System.out.println("Ціна: " + service1.getPrice());
        System.out.println("Тривалість: " + service1.getDurationMinutes() + " хв.");

        
        ServicePackage spaPackage = new ServicePackage("SPA-пакет 'Релакс'");
        spaPackage.addItem(service1);

        
        com.beautysalon.booking.entity.Service service2 =
             new com.beautysalon.booking.entity.Service("Миття голови", "", 150, 15);
        spaPackage.addItem(service2);

        System.out.println("\nКлієнт бронює (Пакет): " + spaPackage.getName());
        System.out.println("Ціна пакету (Composite): " + spaPackage.getPrice());
        System.out.println("Тривалість (Composite): " + spaPackage.getDurationMinutes() + " хв.");
        System.out.println("--- [Composite Demo End] ---\n");
       
        Booking newBooking = new Booking();
        newBooking.setClient(context.getClient());
        newBooking.setMaster(context.getMaster());
        newBooking.setService(context.getService());
        newBooking.setBookingDate(context.getDateTime().toLocalDate());
        newBooking.setBookingTime(context.getDateTime().toLocalTime());

        newBooking.setTotalPrice(spaPackage.getPrice());

        newBooking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(newBooking);
        eventPublisher.notifyObservers(savedBooking);
        return savedBooking;
    }

    public Booking confirmBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Бронювання не знайдено."));
        booking.confirm();
        Booking savedBooking = bookingRepository.save(booking);
        eventPublisher.notifyObservers(savedBooking);
        return savedBooking;
    }

    public Booking completeBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Бронювання не знайдено."));
        booking.complete();
        Booking savedBooking = bookingRepository.save(booking);
        eventPublisher.notifyObservers(savedBooking);
        return savedBooking;
    }

    public Booking cancelBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Бронювання не знайдено."));
        booking.cancel();
        Booking savedBooking = bookingRepository.save(booking);
        eventPublisher.notifyObservers(savedBooking);
        return savedBooking;
    }

    public void notifyPaymentObservers(Booking booking) {
        eventPublisher.notifyObservers(booking);
    }

    public List<Booking> getBookingsByClient(UUID clientId) {
        return bookingRepository.findByClientUserIdOrderByBookingDateDesc(clientId);
    }
}
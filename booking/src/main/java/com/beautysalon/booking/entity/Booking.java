package com.beautysalon.booking.entity;

import com.beautysalon.booking.state.*;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID bookingId;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne
    @JoinColumn(name = "master_id", nullable = false)
    private Master master;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    private LocalDate bookingDate;
    private LocalTime bookingTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;

    @Transient
    private BookingState state;

    private transient ApplicationContext context;

    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
        initState(); 
    }

    private double totalPrice;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    @PostLoad
    private void postLoad() {
        initState();
    }

    private void initState() {
        if (status == null) status = BookingStatus.PENDING;
        if (context == null) return; 

        this.state = switch (status) {
            case PENDING -> context.getBean(PendingState.class);
            case CONFIRMED -> context.getBean(ConfirmedState.class);
            case PAID -> context.getBean(PaidState.class);
            case COMPLETED -> context.getBean(CompletedState.class);
            case CANCELLED -> context.getBean(CancelledState.class);
        };
    }

    public void confirm() { if (state != null) state.confirm(this); }
    public void pay() { if (state != null) state.pay(this); }
    public void cancel() { if (state != null) state.cancel(this); }
    public void complete() { if (state != null) state.complete(this); }

    public Booking() {
        this.status = BookingStatus.PENDING;
    }

    public UUID getBookingId() { return bookingId; }
    public void setBookingId(UUID bookingId) { this.bookingId = bookingId; }

    public User getClient() { return client; }
    public void setClient(User client) { this.client = client; }

    public Master getMaster() { return master; }
    public void setMaster(Master master) { this.master = master; }

    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }

    public Schedule getSchedule() { return schedule; }
    public void setSchedule(Schedule schedule) { this.schedule = schedule; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public LocalTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalTime bookingTime) { this.bookingTime = bookingTime; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) {
        this.status = status;
        initState();
    }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    public void setState(BookingState state) { this.state = state; }
}
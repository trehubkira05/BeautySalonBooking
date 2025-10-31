package com.beautysalon.booking.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR) // <-- Фікс
    private UUID serviceId;

    private String name;
    private String description;
    private double price;
    private int durationMinutes;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private Master master; // Додано для коректного mappedBy у Master.java

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    // Конструктори
    public Service() {}
    public Service(String name, String description, double price, int durationMinutes) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationMinutes = durationMinutes;
    }

    // Геттери та Сеттери
    public UUID getServiceId() { return serviceId; }
    public void setServiceId(UUID serviceId) { this.serviceId = serviceId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public Master getMaster() { return master; }
    public void setMaster(Master master) { this.master = master; }
    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
}
package com.beautysalon.booking.entity;

import com.beautysalon.booking.composite.BookableItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "services")
public class Service implements BookableItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID serviceId;

    private String name;
    private String description;
    private double price;

    @Column(name = "duration_minutes")
    private int durationMinutes;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private Master master;

    @JsonIgnore
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public Service() {}

    public Service(String name, String description, double price, int durationMinutes) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationMinutes = durationMinutes;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public int getDurationMinutes() {
        return durationMinutes;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public void setServiceId(UUID serviceId) {
        this.serviceId = serviceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
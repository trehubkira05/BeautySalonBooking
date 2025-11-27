package com.beautysalon.booking.validation;

public interface IBookingValidationHandler {
    
    void setNext(IBookingValidationHandler next);

    void handle(BookingValidationContext context);
}
package com.beautysalon.booking.validation;

import com.beautysalon.booking.entity.Service;
import com.beautysalon.booking.repository.IServiceRepository;
import java.util.Optional;

public class ServiceExistenceHandler extends AbstractBookingValidationHandler {

    private final IServiceRepository serviceRepository;

    public ServiceExistenceHandler(IServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public void handle(BookingValidationContext context) {
        Optional<com.beautysalon.booking.entity.Service> service = serviceRepository.findById(context.getServiceId());

        if (service.isPresent()) {
            context.setService(service.get());
            handleNext(context);
        } else {
            context.setErrorMessage("Послугу не знайдено.");
        }
    }
}
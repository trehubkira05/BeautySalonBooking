package com.beautysalon.booking.validation;

import com.beautysalon.booking.entity.Master;
import com.beautysalon.booking.repository.IMasterRepository;
import java.util.Optional;

public class MasterExistenceHandler extends AbstractBookingValidationHandler {

    private final IMasterRepository masterRepository;

    public MasterExistenceHandler(IMasterRepository masterRepository) {
        this.masterRepository = masterRepository;
    }

    @Override
    public void handle(BookingValidationContext context) {
        Optional<Master> master = masterRepository.findById(context.getMasterId());

        if (master.isPresent()) {
            context.setMaster(master.get());
            handleNext(context);
        } else {
            context.setErrorMessage("Майстер не знайдений.");
        }
    }
}
package com.beautysalon.booking.repository;

import com.beautysalon.booking.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.time.LocalDate;
import java.util.List;

public interface IScheduleRepository extends JpaRepository<Schedule, UUID> {
    List<Schedule> findByMasterMasterIdAndWorkDate(UUID masterId, LocalDate date);
}
package com.beautysalon.booking.repository;

import com.beautysalon.booking.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;


public interface IScheduleRepository extends JpaRepository<Schedule, UUID> {
    List<Schedule> findByMasterMasterIdAndWorkDate(UUID masterId, LocalDate date);
    @Query("SELECT DISTINCT s.workDate FROM Schedule s WHERE s.master.masterId = :masterId")
    List<LocalDate> findDistinctWorkDatesByMasterId(UUID masterId);
    List<Schedule> findAllByMasterMasterId(UUID masterId);
    List<Schedule> findByMasterMasterIdAndWorkDateBetween(UUID masterId, LocalDate startDate, LocalDate endDate);
}
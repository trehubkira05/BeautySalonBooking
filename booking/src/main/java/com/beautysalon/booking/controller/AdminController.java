package com.beautysalon.booking.controller;

import com.beautysalon.booking.entity.Master;
import com.beautysalon.booking.entity.Schedule;
import com.beautysalon.booking.service.MasterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/management")
public class AdminController {

    private final MasterService masterService;

    public AdminController(MasterService masterService) {
        this.masterService = masterService;
    }

    @PostMapping("/masters")
    public ResponseEntity<Master> addMaster(@RequestBody Master newMaster) {
        Master savedMaster = masterService.addMaster(newMaster);
        return new ResponseEntity<>(savedMaster, HttpStatus.CREATED);
    }
    
    @PostMapping("/masters/{masterId}/schedule")
    public ResponseEntity<Schedule> setMasterSchedule(
            @PathVariable UUID masterId,
            @RequestParam String workDate,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        
        try {
            Schedule schedule = masterService.setMasterWorkSchedule(
                    masterId, 
                    LocalDate.parse(workDate), 
                    LocalTime.parse(startTime), 
                    LocalTime.parse(endTime));
            return new ResponseEntity<>(schedule, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
package com.beautysalon.booking.repository;

import com.beautysalon.booking.entity.Master;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface IMasterRepository extends JpaRepository<Master, UUID> {}
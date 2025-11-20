package com.beautysalon.booking.repository;

import com.beautysalon.booking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
import com.beautysalon.booking.entity.Role;
import java.util.List;

public interface IUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
}

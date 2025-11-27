package com.beautysalon.booking.service;

import com.beautysalon.booking.entity.Master;
import com.beautysalon.booking.entity.Role;
import com.beautysalon.booking.entity.User;
import com.beautysalon.booking.repository.IMasterRepository;
import com.beautysalon.booking.repository.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final IUserRepository userRepository;
    private final IMasterRepository masterRepository;
    private final PasswordEncoder passwordEncoder; 

    public UserService(IUserRepository userRepository, IMasterRepository masterRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.masterRepository = masterRepository;
        this.passwordEncoder = passwordEncoder; 
    }

    @Transactional
    public User save(User user) {
        if (user.getRole() == null) {
            user.setRole(Role.CLIENT);
        }
        
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        return userRepository.save(user);
    }

    public User login(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Transactional
    public User updateUserRole(UUID userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Користувача з ID " + userId + " не знайдено."));
        
        Role currentRole = user.getRole();

        if (currentRole == newRole) {
             return user; 
        }

        if (currentRole == Role.MASTER && newRole != Role.MASTER) {
            masterRepository.findByUserUserId(userId).ifPresent(masterRepository::delete);
        }
        
        if (newRole == Role.MASTER && currentRole != Role.MASTER) {
            if (masterRepository.findByUserUserId(userId).isEmpty()) {
                com.beautysalon.booking.entity.Master newMaster = 
                    new com.beautysalon.booking.entity.Master(user, "Призначити спеціалізацію", 0);
                masterRepository.save(newMaster);
            }
        }
        
        user.setRole(newRole);
        return userRepository.save(user);
    }
    
    public Optional<User> findById(String id) {
        try {
            return userRepository.findById(java.util.UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public void deleteById(String id) {
        try {
            userRepository.deleteById(java.util.UUID.fromString(id));
        } catch (IllegalArgumentException e) {
        }
    }
}
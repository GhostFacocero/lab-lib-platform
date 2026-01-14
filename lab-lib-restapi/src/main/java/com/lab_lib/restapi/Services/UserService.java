package com.lab_lib.restapi.Services;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.lab_lib.restapi.DTO.AppUser.*;
import com.lab_lib.restapi.Models.AppUser;
import com.lab_lib.restapi.Repositories.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    private final UserRepository userRepository;
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional //abbellitore che rollbacka automaticamente in caso di eccezioni
    public UUID registerUser(RegisterRequest newUser) {
        // Email format check
        if (!EMAIL_REGEX.matcher(newUser.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        // Basic password length check
        if (newUser.getPassword() == null || newUser.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        }

        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }

        if (userRepository.existsByNickname(newUser.getNickname())) {
            throw new IllegalArgumentException("Nickname already in use.");
        }

        AppUser user = new AppUser();
        user.setNickname(newUser.getNickname());
        user.setName(newUser.getName());
        user.setSurname(newUser.getSurname());
        user.setCf(newUser.getCf());
        user.setEmail(newUser.getEmail());
        user.setPassword(newUser.getPassword()); // hash later


        AppUser saved = userRepository.save(user);
        entityManager.refresh(saved); // forza un SELECT sul database per aggiornare i campi

        return saved.getToken();
    }

    @Transactional
    public UUID loginUser(LoginRequest User) {
        // Always check user input
        if(User.getPassword() == null || User.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        }  
        AppUser existingUser = userRepository.findByNickname(User.getNickname());
        if(existingUser == null || !existingUser.getPassword().equals(User.getPassword())) {
            throw new IllegalArgumentException("Invalid nickname or password.");
        } else {
            return existingUser.getToken();
        }
    }

    public Long getUserIdByToken(UUID token) {
        if(!userRepository.existsByToken(token))
            throw new IllegalArgumentException("Authentication failed: token does not exist");
        AppUser user = userRepository.findByToken(token);
        return user.getId();
    }

    public boolean existsByToken(UUID token) {
        return userRepository.existsByToken(token);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

}

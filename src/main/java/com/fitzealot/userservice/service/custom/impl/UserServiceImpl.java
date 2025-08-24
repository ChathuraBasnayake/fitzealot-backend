package com.fitzealot.userservice.service.custom.impl;

import com.fitzealot.userservice.model.dto.UserDTO;
import com.fitzealot.userservice.model.entity.User;
import com.fitzealot.userservice.repository.UserRepository;
import com.fitzealot.userservice.service.custom.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    @Transactional
    public void createUser(UserDTO userDTO) {
        String validationResult = validateUser(userDTO);
        if (validationResult != null) {
            throw new IllegalArgumentException(validationResult);
        }
        userDTO.setId(generateUserId());
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));


        try {
            User user = modelMapper.map(userDTO, User.class);
            userRepository.save(user);
            log.info("User created successfully with ID: {}", user.getId());
        } catch (DataIntegrityViolationException e) {
            log.error("Error creating user: {}", e.getMessage());
            throw new DataIntegrityViolationException("Error creating user", e);
        }
    }

    @Override
    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    @Transactional
    public void updateUser(String id, UserDTO userDTO) {
        String validationResult = validateUser(userDTO);
        if (validationResult != null) {
            throw new IllegalArgumentException(validationResult);
        }

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if (userDTO.getPassword() != null) {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        modelMapper.map(userDTO, existingUser);
        existingUser.setId(id);
        userRepository.save(existingUser);
        log.info("User updated successfully with ID: {}", id);
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("User not found with email: " + email);
        }
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public String validateUser(UserDTO userDTO) {
        String id = userDTO.getId();
        String username = userDTO.getUsername();
        String fullname = userDTO.getFullname();
        userDTO.getUsername();
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        String confirmPassword = userDTO.getConfirmPassword();
        Date dateOfBirth = userDTO.getDateOfBirth();
        String phoneNumber = userDTO.getPhoneNumber();

        if (email == null || email.isEmpty() || !email.matches("^[\\w-_.+]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            return "Invalid email format.";
        }

        if (id == null && password == null) {
            return "Password is required for new users.";
        }

        if (password != null) {
            if (password.isEmpty() || password.length() < 8) {
                return "Password must be at least 8 characters.";
            }
            if (!password.matches(".*[A-Z].*")) {
                return "Password must contain at least one uppercase letter.";
            }
            if (!password.matches(".*[a-z].*")) {
                return "Password must contain at least one lowercase letter.";
            }
            if (!password.matches(".*\\d.*")) {
                return "Password must contain at least one digit.";
            }
            if (!password.equals(confirmPassword)) {
                log.info(password+" " + confirmPassword);
                return "Passwords do not match.";
            }
        }

        if (fullname == null || fullname.isEmpty() || fullname.length() < 3) {
            return "Name must be at least 3 characters.";
        }

        if (username == null|| username.isEmpty() || username.length() < 3) {
            return "username must be at least 3 characters.";
        }

        if (dateOfBirth == null) {
            return "Date of Birth must be in the format YYYY-MM-DD.";
        }

        if (phoneNumber == null || !phoneNumber.matches("^\\d{10}$")) {
            return "Phone number must be exactly 10 digits.";
        }

        if (id == null) {
            User existingUser = userRepository.findByEmail(email);
            if (existingUser != null) {
                return "Email is already in use.";
            }
        }

        return null;
    }

    @Override
    public void authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }
        log.info("User authenticated successfully: {}", email);
    }

    @Override
    public void forgotPassword(String password, String confirmPassword, String newPassword) {

        if (password == null || confirmPassword == null || newPassword == null) {
            throw new IllegalArgumentException("Password fields cannot be null.");
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Current password and confirm password do not match.");
        }

        if (newPassword.isEmpty() || newPassword.length() < 8) {
            throw new IllegalArgumentException("New password must be at least 8 characters.");
        }

        if (!newPassword.matches(".*[A-Z].*") || !newPassword.matches(".*[a-z].*") || !newPassword.matches(".*\\d.*")) {
            throw new IllegalArgumentException("New password must contain at least one uppercase letter, one lowercase letter, and one digit.");
        }

        // Here you would typically update the user's password in the database
        // For this example, we will just log the action
        log.info("Password reset successfully for user with current password: {}", password);

    }

    private String generateUserId() {
        return UUID.randomUUID().toString();
    }

    public String verify(String username,String password) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

        if (authenticate.isAuthenticated())
            return jwtService.generateToken(username);

        return "User is not authenticated";

    }


}
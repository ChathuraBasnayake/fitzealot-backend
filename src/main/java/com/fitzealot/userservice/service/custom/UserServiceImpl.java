package com.fitzealot.userservice.service.custom;

import com.fitzealot.userservice.model.dto.UserDTO;
import com.fitzealot.userservice.model.entity.User;
import com.fitzealot.userservice.repository.UserRepository;
import com.fitzealot.userservice.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // Implement the methods defined in UserService interface
    // For example, you might have methods like createUser, getUserById, updateUser, deleteUser, etc.

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    public void createUser(UserDTO userDTO) {

        // Generate a unique ID for the user
        userDTO.setId(generateUserId());

        // Validate the userDTO before saving
        String validateUser = validateUser(userDTO);
        if (validateUser!=null) {
            throw new IllegalArgumentException(validateUser);
        }
        userRepository.save(modelMapper.map(userDTO, User.class));
    }

    @Override
    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id).orElse(null);
        return user != null ? modelMapper.map(user, UserDTO.class) : null;
    }

    @Override
    public void updateUser(String id, UserDTO userDTO) {
        String validateUser = validateUser(userDTO);
        if (validateUser!=null) {
            throw new IllegalArgumentException(validateUser);
        }

        userRepository.findById(id).ifPresentOrElse(user -> {
            modelMapper.map(userDTO, user);
            user.setId(id);

            userRepository.save(user);
        }, () -> {
            throw new EntityNotFoundException("User not found with id: " + id);
        });
    }


    @Override
    public void deleteUser(String id) {
        // Implementation for deleting a user
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
        return user != null ? modelMapper.map(user, UserDTO.class) : null;
    }

    @Override
    public String validateUser(UserDTO userDTO) {
        String id = userDTO.getId();
        String name = userDTO.getName();
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        String confirmPassword = userDTO.getConfirmPassword();
        String dateOfBirth = userDTO.getDateOfBirth();
        String address = userDTO.getAddress();
        String phoneNumber = userDTO.getPhoneNumber();

        if (email == null || email.isEmpty() || !email.matches("^[\\w-_.+]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            return "Invalid email format.";
        }

        if (password == null || password.isEmpty()) {
            return "Password cannot be empty.";
        }

        if (password.length() < 8) {
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
            return "Passwords do not match.";
        }

        if (name == null || name.isEmpty() || name.length() < 3) {
            return "Name must be at least 3 characters.";
        }

        if (dateOfBirth == null || !dateOfBirth.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return "Date of Birth must be in the format YYYY-MM-DD.";
        }

        if (address == null || address.isEmpty() || address.length() < 10) {
            return "Address must be at least 10 characters.";
        }

        if (phoneNumber == null || !phoneNumber.matches("^\\d{10}$")) {
            return "Phone number must be exactly 10 digits.";
        }

        if (id == null && userRepository.findByEmail(email) != null) {
            return "Email is already in use.";
        }

        return null;  // null means validation passed
    }


    //    id generation
    public String generateUserId() {
        long count = userRepository.count();
        return "USER" + (count + 1);
    }

}

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
        if (!validateUser(userDTO)) {
            throw new IllegalArgumentException("Invalid user data");
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
        if (!validateUser(userDTO)) {
            throw new IllegalArgumentException("Invalid user data");
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
    public boolean validateUser(UserDTO userDTO) {

        String id = userDTO.getId();
        String name = userDTO.getName();
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        String confirmPassword = userDTO.getConfirmPassword();
        String dateOfBirth = userDTO.getDateOfBirth();
        String address = userDTO.getAddress();
        String phoneNumber = userDTO.getPhoneNumber();

        if (email == null || email.isEmpty() || !email.matches("^[\\w-_.+]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            return false;
        }

        if (password == null || password.isEmpty() || password.length() < 8 ||
                !password.matches(".*[A-Z].*") ||         // At least one uppercase
                !password.matches(".*[a-z].*") ||         // At least one lowercase
                !password.matches(".*\\d.*") ||           // At least one digit
                !password.equals(confirmPassword)) {      // Passwords must match
            return false;
        }

        if (name == null || name.isEmpty() || name.length() < 3) {
            return false;
        }

        if (dateOfBirth == null || !dateOfBirth.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        }

        if (address == null || address.isEmpty() || address.length() < 10) {
            return false;
        }

        if (phoneNumber == null || !phoneNumber.matches("^\\d{10}$")) {
            return false;
        }

        if (id == null) {
            System.out.println("efwsf");
            return userRepository.findByEmail(email) == null;
        }

        return true;
    }

    //    id generation
    public String generateUserId() {
        long count = userRepository.count();
        return "USER" + (count + 1);
    }

}

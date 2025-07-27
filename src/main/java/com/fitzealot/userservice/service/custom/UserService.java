package com.fitzealot.userservice.service.custom;

import com.fitzealot.userservice.model.dto.UserDTO;

import java.util.List;

public interface UserService {
    void createUser(UserDTO userDTO);

    UserDTO getUserById(String id);

    void updateUser(String id, UserDTO userDTO);

    void deleteUser(String id);

    List<UserDTO> getAllUsers();

    UserDTO getUserByEmail(String email);

    String validateUser(UserDTO userDTO);

    void authenticateUser(String email, String password);

    void forgotPassword(String password, String confirmPassword, String newPassword);

    String verify(UserDTO users);
}

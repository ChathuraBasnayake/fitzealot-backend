package com.fitzealot.userservice.controller;

import com.fitzealot.userservice.model.dto.UserDTO;
import com.fitzealot.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @PostMapping("/CreateUser")
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/GetUser")
    public ResponseEntity<UserDTO> getUser(@RequestParam String id) {
        UserDTO user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/GetAllUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/UpdateUser")
    public ResponseEntity<Void> updateUser(@RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO.getId(), userDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/DeleteUser")
    void deleteUser(String id) {
        // Logic to delete user by ID
    }

    @GetMapping("/GetUserByEmail/{email}")
    UserDTO getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

}
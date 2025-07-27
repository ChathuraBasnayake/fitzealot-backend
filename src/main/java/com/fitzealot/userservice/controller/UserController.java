package com.fitzealot.userservice.controller;

import com.fitzealot.userservice.model.dto.UserDTO;
import com.fitzealot.userservice.service.custom.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        UserDTO user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/updateUser")
    public ResponseEntity<Void> updateUser(@RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO.getId(), userDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/authenticate")
    public ResponseEntity<Void> authenticateUser(
            @RequestParam String email,
            @RequestParam String password
    ) {
        userService.authenticateUser(email, password);
        return ResponseEntity.ok().build();
    }

    @PutMapping("forgotPassword/{password}/{newPassword}/{ConfirmNewPassword}")
    public ResponseEntity<Void> forgotPassword(@RequestParam String password, @RequestParam String confirmPassword,
                                               @RequestParam String newPassword) {


        userService.forgotPassword(password, confirmPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO users){

        return userService.verify(users);

    }

}
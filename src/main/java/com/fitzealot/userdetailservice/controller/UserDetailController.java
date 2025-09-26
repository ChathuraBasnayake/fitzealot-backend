package com.fitzealot.userdetailservice.controller;

import com.fitzealot.userdetailservice.model.dto.UserDetailsDTO;
import com.fitzealot.userdetailservice.service.UserDetailService;
import com.fitzealot.userdetailservice.service.impl.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("/user-details")
@RestController
@AllArgsConstructor
public class UserDetailController {

    private final UserDetailService userDetailService;

    @PostMapping("/create")
    ResponseEntity<Void> createUserDetails(@RequestBody UserDetailsDTO userDetailsDTO) {
        userDetailService.createUserDetails(userDetailsDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/{username}")
    ResponseEntity<UserDetailsDTO> getUserDetails(@PathVariable String username) {
        return ResponseEntity.ok(userDetailService.getUserDetails(username));
    }

}

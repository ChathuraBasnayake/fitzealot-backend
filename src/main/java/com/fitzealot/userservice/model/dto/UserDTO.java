package com.fitzealot.userservice.model.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String id;
    private String fullname;
    private String username;
    private String email;
    private String phoneNumber;
    private Date dateOfBirth;
    private String password;
    private String confirmPassword;

}

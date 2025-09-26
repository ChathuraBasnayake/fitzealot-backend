package com.fitzealot.userdetailservice.model.dto;

import lombok.Data;

@Data
public class UserDetailsDTO {

    private String username;
    private Double height;
    private Double weight;
    private Integer age;
    private String goal;


}
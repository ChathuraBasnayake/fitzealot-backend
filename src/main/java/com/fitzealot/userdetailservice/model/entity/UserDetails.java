package com.fitzealot.userdetailservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {
    @Id
    private String username;
    private Double height;
    private Double weight;
    private Integer age;
    private String goal;
}
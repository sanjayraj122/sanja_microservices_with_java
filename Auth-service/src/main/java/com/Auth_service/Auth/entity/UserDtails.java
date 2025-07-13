package com.Auth_service.Auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_details")
public class UserDtails {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long userId;
    @Column(name = "full_name", nullable = false)
    private String name;
    @Column(name = "username", unique = true, nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    private String roles;
}

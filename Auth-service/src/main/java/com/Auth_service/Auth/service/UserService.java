package com.Auth_service.Auth.service;

import com.Auth_service.Auth.entity.UserDtails;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    UserDtails saveUser(UserDtails userDetails);

    UserDtails getUserByEmail(String username);

    List<UserDtails> getAllUsers();

    void deleteUserById(Long id);

    UserDtails updateUser(UserDtails userDetails);

    boolean existsByUsername(String username);

}

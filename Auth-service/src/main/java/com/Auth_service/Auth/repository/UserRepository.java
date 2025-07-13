package com.Auth_service.Auth.repository;

import com.Auth_service.Auth.entity.UserDtails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDtails, Long> {

    Optional<UserDtails> findByEmail(String username);

}

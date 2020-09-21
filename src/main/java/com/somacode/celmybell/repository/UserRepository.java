package com.somacode.celmybell.repository;

import com.somacode.celmybell.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    public Optional<User> findByUsername(String username);
}

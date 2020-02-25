package com.example.notes.persist.repositories;

import com.example.notes.persist.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findOneByUsername(String username);
}

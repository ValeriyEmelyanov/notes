package com.example.notes.persist.repositories;

import com.example.notes.persist.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий пользователей (учетных записей / аккаунтов )
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findOneByUsername(String username);
}

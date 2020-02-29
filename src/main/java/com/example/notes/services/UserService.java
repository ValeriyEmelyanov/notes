package com.example.notes.services;

import com.example.notes.persist.entities.User;

import java.util.Optional;

/**
 * Интерфейс сервиса пользователей
 */
public interface UserService {
    Optional<String> getCurrentUsername();
    Optional<Integer> getCurrentUserId();
    Optional<User> getCurrentUser();
}

package com.example.notes.services;

import com.example.notes.persist.entities.User;
import com.example.notes.transfer.UserRegDto;

import java.util.Optional;

/**
 * Интерфейс сервиса пользователей
 */
public interface UserService {
    // Для определения текущего пользователя
    Optional<String> getCurrentUsername();
    Optional<Integer> getCurrentUserId();
    Optional<User> getCurrentUser();

    // CRUD-операции для управления списком пользователей
    // * Удаление фиизическое отсутствует - у пользователя снимается флажок активности
    Optional<User> findByUsername(String username);
    void create(UserRegDto userRegDto);
}

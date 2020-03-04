package com.example.notes.services;

import com.example.notes.persist.entities.User;
import com.example.notes.transfer.UserDto;
import com.example.notes.transfer.UserRegDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    Optional<UserDto> findByUsername(String username);
    UserDto getById(Integer id);
    void create(UserRegDto userRegDto);
    Page<UserDto> findAll(Pageable pageable);
    void update(UserDto userDto);
}

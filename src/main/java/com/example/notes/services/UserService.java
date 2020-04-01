package com.example.notes.services;

import com.example.notes.persist.entities.User;
import com.example.notes.transfer.UserDto;
import com.example.notes.transfer.UserRegDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Интерфейс сервиса пользователей.
 * Удаление физическое отсутствует - у пользователя снимается флажок активности.
 */
public interface UserService {
    Optional<String> getCurrentUsername();
    Optional<User> getCurrentUser();

    Optional<UserDto> findByUsername(String username);
    UserDto getById(Integer id);
    void create(UserRegDto userRegDto);
    Page<UserDto> findAll(Pageable pageable);
    void update(UserDto userDto);
    void disable(Integer id);
}

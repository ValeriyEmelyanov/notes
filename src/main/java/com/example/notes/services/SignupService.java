package com.example.notes.services;

import com.example.notes.transfer.UserDto;

/**
 * Интерфейс сервиса для регистрации пользователей
 */
public interface SignupService {

    void signup(UserDto userDto);
    boolean isFreeUsername(String username);

}

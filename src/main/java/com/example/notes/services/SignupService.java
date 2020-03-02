package com.example.notes.services;

import com.example.notes.transfer.UserRegDto;

/**
 * Интерфейс сервиса для регистрации пользователей
 */
public interface SignupService {

    void signup(UserRegDto userRegDto);
    boolean isFreeUsername(String username);

}

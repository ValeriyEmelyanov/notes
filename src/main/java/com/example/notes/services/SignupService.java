package com.example.notes.services;

import com.example.notes.transfer.UserRegDto;

/**
 * Интерфейс сервиса для регистрации пользователей
 */
public interface SignupService {
    /**
     * Регистрирует нового пользователя.
     *
     * @param userRegDto данные для регистрации пользователя
     */
    void signup(UserRegDto userRegDto);
}

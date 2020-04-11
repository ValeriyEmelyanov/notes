package com.example.notes.utils;

import com.example.notes.services.UserService;
import com.example.notes.transfer.UserRegDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Валидатор данных регистрируемого пользователя.
 * - Проверяет, что имени пользователя нет в базе данных.
 * - Проверяет совпадение паролей.
 */
@Component
public class SignupValidator implements Validator {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegDto userRegDto = (UserRegDto) target;

        if (userService.findByUsername(userRegDto.getUsername()).isPresent()) {
            errors.rejectValue("username", "", String.format("User %s already exists!", userRegDto.getUsername()));
        }

        if (!userRegDto.getPassword().equals(userRegDto.getMatchingPassword())) {
            errors.rejectValue("password", "", "Password not matching");
        }
    }
}

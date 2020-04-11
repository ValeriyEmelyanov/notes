package com.example.notes.utils;

import com.example.notes.services.UserService;
import com.example.notes.transfer.UserDto;
import com.example.notes.transfer.UserRegDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.Errors;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Модульный тест валидатора данных регистрируемого пользователя.
 */
@ExtendWith(SpringExtension.class)
class SignupValidatorTest {
    private static final String USERNAME = "user";
    private static final String PASSWORD = "12345";

    private UserRegDto userRegDto;

    @InjectMocks
    SignupValidator signupValidator;

    @Mock
    UserService userService;

    @BeforeEach
    void setUp() {
        userRegDto = new UserRegDto();
        userRegDto.setUsername(USERNAME);
        userRegDto.setPassword(PASSWORD);
        userRegDto.setMatchingPassword(PASSWORD);
    }

    @Test
    void validateUserNotFound() {
        when(userService.findByUsername(USERNAME)).thenReturn(Optional.empty());
        Errors errors = mock(Errors.class);
        signupValidator.validate(userRegDto, errors);
        verify(errors, never()).rejectValue(eq("username"), any(), any());
    }

    @Test
    void validateUserFound() {
        when(userService.findByUsername(USERNAME)).thenReturn(Optional.of(new UserDto()));
        Errors errors = mock(Errors.class);
        signupValidator.validate(userRegDto, errors);
        verify(errors).rejectValue(eq("username"), any(), any());
    }

    @Test
    void validatePasswordNotMatching() {
        when(userService.findByUsername(USERNAME)).thenReturn(Optional.empty());
        userRegDto.setMatchingPassword(PASSWORD + "some symbols");
        Errors errors = mock(Errors.class);
        signupValidator.validate(userRegDto, errors);
        verify(errors).rejectValue(eq("password"), any(), any());
    }
}
package com.example.notes.services;

import com.example.notes.persist.entities.User;
import com.example.notes.services.Impl.SignupServiceImpl;
import com.example.notes.transfer.UserRegDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doAnswer;

/**
 * Модульный тест сервиса SignupServiceImpl
 */
class SignupServiceImplTest {

    @InjectMocks
    private SignupServiceImpl signupService;

    @Mock
    protected UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void signup() {
        List<User> users = new ArrayList<>();

        UserRegDto userRegDto = new UserRegDto();
        userRegDto.setUsername("somebody");

        doAnswer((Answer<Void>) invocation -> {
            User user = new User();
            user.setUsername(userRegDto.getUsername());
            users.add(user);
            return null;
        }).when(userService).create(userRegDto);

        signupService.signup(userRegDto);

        assertEquals(1, users.size());
        assertNotNull(users.get(0));
        assertEquals(userRegDto.getUsername(), users.get(0).getUsername());
    }
}
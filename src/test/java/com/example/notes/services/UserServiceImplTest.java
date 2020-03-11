package com.example.notes.services;

import com.example.notes.persist.entities.Role;
import com.example.notes.persist.entities.User;
import com.example.notes.persist.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getCurrentUsername() {
        String username = "user";

        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn(username);

        Optional<String> usernameOptional = userService.getCurrentUsername();

        assertTrue(usernameOptional.isPresent());
        assertEquals(username, usernameOptional.get());
    }

    @Test
    void getCurrentUserId() {
        String username = "user";
        Integer id = 1;

        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn(username);

        User user = User.builder().id(id).build();

        when(userRepository.findOneByUsername(anyString())).thenReturn(Optional.ofNullable(user));

        Optional<Integer> idOptional = userService.getCurrentUserId();

        assertTrue(idOptional.isPresent());
        assertEquals(id, idOptional.get());
    }

    @Test
    void getCurrentUser() {
        String username = "user";
        Integer id = 1;

        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn(username);

        User user = User.builder().id(id).username(username).role(Role.USER).active(true).build();

        when(userRepository.findOneByUsername(anyString())).thenReturn(Optional.ofNullable(user));

        Optional<User> userOptional = userService.getCurrentUser();

        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());
    }

    @Test
    void findByUsername() {
    }

    @Test
    void getById() {
    }

    @Test
    void create() {
    }

    @Test
    void findAll() {
    }

    @Test
    void update() {
    }

    @Test
    void disable() {
    }
}
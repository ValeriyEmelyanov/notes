package com.example.notes.services;

import com.example.notes.persist.entities.Role;
import com.example.notes.persist.entities.User;
import com.example.notes.persist.repositories.UserRepository;
import com.example.notes.transfer.UserDto;
import com.example.notes.transfer.UserRegDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    private static final Integer USER_ID = 1;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "12345";

    private User user;

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        user = User.builder()
                .id(USER_ID)
                .username(USERNAME)
                .encryptedPassword(PASSWORD)
                .role(Role.USER)
                .active(true)
                .build();
    }

    @Test
    void getCurrentUsername() {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn(USERNAME);

        Optional<String> usernameOptional = userService.getCurrentUsername();

        assertTrue(usernameOptional.isPresent());
        assertEquals(USERNAME, usernameOptional.get());
    }

    @Test
    void getCurrentUserId() {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn(USERNAME);
        when(userRepository.findOneByUsername(anyString())).thenReturn(Optional.ofNullable(user));

        Optional<Integer> idOptional = userService.getCurrentUserId();

        assertTrue(idOptional.isPresent());
        assertEquals(USER_ID, idOptional.get());
    }

    @Test
    void getCurrentUser() {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn(USERNAME);
        when(userRepository.findOneByUsername(anyString())).thenReturn(Optional.ofNullable(user));

        Optional<User> userOptional = userService.getCurrentUser();

        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());
    }

    @Test
    void findByUsername() {
        when(userRepository.findOneByUsername(anyString())).thenReturn(Optional.ofNullable(user));

        Optional<UserDto> userDtoOptional = userService.findByUsername(USERNAME);

        assertTrue(userDtoOptional.isPresent());

        UserDto userDto = userDtoOptional.get();

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getRole(), userDto.getRole());
        assertEquals(user.isActive(), userDto.isActive());
    }

    @Test
    void getById() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(user));

        UserDto userDto = userService.getById(USER_ID);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getRole(), userDto.getRole());
        assertEquals(user.isActive(), userDto.isActive());
    }

    @Test
    void create() {
        List<User> users = new ArrayList<>();

        when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);
        when(userRepository.save(any(User.class)))
                .thenAnswer((Answer<Void>) invokation -> {
                    users.add(user);
                    return null;
                });

        UserRegDto userRegDto = new UserRegDto();
        userRegDto.setUsername(USERNAME);
        userRegDto.setPassword(PASSWORD);

        userService.create(userRegDto);

        assertEquals(1, users.size());
    }

    @Test
    void findAll() {
        List<User> users = new ArrayList<>();
        users.add(user);

        int pageSize = 10;
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        PageRequest pageRequest = PageRequest.of(0, pageSize, sort);

        when(userRepository.findAll(pageRequest)).thenReturn(new PageImpl<User>(users, pageRequest, users.size()));

        Page<UserDto> page = userService.findAll(pageRequest);

        assertEquals(users.size(), page.getContent().size());
    }

    @Test
    void update() {
        Role role = Role.ADMIN;
        boolean active = false;

        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class)))
                .thenAnswer((Answer<Void>) invocation -> {
                    user.setRole(role);
                    user.setActive(active);
                    return null;
                });

        UserDto userDto = UserDto.builder().id(USER_ID).role(role).active(active).build();

        userService.update(userDto);

        assertEquals(role, user.getRole());
        assertEquals(active, user.isActive());
    }

    @Test
    void disable() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class)))
                .thenAnswer((Answer<Void>) invocation -> {
                    user.setActive(false);
                    return null;
                });
        userService.disable(USER_ID);

        assertEquals(false, user.isActive());
    }
}
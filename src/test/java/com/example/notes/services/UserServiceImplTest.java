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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        // Подготовим данные для настройки mock.
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
        // Подготовим данные для настройки mock - контекст безопасности.
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        // Настраиваем mock.
        when(securityContext.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn(USERNAME);

        // Вызываем тестируемый метод.
        Optional<String> usernameOptional = userService.getCurrentUsername();

        // Проверяем результат работы.
        assertTrue(usernameOptional.isPresent());
        assertEquals(USERNAME, usernameOptional.get());
    }

    @Test
    void getCurrentUsernameFail() {
        // Подготовим данные для настройки mock - контекст безопасности.
        Authentication auth = mock(AnonymousAuthenticationToken.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        // Настраиваем mock.
        when(securityContext.getAuthentication()).thenReturn(auth);

        // Вызываем тестируемый метод.
        Optional<String> usernameOptional = userService.getCurrentUsername();

        // Проверяем результат работы.
        assertFalse(usernameOptional.isPresent());
    }

    @Test
    void getCurrentUserId() {
        // Подготовим данные для настройки mock - контекст безопасности.
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        // Настраиваем mock.
        when(securityContext.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn(USERNAME);
        when(userRepository.findOneByUsername(anyString())).thenReturn(Optional.ofNullable(user));

        // Вызываем тестируемый метод.
        Optional<Integer> idOptional = userService.getCurrentUserId();

        // Проверяем результат работы.
        assertTrue(idOptional.isPresent());
        assertEquals(USER_ID, idOptional.get());
    }

    @Test
    void getCurrentUserIdFail() {
        // Подготовим данные для настройки mock - контекст безопасности.
        Authentication auth = mock(AnonymousAuthenticationToken.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        // Настраиваем mock.
        when(securityContext.getAuthentication()).thenReturn(auth);

        // Вызываем тестируемый метод.
        Optional<Integer> idOptional = userService.getCurrentUserId();

        // Вроверяем результат работы.
        assertFalse(idOptional.isPresent());
    }

    @Test
    void getCurrentUser() {
        // Подготовим данные для настройки mock - контекст безопасности.
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        // Настраиваем mock.
        when(securityContext.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn(USERNAME);
        when(userRepository.findOneByUsername(anyString())).thenReturn(Optional.ofNullable(user));

        // Вызываем тестируемый метод.
        Optional<User> userOptional = userService.getCurrentUser();

        // Проверяем результат работы.
        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());
    }

    @Test
    void getCurrentUserFail() {
        // Подготовим данные для настройки mock - контекст безопасности.
        Authentication auth = mock(AnonymousAuthenticationToken.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        // Настраиваем mock.
        when(securityContext.getAuthentication()).thenReturn(auth);

        // Вызываем тестируемый метод.
        Optional<User> userOptional = userService.getCurrentUser();

        // Проверяем результат работы.
        assertFalse(userOptional.isPresent());
    }

    @Test
    void findByUsername() {
        // Настраиваем mock.
        when(userRepository.findOneByUsername(anyString())).thenReturn(Optional.ofNullable(user));

        // Вызываем тестируемый метод.
        Optional<UserDto> userDtoOptional = userService.findByUsername(USERNAME);

        // Проверяем результат работы.
        assertTrue(userDtoOptional.isPresent());
        UserDto userDto = userDtoOptional.get();
        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getRole(), userDto.getRole());
        assertEquals(user.isActive(), userDto.isActive());
    }

    @Test
    void findByUsernameFail() {
        // Настраиваем mock.
        when(userRepository.findOneByUsername(anyString())).thenReturn(Optional.empty());

        // Вызываем тестируемый метод.
        Optional<UserDto> userDtoOptional = userService.findByUsername("somename");

        // Проверяем результат работы.
        assertFalse(userDtoOptional.isPresent());
    }

    @Test
    void getById() {
        // Настраиваем mock.
        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(user));

        // Вызываем тестируемый метод.
        UserDto userDto = userService.getById(USER_ID);

        // Проверяем результат работы.
        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getRole(), userDto.getRole());
        assertEquals(user.isActive(), userDto.isActive());
    }

    @Test
    void getByIdFail() {
        // Настраиваем mock.
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Вызываем тестируемый метод и проверяем результат.
        assertThrows(IllegalArgumentException.class, () -> userService.getById(99));
    }

    @Test
    void create() {
        List<User> users = new ArrayList<>();

        // Настраиваем mock.
        when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);
        when(userRepository.save(any(User.class)))
                .thenAnswer((Answer<Void>) invokation -> {
                    users.add(user);
                    return null;
                });

        // Подготовим параметр для вызова тестируемого метода.
        UserRegDto userRegDto = new UserRegDto();
        userRegDto.setUsername(USERNAME);
        userRegDto.setPassword(PASSWORD);

        // Вызываем тестируемый метод.
        userService.create(userRegDto);

        // Проверяем результат работы.
        assertEquals(1, users.size());
    }

    @Test
    void findAll() {
        List<User> users = new ArrayList<>();
        users.add(user);

        int pageSize = 10;
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        PageRequest pageRequest = PageRequest.of(0, pageSize, sort);

        // Настраиваем mock.
        when(userRepository.findAll(pageRequest)).thenReturn(new PageImpl<User>(users, pageRequest, users.size()));

        // Вызываем тестируемый метод.
        Page<UserDto> page = userService.findAll(pageRequest);

        // Проверяем результат работы.
        assertEquals(users.size(), page.getContent().size());
    }

    @Test
    void update() {
        Role role = Role.ADMIN;
        boolean active = false;

        // Настраиваем mock.
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class)))
                .thenAnswer((Answer<Void>) invocation -> {
                    user.setRole(role);
                    user.setActive(active);
                    return null;
                });

        // Подготовим параметр для вызова тестируемого метода.
        UserDto userDto = UserDto.builder().id(USER_ID).role(role).active(active).build();

        // Вызываем тестируемый метод.
        userService.update(userDto);

        // Проверяем результат работы.
        assertEquals(role, user.getRole());
        assertEquals(active, user.isActive());
    }

    @Test
    void disable() {
        // Настраиваем mock.
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class)))
                .thenAnswer((Answer<Void>) invocation -> {
                    user.setActive(false);
                    return null;
                });

        // Вызываем тестируемый метод.
        userService.disable(USER_ID);

        // Проверяем результат работы.
        assertFalse(user.isActive());
    }
}
package com.example.notes.services;

import com.example.notes.persist.entities.Role;
import com.example.notes.persist.entities.User;
import com.example.notes.persist.repositories.UserRepository;
import com.example.notes.transfer.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Сервис для регистрации пользователей
 */
@Service
@Transactional
public class SignupServiceImpl implements SignupService {
    private static final Logger logger = LoggerFactory.getLogger(SignupServiceImpl.class);

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Регистрация полльзователя: шифруется пароль, в базе данных сохраняется новый пользователь.
     * @param userDto   Данные регистрируемого пользователя
     */
    @Override
    public void signup(UserDto userDto) {
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        User newUser = User.builder()
                .username(userDto.getUsername())
                .encryptedPassword(encryptedPassword)
                .role(Role.USER)
                .active(true)
                .build();
        userRepository.save(newUser);
        logger.info("New user is saved: {}", newUser.getUsername());
    }

    /**
     * Проверяется, что имя пользователя еще не используется
     * @param username  Имя пользователя
     * @return          Возвращаеи ИСТИНА, если в базе данных нет пользователя с указанным именем
     */
    @Override
    public boolean isFreeUsername(String username) {
        Optional<User> optionalUser = userRepository.findOneByUsername(username);
        return !optionalUser.isPresent();
    }
}

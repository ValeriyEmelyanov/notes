package com.example.notes.services;

import com.example.notes.transfer.UserRegDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Сервис для регистрации пользователей
 */
@Service
@Transactional
public class SignupServiceImpl implements SignupService {
    private static final Logger logger = LoggerFactory.getLogger(SignupServiceImpl.class);

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Регистрация полльзователя: шифруется пароль, в базе данных сохраняется новый пользователь.
     * @param userRegDto   Данные регистрируемого пользователя
     */
    @Override
    public void signup(UserRegDto userRegDto) {
        userService.create(userRegDto);
        logger.info("New user is saved: {}", userRegDto.getUsername());
    }

    /**
     * Проверяется, что имя пользователя еще не используется
     * @param username  Имя пользователя
     * @return          Возвращаеи ИСТИНА, если в базе данных нет пользователя с указанным именем
     */
    @Override
    public boolean isFreeUsername(String username) {
        return !userService.findByUsername(username).isPresent();
    }
}

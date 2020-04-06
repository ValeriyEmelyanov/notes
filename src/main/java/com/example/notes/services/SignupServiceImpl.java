package com.example.notes.services;

import com.example.notes.transfer.UserRegDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Сервис регистрации пользователей.
 */
@Service
@Transactional
public class SignupServiceImpl implements SignupService {
    /**
     * Логгер.
     */
    private static final Logger logger = LoggerFactory.getLogger(SignupServiceImpl.class);

    /**
     * Сервис работы с пользователями.
     */
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void signup(UserRegDto userRegDto) {
        userService.create(userRegDto);
        logger.info("New user is saved: {}", userRegDto.getUsername());
    }

    @Override
    public boolean isFreeUsername(String username) {
        return !userService.findByUsername(username).isPresent();
    }
}

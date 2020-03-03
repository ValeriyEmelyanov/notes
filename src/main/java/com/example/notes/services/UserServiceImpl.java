package com.example.notes.services;

import com.example.notes.persist.entities.Role;
import com.example.notes.persist.entities.User;
import com.example.notes.persist.repositories.UserRepository;
import com.example.notes.transfer.UserDto;
import com.example.notes.transfer.UserRegDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация сервиса работы с пользователями
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

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

    @Override
    public Optional<String> getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return Optional.of(auth.getName());
    }

    @Override
    public Optional<Integer> getCurrentUserId() {
        Optional<String> optionalUsername = getCurrentUsername();
        if (optionalUsername.isPresent()) {
            return userRepository.findOneByUsername(optionalUsername.get()).map(User::getId);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getCurrentUser() {
        Optional<String> optionalUsername = getCurrentUsername();
        if (optionalUsername.isPresent()) {
            return userRepository.findOneByUsername(optionalUsername.get());
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> findByUsername(String username) {
        Optional<User> optionalUser = userRepository.findOneByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return Optional.of(UserDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .role(user.getRole())
                    .active(user.isActive())
                    .build());
        }
        return Optional.empty();
    }

    @Override
    public void create(UserRegDto userRegDto) {
        String encryptedPassword = passwordEncoder.encode(userRegDto.getPassword());
        User newUser = User.builder()
                .username(userRegDto.getUsername())
                .encryptedPassword(encryptedPassword)
                .role(Role.USER)
                .active(true)
                .build();
        userRepository.save(newUser);
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        List<User> users = usersPage.getContent();

        List<UserDto> userDtos = users.stream()
                .map(u -> UserDto.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .role(u.getRole())
                        .active(u.isActive())
                        .build()).collect(Collectors.toList());

        return new PageImpl<UserDto>(userDtos, pageable, usersPage.getTotalElements());
    }

    @Override
    public void update(UserDto userDto) {

    }
}

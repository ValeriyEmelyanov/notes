package com.example.notes.services;

import com.example.notes.persist.entities.Role;
import com.example.notes.persist.entities.User;
import com.example.notes.persist.repositories.UserRepository;
import com.example.notes.transfer.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class SignupServiceImpl implements SignupService {

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
    public void signup(UserDto userDto) {
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        User newUser = User.builder()
                .username(userDto.getUsername())
                .encryptedPassword(encryptedPassword)
                .role(Role.USER)
                .active(true)
                .build();
        userRepository.save(newUser);
    }

}

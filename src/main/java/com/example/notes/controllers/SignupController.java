package com.example.notes.controllers;

import com.example.notes.services.SignupService;
import com.example.notes.transfer.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * Контроллер для регистрации пользователей
 */
@Controller
public class SignupController {

    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);

    private SignupService signupService;

    @Autowired
    public void setSignupService(SignupService signupService) {
        this.signupService = signupService;
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new UserDto());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("user") @Valid UserDto userDto, BindingResult result) {
        logger.info("Request to register a new user: {}", userDto.getUsername());

        if (result.hasErrors()) {
            return "signup";
        }

        if (!signupService.isFreeUsername(userDto.getUsername())) {
            result.rejectValue("username", "", String.format("User %s already exists!", userDto.getUsername()));
            return "signup";
        }

        if (!userDto.getPassword().equals(userDto.getMatchingPassword())) {
            result.rejectValue("password", "", "Password not matching");
            return "signup";
        }

        signupService.signup(userDto);

        return "redirect:/login";
    }
}

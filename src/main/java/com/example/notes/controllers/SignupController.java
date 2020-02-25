package com.example.notes.controllers;

import com.example.notes.services.SignupService;
import com.example.notes.transfer.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    private SignupService signupService;

    @Autowired
    public void setSignupService(SignupService signupService) {
        this.signupService = signupService;
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(UserDto userDto) {
        if (!userDto.getPassword().equals(userDto.getMatchingPassword())) {
            return "signup";
        }

        signupService.signup(userDto);

        return "redirect:/login";
    }
}

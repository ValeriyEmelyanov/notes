package com.example.notes.controllers;

import com.example.notes.services.SignupService;
import com.example.notes.transfer.UserRegDto;
import com.example.notes.utils.SignupValidator;
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
 * Контроллер для регистрации пользователей.
 */
@Controller
public class SignupController {

    /**
     * Логгер
     */
    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);

    /**
     * Сервис для регистрации пользователей.
     */
    private SignupService signupService;

    /**
     * Валидатор данных регистрируемого пользователя.
     */
    private SignupValidator signupValidator;

    @Autowired
    public void setSignupService(SignupService signupService) {
        this.signupService = signupService;
    }

    @Autowired
    public void setSignupValidator(SignupValidator signupValidator) {
        this.signupValidator = signupValidator;
    }

    /**
     * Маппинг запроса страницы на регистрацию.
     *
     * @param model модель
     * @return имя страницы
     */
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new UserRegDto());
        return "signup";
    }

    /**
     * Маппинг запроса на регистрацию пользователя.
     *
     * @param userRegDto регистрационные данные пользователя
     * @param result
     * @return
     */
    @PostMapping("/signup")
    public String signup(@ModelAttribute("user") @Valid UserRegDto userRegDto, BindingResult result) {
        logger.info("Request to register a new user: {}", userRegDto.getUsername());

        signupValidator.validate(userRegDto, result);
        if (result.hasErrors()) {
            return "signup";
        }

        signupService.signup(userRegDto);

        return "redirect:/login";
    }
}

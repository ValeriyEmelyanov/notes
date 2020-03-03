package com.example.notes.controllers;

import com.example.notes.services.UserService;
import com.example.notes.transfer.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Контроллер аккаунтов пользователей
 */
@Controller
public class UserController {

    private final static int PAGE_SIZE = 10;
    private final static String SORT_FIELD = "username";

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String list(@PageableDefault(size = PAGE_SIZE) Pageable pageable,
                       Model model) {
        Sort sort = new Sort(Sort.Direction.ASC, SORT_FIELD);
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<UserDto> page = userService.findAll(pageRequest);
        model.addAttribute("page", page);

        return "users";
    }

    @GetMapping("/users/edit/{id}")
    public String edit() {
        return "operations/usersedit";
    }

    @GetMapping("users/disable/{id}")
    public String disable() {
        return "redirect:/users";
    }
}

package com.example.notes.controllers;

import com.example.notes.persist.entities.Role;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


/**
 * Контроллер аккаунтов пользователей.
 */
@Controller
public class UserController {

    /**
     * Константы для настройки параметров страницы.
     */
    private final static int PAGE_SIZE = 10;
    private final static String SORT_FIELD = "username";

    /**
     * Сервис для работы с пользователями.
     */
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Маппинг запроса списка пользователей.
     *
     * @param pageable параметры страницы
     * @param model модель
     * @return имя страницы
     */
    @GetMapping("/users")
    public String list(@PageableDefault(size = PAGE_SIZE) Pageable pageable,
                       Model model) {
        Sort sort = new Sort(Sort.Direction.ASC, SORT_FIELD);
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<UserDto> page = userService.findAll(pageRequest);
        model.addAttribute("page", page);

        return "users";
    }

    /**
     * Маппинг запроса сраницы редактироания пользователя.
     *
     * @param id идентификатор пользователя
     * @param model модель
     * @return имя страницы
     */
    @GetMapping("/users/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        UserDto userDto = userService.getById(id);
        model.addAttribute("user", userDto);
        model.addAttribute("roles", Role.values());
        return "operations/usersedit";
    }

    /**
     * Маппинг запроса на сохранение измененных данных пользователя.
     *
     * @param userDto данные пользователя
     * @return перенаправляет на страницу списка пользователей
     */
    @PutMapping("/users/update")
    public String update(@ModelAttribute("user") UserDto userDto) {
        userService.update(userDto);
        return "redirect:/users";
    }

    /**
     * Маппинг запроса на удаление пользователя.
     *
     * @param id идентификатор пользователя.
     * @return перенаправляет на страницу списка пользователей
     */
    @PatchMapping("/users/disable/{id}")
    public String disable(@PathVariable Integer id) {
        userService.disable(id);
        return "redirect:/users";
    }
}

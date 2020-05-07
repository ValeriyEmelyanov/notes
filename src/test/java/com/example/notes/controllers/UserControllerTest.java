package com.example.notes.controllers;

import com.example.notes.persist.entities.Role;
import com.example.notes.services.UserService;
import com.example.notes.transfer.UserDto;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Модульный тест контрооллера UserController
 */
class UserControllerTest {
    private final static String SORT_FIELD = "username";

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void list() throws Exception {
        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(new UserDto());
        userDtos.add(new UserDto());

        int pageSize = 10;
        Sort sort = Sort.by(Sort.Direction.ASC, SORT_FIELD);
        PageRequest pageRequest = PageRequest.of(0, pageSize, sort);

        when(userService.findAll(pageRequest))
                .thenReturn(new PageImpl<UserDto>(userDtos, pageRequest, userDtos.size()));

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attribute("page", instanceOf(Page.class)))
                .andExpect(model().attribute("page",
                        hasProperty("content", is(CoreMatchers.instanceOf(List.class)))));

        verify(userService).findAll(any(Pageable.class));
    }

    @Test
    void edit() throws Exception {
        when(userService.getById(anyInt())).thenReturn(new UserDto());

        mockMvc.perform(get("/users/edit/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("operations/usersedit"))
                .andExpect(model().attribute("user", instanceOf(UserDto.class)))
                .andExpect(model().attribute("roles", Role.values()));

        verify(userService).getById(anyInt());
    }

    @Test
    void update() throws Exception {
        doAnswer((Answer<Void>) invocation -> null)
                .when(userService).update(any(UserDto.class));

        mockMvc.perform(post("/users/update")
                .param("id", "1")
                .param("username", "username")
                .param("role", "USER")
                .param("active", "true"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users"));

        verify(userService).update(any(UserDto.class));
    }

    @Test
    void disable() throws Exception {
        doAnswer((Answer<Void>) invocation -> null)
                .when(userService).disable(anyInt());

        mockMvc.perform(patch("/users/disable/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users"));

        verify(userService).disable(anyInt());
    }
}
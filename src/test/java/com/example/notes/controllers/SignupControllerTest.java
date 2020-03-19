package com.example.notes.controllers;

import com.example.notes.services.SignupServiceImpl;
import com.example.notes.transfer.UserRegDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class SignupControllerTest {

    @InjectMocks
    SignupController signupController;

    @Mock
    SignupServiceImpl signupService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");

        mockMvc = MockMvcBuilders.standaloneSetup(signupController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void signup() throws Exception {
        // вызываем тестируемый метод, проверяем результат работы
        mockMvc.perform(get("/signup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attribute("user", instanceOf(UserRegDto.class)));
    }

    @Test
    void signupPost() throws Exception {
        // настраиваем mock для вызова методов сервисного слоя из тестируемого метода
        when(signupService.isFreeUsername(anyString())).thenReturn(true);
        doAnswer((Answer<Void>) invocation -> null).when(signupService).signup(any(UserRegDto.class));

        // вызываем тестируемый метод, проверяем результат работы
        mockMvc.perform(post("/signup")
                .param("username", "newer")
                .param("password", "12345")
                .param("matchingPassword", "12345"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    void signupPostHasError() throws Exception {
        // настраиваем mock для вызова методов сервисного слоя из тестируемого метода
        when(signupService.isFreeUsername(anyString())).thenReturn(true);
        doAnswer((Answer<Void>) invocation -> null).when(signupService).signup(any(UserRegDto.class));

        // вызываем тестируемый метод, проверяем результат работы
        mockMvc.perform(post("/signup")
                .param("username", "newer")
                .param("password", "12345")
                .param("matchingPassword", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().hasErrors());
    }

    @Test
    void signupPostNotFree() throws Exception {
        // настраиваем mock для вызова методов сервисного слоя из тестируемого метода
        when(signupService.isFreeUsername(anyString())).thenReturn(false);
        doAnswer((Answer<Void>) invocation -> null).when(signupService).signup(any(UserRegDto.class));

        // вызываем тестируемый метод, проверяем результат работы
        mockMvc.perform(post("/signup")
                .param("username", "newer")
                .param("password", "12345")
                .param("matchingPassword", "12345"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().hasErrors());
    }

    @Test
    void signupPostNotMatching() throws Exception {
        // настраиваем mock для вызова методов сервисного слоя из тестируемого метода
        when(signupService.isFreeUsername(anyString())).thenReturn(true);
        doAnswer((Answer<Void>) invocation -> null).when(signupService).signup(any(UserRegDto.class));

        // вызываем тестируемый метод, проверяем результат работы
        mockMvc.perform(post("/signup")
                .param("username", "newer")
                .param("password", "12345")
                .param("matchingPassword", "54321"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().hasErrors());
    }
}
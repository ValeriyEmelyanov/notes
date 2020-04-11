package com.example.notes.controllers;

import com.example.notes.services.SignupService;
import com.example.notes.transfer.UserRegDto;
import com.example.notes.utils.SignupValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Модульный тест контрооллера SignupController
 */
class SignupControllerTest {

    @InjectMocks
    SignupController signupController;

    @Mock
    SignupService signupService;

    @Mock
    private SignupValidator signupValidator;

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
        mockMvc.perform(get("/signup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attribute("user", instanceOf(UserRegDto.class)));
    }

    @Test
    void signupPost() throws Exception {
        doAnswer((Answer<Void>) invocation -> null).when(signupService).signup(any(UserRegDto.class));

        mockMvc.perform(post("/signup")
                .param("username", "newer")
                .param("password", "12345")
                .param("matchingPassword", "12345"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));

        verify(signupService).signup(any(UserRegDto.class));
    }

    @Test
    void signupPostHasError() throws Exception {
        doAnswer((Answer<Void>) invocation -> null).when(signupService).signup(any(UserRegDto.class));

        BindingResult result = new BeanPropertyBindingResult(new UserRegDto(), "user");
        result.rejectValue("password", "", "Password not matching");

        mockMvc.perform(post("/signup")
                .param("username", "newer")
                .param("password", "12345")
                .param("matchingPassword", "")
                .flashAttr("result", result))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().hasErrors());

        verify(signupService, never()).signup(any(UserRegDto.class));
    }
}
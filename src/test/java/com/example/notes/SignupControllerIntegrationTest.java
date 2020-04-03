package com.example.notes;

import com.example.notes.services.UserService;
import com.example.notes.transfer.UserDto;
import com.example.notes.transfer.UserRegDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Интеграционный тест для контроллера SignupController
 */
@SpringBootTest
@AutoConfigureMockMvc
class SignupControllerIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void signup() throws Exception {
        mockMvc.perform(get("/signup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attribute("user", instanceOf(UserRegDto.class)));
    }

    @Test
    void signupPost() throws Exception {
        String username = "newuser";
        String password = "12345";

        mockMvc.perform(post("/signup")
                .param("username", username)
                .param("password", password)
                .param("matchingPassword", password))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        Optional<UserDto> optionalUserDto = userService.findByUsername(username);
        if (!optionalUserDto.isPresent())
            throw new Exception("The user is not added!");
        UserDto userDto = optionalUserDto.get();
        assertEquals(username, userDto.getUsername());
    }

    @Test
    void signupPostError() throws Exception {
        String username = "newuser";
        String password = "12345";

        mockMvc.perform(post("/signup")
                .param("password", password)
                .param("matchingPassword", password))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().hasErrors());

        mockMvc.perform(post("/signup")
                .param("password", password))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().hasErrors());
    }

    @Test
    @Sql({"data_users.sql"})
    void signupPostNotFreeUserName() throws Exception {
        String username = "user1";
        String password = "12345";

        mockMvc.perform(post("/signup")
                .param("username", username)
                .param("password", password)
                .param("matchingPassword", password))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().hasErrors());
    }

    @Test
    void signupPostNotMatchingPassword() throws Exception {
        String username = "user1";
        String password = "12345";
        String matchingPassword = "54321";

        mockMvc.perform(post("/signup")
                .param("username", username)
                .param("password", password)
                .param("matchingPassword", matchingPassword))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().hasErrors());
    }
}
package com.example.notes;

import com.example.notes.persist.entities.Role;
import com.example.notes.services.UserService;
import com.example.notes.transfer.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Интеграционный тест для контроллера UserController
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @Sql({"data_users.sql"})
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void list() throws Exception {
        int usersCount = 3;

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attribute("page", instanceOf(Page.class)))
                .andExpect(model().attribute("page", hasProperty("content", instanceOf(List.class))))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(usersCount))));
    }

    @Test
    @Sql({"data_users.sql"})
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void edit() throws Exception {
        Integer id = 2;
        String username = "user1";
        Role role = Role.USER;
        boolean active = true;

        mockMvc.perform(get("/users/edit/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("operations/usersedit"))
                .andExpect(model().attribute("user", instanceOf(UserDto.class)))
                .andExpect(model().attribute("user", hasProperty("id", is(id))))
                .andExpect(model().attribute("user", hasProperty("username", is(username))))
                .andExpect(model().attribute("user", hasProperty("role", is(role))))
                .andExpect(model().attribute("user", hasProperty("active", is(active))));
    }

    @Test
    @Sql({"data_users.sql"})
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void editNoUser() throws Exception {
        Throwable thrown = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(get("/users/edit/901")));

        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("No user with such id!"));
        assertNotNull(thrown.getCause());
        assertEquals(thrown.getCause().getClass(), IllegalArgumentException.class);
    }

    @Test
    @Sql({"data_users.sql"})
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void update() throws Exception {
        Integer id = 3;
        String username = "user2";
        Role role = Role.ADMIN;
        boolean active = true;

        mockMvc.perform(post("/users/update")
                .param("id", String.valueOf(id))
                .param("role", role.name())
                .param("active", String.valueOf(active)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        UserDto userDto = userService.getById(id);
        assertEquals(id, userDto.getId());
        assertEquals(username, userDto.getUsername());
        assertEquals(role, userDto.getRole());
        assertEquals(active, userDto.isActive());
    }

    @Test
    @Sql({"data_users.sql"})
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void updateNoUser() throws Exception {
        Integer id = 901;
        String username = "user2";
        Role role = Role.ADMIN;
        boolean active = true;

        Throwable thrown = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(post("/users/update")
                        .param("id", String.valueOf(id))
                        .param("role", role.name())
                        .param("active", String.valueOf(active))));

        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Request processing failed"));
    }

    @Test
    @Sql({"data_users.sql"})
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void disable() throws Exception {
        Integer id = 2;
        boolean active = false;

        mockMvc.perform(get("/users/disable/2"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        UserDto userDto = userService.getById(id);
        assertEquals(id, userDto.getId());
        assertEquals(active, userDto.isActive());
    }

    @Test
    @Sql({"data_users.sql"})
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void disableNoUser() throws Exception {
        Throwable thrown = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(get("/users/disable/901")));

        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Request processing failed"));
    }

}
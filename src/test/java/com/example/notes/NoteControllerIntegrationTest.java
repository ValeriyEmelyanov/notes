package com.example.notes;

import com.example.notes.persist.entities.Note;
import com.example.notes.persist.entities.Role;
import com.example.notes.persist.entities.User;
import com.example.notes.services.NoteService;
import com.example.notes.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Интеграционный тест для контроллера NoteController
 */
@SpringBootTest
@AutoConfigureMockMvc
class NoteControllerIntegrationTest {

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql({"data_users.sql", "data_notes.sql"})
    @WithMockUser(username = "admin")
    void list() throws Exception {
        int pageListSize = 10;

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("page", instanceOf(Page.class)))
                .andExpect(model().attribute("page", hasProperty("content", instanceOf(List.class))))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(pageListSize))));
    }

    @Test
    @Sql({"data_users.sql", "data_notes.sql"})
    @WithMockUser(username = "user1")
    void listWithUser() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @Sql({"data_users.sql", "data_notes.sql"})
    @WithMockUser(username = "admin")
    void listFiltered() throws Exception {
        int pageListSize = 2;

        mockMvc.perform(get("/")
                .param("searchText", "ext"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("page", instanceOf(Page.class)))
                .andExpect(model().attribute("page", hasProperty("content", instanceOf(List.class))))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(pageListSize))));

        pageListSize = 3;

        mockMvc.perform(get("/")
                .param("done", "DONE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("page", instanceOf(Page.class)))
                .andExpect(model().attribute("page", hasProperty("content", instanceOf(List.class))))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(pageListSize))));

        pageListSize = 1;

        mockMvc.perform(get("/")
                .param("searchText", "ext")
                .param("done", "DONE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("page", instanceOf(Page.class)))
                .andExpect(model().attribute("page", hasProperty("content", instanceOf(List.class))))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(pageListSize))));
    }

    @Test
    @Sql({"data_users.sql", "data_notes.sql"})
    @WithMockUser(username = "admin")
    void sortChoose() throws Exception {
        mockMvc.perform(get("/sort/ASC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("sortDateOrder", is("ASC")));
    }

    @Test
    @Sql({"data_users.sql", "data_notes.sql"})
    @WithMockUser(username = "user1")
    void sortChooseDesc() throws Exception {
        mockMvc.perform(get("/sort/DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("sortDateOrder", is("DESC")));
    }

    @Test
    @Sql({"data_users.sql", "data_notes.sql"})
    @WithMockUser(username = "admin")
    void page() throws Exception {
        mockMvc.perform(get("/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @Sql({"data_users.sql"})
    @WithMockUser(username = "admin")
    void newNote() throws Exception {
        mockMvc.perform(get("/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("operations/new"));
    }

    @Test
    @Sql({"data_users.sql"})
    @WithMockUser(username = "admin")
    void save() throws Exception {
        String message = "New message";
        int sizeAfter = 1;

        mockMvc.perform(post("/save")
                .param("message", message))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        Page<Note> page = noteService.findByUser(PageRequest.of(0, 10, Sort.unsorted()), user);

        List<Note> notes = page.getContent();
        if (notes.isEmpty()) throw new Exception("Note not saved!");
        Note note = notes.get(0);

        assertEquals(sizeAfter, notes.size());
        assertEquals(message, note.getMessage());
        assertEquals(userId, note.getUser().getId());
    }

    @Test
    @Sql({"data_users.sql", "data_notes.sql"})
    @WithMockUser(username = "admin")
    void edit() throws Exception {
        Integer id = 1;
        String message = "Note 1 ext";
        boolean done = false;

        mockMvc.perform(get("/edit/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("operations/edit"))
                .andExpect(model().attribute("note", instanceOf(Note.class)))
                .andExpect(model().attribute("note", hasProperty("id", is(id))))
                .andExpect(model().attribute("note", hasProperty("message", is(message))))
                .andExpect(model().attribute("note", hasProperty("done", is(done))));
    }

    @Test
    @Sql({"data_users.sql", "data_notes.sql"})
    @WithMockUser(username = "admin")
    void update() throws Exception {
        Integer id = 1;
        String message = "Note 1 updated";
        boolean done = true;

        mockMvc.perform(post("/update")
                .param("id", String.valueOf(id))
                .param("message", message)
                .param("done", String.valueOf(done)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        User user = new User(1, "admin", "12345", Role.ADMIN, true);
        Note note = noteService.getById(id, user);

        assertEquals(id, note.getId());
        assertEquals(message, note.getMessage());
        assertEquals(done, note.isDone());
        assertEquals(user.getUsername(), note.getUser().getUsername());
    }

    @Test
    @Sql({"data_users.sql", "data_notes.sql"})
    @WithMockUser(username = "admin")
    void delete() throws Exception {
        mockMvc.perform(get("/delete/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        User user = new User(1, "admin", "12345", Role.ADMIN, true);
        Integer id = 1;
        assertThrows(EntityNotFoundException.class, () -> noteService.getById(id, user));
    }
}
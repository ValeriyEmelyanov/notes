package com.example.notes;

import com.example.notes.persist.entities.Note;
import com.example.notes.persist.entities.Role;
import com.example.notes.persist.entities.User;
import com.example.notes.services.NoteService;
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
    private MockMvc mockMvc;

    @Test
    @Sql({"data_users.sql", "data_notes.sql"})
    @WithMockUser(username = "admin")
    void list() throws Exception {
        // Контрольные данные (см. data_notes.sql)
        int pageListSize = 10;

        // Делаем вызов и проверяем результат.
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
        // Контрольные размер списка (см. data_notes.sql)
        int pageListSize = 2;

        // Проверяем фильтр по контекстной строке.
        mockMvc.perform(get("/")
                .param("searchText", "ext"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("page", instanceOf(Page.class)))
                .andExpect(model().attribute("page", hasProperty("content", instanceOf(List.class))))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(pageListSize))));

        // Контрольные размер списка (см. data_notes.sql)
        pageListSize = 3;

        // Проверяем фильтр по отметке сделано.
        mockMvc.perform(get("/")
                .param("done", "DONE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("page", instanceOf(Page.class)))
                .andExpect(model().attribute("page", hasProperty("content", instanceOf(List.class))))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(pageListSize))));

        // Контрольные размер списка (см. data_notes.sql)
        pageListSize = 1;

        // Проверяем фильтр по контекстной строке и отметке сделано.
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
        // Контрольные данные
        String message = "New message";
        int sizeAfter = 1;

        // Делаем вызов и проверяем результат.
        mockMvc.perform(post("/save")
                .param("message", message))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // Извлечем сохраненную заметку и проверим результат
        Integer userId = 1;
        Page<Note> page = noteService.findByUserId(PageRequest.of(0, 10, Sort.unsorted()), userId);
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
        // Контрольные данные (см. data_notes.sql)
        Integer id = 1;
        String message = "Note 1 ext";
        boolean done = false;

        // Делаем вызов и проверяем результат.
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
        // Контрольные данные
        Integer id = 1;
        String message = "Note 1 updated";
        boolean done = true;

        // Делаем вызов и проверяем результат.
        mockMvc.perform(post("/update")
                .param("id", String.valueOf(id))
                .param("message", message)
                .param("done", String.valueOf(done)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // Получим измененную заметку и проверим результат
        User user = User.builder()
                .id(1)
                .username("admin")
                .role(Role.ADMIN)
                .active(true)
                .build();
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
        // Делаем вызов и проверяем результат.
        mockMvc.perform(get("/delete/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // Пытаемся получить удаленную заметку
        User user = User.builder()
                .id(1)
                .username("admin")
                .role(Role.ADMIN)
                .active(true)
                .build();
        Integer id = 1;
        assertThrows(EntityNotFoundException.class, () -> noteService.getById(id, user));
    }
}
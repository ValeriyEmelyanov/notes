package com.example.notes.controllers;

import com.example.notes.persist.entities.Note;
import com.example.notes.persist.entities.User;
import com.example.notes.services.NoteServiceImpl;
import com.example.notes.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class NoteControllerTest {
    private final static String SORT_FIELD = "date";

    @InjectMocks
    NoteController noteController;

    @Mock
    NoteServiceImpl noteService;

    @Mock
    UserServiceImpl userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(noteController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void list() throws Exception {
        // Подготовим данные для настройки mock
        List<Note> notes = new ArrayList<>();
        notes.add(new Note());
        notes.add(new Note());
        int notesSize = notes.size();

        int pageSize = 10;
        Sort sort = new Sort(Sort.Direction.ASC, SORT_FIELD);
        PageRequest pageRequest = PageRequest.of(0, pageSize, sort);

        // настраиваем mock для вызова методов сервисного слоя из тестируемого метода
        when(userService.getCurrentUserId()).thenReturn(Optional.of(1));
        when(noteService.findByUserId(any(Pageable.class), anyInt()))
                .thenReturn(new PageImpl<Note>(notes, pageRequest, notes.size()));

        // вызываем тестируемый метод, проверяем результат работы
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("page", hasProperty("content", is(instanceOf(List.class)))))
                .andExpect(model().attribute("page", hasProperty("content", is(hasSize(notesSize)))));
    }

    @Test
    void sortChoose() {
    }

    @Test
    void page() {
    }

    @Test
    void newNote() {
    }

    @Test
    void save() {
    }

    @Test
    void edit() throws Exception {
        // настраиваем mock для вызова методов сервисного слоя из тестируемого метода
        when(userService.getCurrentUser()).thenReturn(Optional.of(new User()));
        when(noteService.getById(anyInt(), any(User.class))).thenReturn(new Note());

        // вызываем тестируемый метод, проверяем результат работы
        mockMvc.perform(get("/edit/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("operations/edit"))
                .andExpect(model().attribute("note", instanceOf(Note.class)));
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}
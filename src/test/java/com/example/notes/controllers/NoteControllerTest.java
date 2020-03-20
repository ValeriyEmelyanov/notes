package com.example.notes.controllers;

import com.example.notes.filtering.FilterAdjuster;
import com.example.notes.persist.entities.Note;
import com.example.notes.persist.entities.User;
import com.example.notes.services.NoteService;
import com.example.notes.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
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
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class NoteControllerTest {
    private final static String SORT_FIELD = "date";

    private List<Note> notes;
    private int notesSize;
    private PageRequest pageRequest;

    @InjectMocks
    NoteController noteController;

    @Mock
    NoteService noteService;

    @Mock
    UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(noteController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        // Подготовим данные для настройки mockMvc.
        notes = new ArrayList<>();
        notes.add(new Note());
        notes.add(new Note());
        notesSize = notes.size();

        int pageSize = 10;
        Sort sort = new Sort(Sort.Direction.ASC, SORT_FIELD);
        pageRequest = PageRequest.of(0, pageSize, sort);
    }

    @Test
    void list() throws Exception {
        // Настраиваем mockMvc для вызова методов сервисного слоя из тестируемого метода.
        when(userService.getCurrentUserId()).thenReturn(Optional.of(1));
        when(noteService.findByUserId(any(Pageable.class), anyInt()))
                .thenReturn(new PageImpl<Note>(notes, pageRequest, notes.size()));

        // Вызываем тестируемый метод, проверяем результат работы.
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("page", hasProperty("content", is(instanceOf(List.class)))))
                .andExpect(model().attribute("page", hasProperty("content", is(hasSize(notesSize)))));
        // Проверякм вызовы методов сервисного слоя.
        verify(noteService, never()).findByUserIdAndSearchParameters(
                any(Pageable.class), anyInt(), any(FilterAdjuster.class));
        verify(noteService).findByUserId(any(Pageable.class), anyInt());
    }

    @Test
    void listWithFilter() throws Exception {
        // Настраиваем mockMvc для вызова методов сервисного слоя из тестируемого метода.
        when(userService.getCurrentUserId()).thenReturn(Optional.of(1));
        when(noteService.findByUserIdAndSearchParameters(
                any(Pageable.class), anyInt(), any(FilterAdjuster.class)))
                .thenReturn(new PageImpl<Note>(notes, pageRequest, notes.size()));

        // Вызываем тестируемый метод, проверяем результат работы.
        mockMvc.perform(get("/")
                .param("searchText", "...")
                .param("done", "DONE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("page", hasProperty("content", is(instanceOf(List.class)))))
                .andExpect(model().attribute("page", hasProperty("content", is(hasSize(notesSize)))));
        // Проверякм вызовы методов сервисного слоя.
        verify(noteService).findByUserIdAndSearchParameters(
                any(Pageable.class), anyInt(), any(FilterAdjuster.class));
        verify(noteService, never()).findByUserId(any(Pageable.class), anyInt());
    }

    @Test
    void sortChoose() throws Exception {
        // Настраиваем mockMvc для вызова методов сервисного слоя из тестируемого метода.
        when(userService.getCurrentUserId()).thenReturn(Optional.of(1));
        when(noteService.findByUserId(any(Pageable.class), anyInt()))
                .thenReturn(new PageImpl<Note>(notes, pageRequest, notes.size()));

        // Вызываем тестируемый метод, проверяем результат работы.
        mockMvc.perform(get("/sort/ASC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void sortChooseDesc() throws Exception {
        // Настраиваем mockMvc для вызова методов сервисного слоя из тестируемого метода.
        when(userService.getCurrentUserId()).thenReturn(Optional.of(1));
        when(noteService.findByUserId(any(Pageable.class), anyInt()))
                .thenReturn(new PageImpl<Note>(notes, pageRequest, notes.size()));

        // Вызываем тестируемый метод, проверяем результат работы.
        mockMvc.perform(get("/sort/DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void page() throws Exception {
        // Настраиваем mockMvc для вызова методов сервисного слоя из тестируемого метода.
        when(userService.getCurrentUserId()).thenReturn(Optional.of(1));
        when(noteService.findByUserId(any(Pageable.class), anyInt()))
                .thenReturn(new PageImpl<Note>(notes, pageRequest, notes.size()));

        // Вызываем тестируемый метод, проверяем результат работы.
        mockMvc.perform(get("/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void newNote() throws Exception {
        mockMvc.perform(get("/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("operations/new"));
    }

    @Test
    void save() throws Exception {
        // Настраиваем mockMvc для вызова методов сервисного слоя из тестируемого метода.
        when(userService.getCurrentUser()).thenReturn(Optional.of(new User()));
        doAnswer((Answer<Void>) invocation -> null)
                .when(noteService).save(any(Note.class));

        // Вызываем тестируемый метод, проверяем результат работы.
        mockMvc.perform(post("/save")
                .param("message", "new message"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
        // Проверякм - был один вызов метода сервисного слоя.
        verify(noteService).save(any(Note.class));
    }

    @Test
    void edit() throws Exception {
        // Настраиваем mockMvc для вызова методов сервисного слоя из тестируемого метода.
        when(userService.getCurrentUser()).thenReturn(Optional.of(new User()));
        when(noteService.getById(anyInt(), any(User.class))).thenReturn(new Note());

        // Вызываем тестируемый метод, проверяем результат работы.
        mockMvc.perform(get("/edit/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("operations/edit"))
                .andExpect(model().attribute("note", instanceOf(Note.class)));
        // Проверякм - был один вызов метода сервисного слоя.
        verify(noteService).getById(anyInt(), any(User.class));
    }

    @Test
    void update() throws Exception {
        // Настраиваем mockMvc для вызова методов сервисного слоя из тестируемого метода.
        when(userService.getCurrentUser()).thenReturn(Optional.of(new User()));
        doAnswer((Answer<Void>) invocation -> null)
                .when(noteService).update(anyInt(), anyString(), anyBoolean(), any(User.class));

        // Вызываем тестируемый метод, проверяем результат работы.
        mockMvc.perform(post("/update")
                .param("id", "1")
                .param("message", "new message")
                .param("done", "true"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
        // Проверякм - был один вызов метода сервисного слоя.
        verify(noteService).update(anyInt(), anyString(), anyBoolean(), any(User.class));
    }

    @Test
    void delete() throws Exception {
        // Настраиваем mockMvc для вызова методов сервисного слоя из тестируемого метода.
        when(userService.getCurrentUser()).thenReturn(Optional.of(new User()));
        doAnswer((Answer<Void>) invocation -> null)
                .when(noteService).delete(anyInt(), any(User.class));

        // Вызываем тестируемый метод, проверяем результат работы.
        mockMvc.perform(get("/delete/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
        // Проверякм - был один вызов метода сервисного слоя.
        verify(noteService).delete(anyInt(), any(User.class));
    }
}
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
        when(userService.getCurrentUser()).thenReturn(Optional.of(new User()));
        when(noteService.findByUser(any(Pageable.class), any(User.class)))
                .thenReturn(new PageImpl<Note>(notes, pageRequest, notes.size()));

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("page", hasProperty("content", is(instanceOf(List.class)))))
                .andExpect(model().attribute("page", hasProperty("content", is(hasSize(notesSize)))));

        verify(noteService, never()).findByUserAndSearchParameters(
                any(Pageable.class), any(User.class), any(FilterAdjuster.class));
        verify(noteService).findByUser(any(Pageable.class), any(User.class));
    }

    @Test
    void listWithFilter() throws Exception {
        when(userService.getCurrentUser()).thenReturn(Optional.of(new User()));
        when(noteService.findByUserAndSearchParameters(
                any(Pageable.class), any(User.class), any(FilterAdjuster.class)))
                .thenReturn(new PageImpl<Note>(notes, pageRequest, notes.size()));

        mockMvc.perform(get("/")
                .param("searchText", "...")
                .param("done", "DONE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("page", hasProperty("content", is(instanceOf(List.class)))))
                .andExpect(model().attribute("page", hasProperty("content", is(hasSize(notesSize)))));

        verify(noteService).findByUserAndSearchParameters(
                any(Pageable.class), any(User.class), any(FilterAdjuster.class));
        verify(noteService, never()).findByUser(any(Pageable.class), any(User.class));
    }

    @Test
    void sortChoose() throws Exception {
        when(userService.getCurrentUser()).thenReturn(Optional.of(new User()));
        when(noteService.findByUser(any(Pageable.class), any(User.class)))
                .thenReturn(new PageImpl<Note>(notes, pageRequest, notes.size()));

        mockMvc.perform(get("/sort/ASC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void sortChooseDesc() throws Exception {
        when(userService.getCurrentUser()).thenReturn(Optional.of(new User()));
        when(noteService.findByUser(any(Pageable.class), any(User.class)))
                .thenReturn(new PageImpl<Note>(notes, pageRequest, notes.size()));

        mockMvc.perform(get("/sort/DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void page() throws Exception {
        when(userService.getCurrentUser()).thenReturn(Optional.of(new User()));
        when(noteService.findByUser(any(Pageable.class), any(User.class)))
                .thenReturn(new PageImpl<Note>(notes, pageRequest, notes.size()));

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
        when(userService.getCurrentUser()).thenReturn(Optional.of(new User()));
        doAnswer((Answer<Void>) invocation -> null)
                .when(noteService).save(any(Note.class));

        mockMvc.perform(post("/save")
                .param("message", "new message"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(noteService).save(any(Note.class));
    }

    @Test
    void edit() throws Exception {
        when(userService.getCurrentUser()).thenReturn(Optional.of(new User()));
        when(noteService.getById(anyInt(), any(User.class))).thenReturn(new Note());

        mockMvc.perform(get("/edit/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("operations/edit"))
                .andExpect(model().attribute("note", instanceOf(Note.class)));

        verify(noteService).getById(anyInt(), any(User.class));
    }

    @Test
    void update() throws Exception {
        when(userService.getCurrentUser()).thenReturn(Optional.of(new User()));
        doAnswer((Answer<Void>) invocation -> null)
                .when(noteService).update(anyInt(), anyString(), anyBoolean(), any(User.class));

        mockMvc.perform(post("/update")
                .param("id", "1")
                .param("message", "new message")
                .param("done", "true"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(noteService).update(anyInt(), anyString(), anyBoolean(), any(User.class));
    }

    @Test
    void delete() throws Exception {
        when(userService.getCurrentUser()).thenReturn(Optional.of(new User()));
        doAnswer((Answer<Void>) invocation -> null)
                .when(noteService).delete(anyInt(), any(User.class));

        mockMvc.perform(get("/delete/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(noteService).delete(anyInt(), any(User.class));
    }
}
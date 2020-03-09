package com.example.notes.services;

import com.example.notes.persist.entities.Note;
import com.example.notes.persist.entities.Role;
import com.example.notes.persist.entities.User;
import com.example.notes.persist.repositories.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class NoteServiceImplTest {
    private static final Integer NOTE_ID = 1;
    private static final String MSG = "To do something";
    private static final Date DATE_NOW = new Date();

    @InjectMocks
    NoteServiceImpl noteService;

    @Mock
    private NoteRepository noteRepository;

    private Note noteModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        User user = new User();
        user.setId(1);
        user.setUsername("user");
        user.setEncryptedPassword("12345");
        user.setRole(Role.USER);
        user.setActive(true);

        noteModel = new Note();
        noteModel.setId(NOTE_ID);
        noteModel.setMessage(MSG);
        noteModel.setDate(DATE_NOW);
        noteModel.setDone(false);
        noteModel.setUser(user);
    }

    @Test
    void getById() {
        when(noteRepository.getOne(anyInt())).thenReturn(noteModel);

        Note note = noteService.getById(1);

        assertNotNull(note);
        assertEquals(noteModel.getId(), note.getId());
        assertEquals(noteModel.getMessage(), note.getMessage());
        assertEquals(noteModel.getDate(), note.getDate());
        assertEquals(noteModel.isDone(), note.isDone());
        assertEquals(noteModel.getUser(), note.getUser());
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findByUserId() {
    }

    @Test
    void findByUserIdAndSearchParameters() {
    }
}
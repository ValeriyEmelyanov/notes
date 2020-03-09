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
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private User currentUser; // Текущий (залогинившийся) пользователь
    private User wrongUser;   // Какой-то другой (левый) пользователь
    private List<Note> notes;
    private int notesSize;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        currentUser = User.builder()
                .id(1)
                .username("user")
                .encryptedPassword("12345")
                .role(Role.USER)
                .active(true)
                .build();

        wrongUser = User.builder().id(99).build();

        noteModel = Note.builder()
                .id(NOTE_ID)
                .message(MSG)
                .date(DATE_NOW)
                .done(false)
                .user(currentUser)
                .build();

        notes = new ArrayList<>();
        notes.add(noteModel);
        notesSize = notes.size();
    }

    @Test
    void getById() {
        when(noteRepository.getOne(anyInt())).thenReturn(noteModel);

        Note note = noteService.getById(1, currentUser);

        assertNotNull(note);
        assertEquals(noteModel.getId(), note.getId());
        assertEquals(noteModel.getMessage(), note.getMessage());
        assertEquals(noteModel.getDate(), note.getDate());
        assertEquals(noteModel.isDone(), note.isDone());
        assertEquals(noteModel.getUser(), note.getUser());
    }

    @Test
    void getByIdWithWrongUser() {
        when(noteRepository.getOne(anyInt())).thenReturn(noteModel);

        assertThrows(IllegalArgumentException.class,
                () -> {
                    Note note = noteService.getById(1, wrongUser);
                });
    }

    @Test
    void save() {
        Note newNote = new Note("New message", currentUser);

        when(noteRepository.save(newNote))
                .thenAnswer(
                        (Answer<Void>) invocation -> {
                            notes.add(newNote);
                            return null;
                        });

        noteService.save(newNote);

        assertEquals(notesSize + 1, notes.size());
    }

    @Test
    void update() {
        String message = "New message";
        boolean done = true;

        when(noteRepository.getOne(anyInt())).thenReturn(noteModel);
        when(noteRepository.save(noteModel))
                .thenAnswer(
                        (Answer<Void>) invocation -> {
                            noteModel.setMessage(message);
                            noteModel.setDone(done);
                            noteModel.setUser(currentUser);
                            return null;
                        });

        noteService.update(NOTE_ID, message, done, currentUser);

        assertEquals(notesSize, notes.size());
        assertEquals(NOTE_ID, (Integer) noteModel.getId());
        assertEquals(message, noteModel.getMessage());
        assertEquals(DATE_NOW, noteModel.getDate());
        assertEquals(done, noteModel.isDone());
        assertEquals(currentUser, noteModel.getUser());
    }

    @Test
    void updateWithWrongUser() {
        String message = "New message";
        boolean done = true;
        //User user = currentUser;
        User user = wrongUser;

        when(noteRepository.getOne(anyInt())).thenReturn(noteModel);
        when(noteRepository.save(noteModel))
                .thenAnswer(
                        (Answer<Void>) invocation -> {
                            noteModel.setMessage(message);
                            noteModel.setDone(done);
                            noteModel.setUser(user);
                            return null;
                        });

        assertThrows(IllegalArgumentException.class,
                () -> {
                    noteService.update(NOTE_ID, message, done, user);
                });

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
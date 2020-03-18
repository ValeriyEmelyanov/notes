package com.example.notes.services;

import com.example.notes.filtering.DoneFilterOption;
import com.example.notes.filtering.FilterAdjuster;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

class NoteServiceImplTest {
    private static final Integer CURRENT_USER_ID = 1;
    private static final Integer NOTE_ID = 1;
    private static final String MSG = "To do everything";
    private static final Date DATE_NOW = new Date();
    private final static String SORT_FIELD = "date";

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

        // Подготовим данные для настройки mock

        currentUser = User.builder()
                .id(CURRENT_USER_ID)
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
        notes.add(Note.builder()
                .id(NOTE_ID + 1)
                .message("Some text")
                .done(false)
                .user(currentUser)
                .build());
        notes.add(Note.builder()
                .id(NOTE_ID + 2)
                .message(MSG)
                .done(true)
                .user(currentUser)
                .build());
        notes.add(Note.builder()
                .id(NOTE_ID + 3)
                .message("Wrong note")
                .done(false)
                .user(wrongUser)
                .build());
        notesSize = notes.size();
    }

    @Test
    void getById() {
        // настраиваем mock для вызова методов репозитория из тестируемого метода
        when(noteRepository.getOne(anyInt())).thenReturn(noteModel);

        // вызываем тестируемый метод
        Note note = noteService.getById(1, currentUser);

        // проверяем результат работы
        assertNotNull(note);
        assertEquals(noteModel.getId(), note.getId());
        assertEquals(noteModel.getMessage(), note.getMessage());
        assertEquals(noteModel.getDate(), note.getDate());
        assertEquals(noteModel.isDone(), note.isDone());
        assertEquals(noteModel.getUser(), note.getUser());
    }

    @Test
    void getByIdWithWrongUser() {
        // настраиваем mock для вызова методов репозитория из тестируемого метода
        when(noteRepository.getOne(anyInt())).thenReturn(noteModel);

        // вызываем тестируемый метод, проверяем результат работы
        assertThrows(IllegalArgumentException.class,
                () -> {
                    Note note = noteService.getById(1, wrongUser);
                });
    }

    @Test
    void save() {
        Note newNote = new Note("New message", currentUser);

        // настраиваем mock для вызова методов репозитория из тестируемого метода
        when(noteRepository.save(newNote))
                .thenAnswer(
                        (Answer<Void>) invocation -> {
                            notes.add(newNote);
                            return null;
                        });

        // вызываем тестируемый метод
        noteService.save(newNote);

        // проверяем результат работы
        assertEquals(notesSize + 1, notes.size());
    }

    @Test
    void update() {
        String message = "New message";
        boolean done = true;

        // настраиваем mock для вызова методов репозитория из тестируемого метода
        when(noteRepository.getOne(anyInt())).thenReturn(noteModel);
        when(noteRepository.save(noteModel))
                .thenAnswer(
                        (Answer<Void>) invocation -> {
                            noteModel.setMessage(message);
                            noteModel.setDone(done);
                            noteModel.setUser(currentUser);
                            return null;
                        });

        // вызываем тестируемый метод
        noteService.update(NOTE_ID, message, done, currentUser);

        // проверяем результат работы
        assertEquals(notesSize, notes.size());
        assertEquals(NOTE_ID, noteModel.getId());
        assertEquals(message, noteModel.getMessage());
        assertEquals(DATE_NOW, noteModel.getDate());
        assertEquals(done, noteModel.isDone());
        assertEquals(currentUser, noteModel.getUser());
    }

    @Test
    void updateWithWrongUser() {
        String message = "New message";
        boolean done = true;

        // настраиваем mock для вызова методов репозитория из тестируемого метода
        when(noteRepository.getOne(anyInt())).thenReturn(noteModel);
        when(noteRepository.save(noteModel))
                .thenAnswer(
                        (Answer<Void>) invocation -> {
                            noteModel.setMessage(message);
                            noteModel.setDone(done);
                            noteModel.setUser(wrongUser);
                            return null;
                        });

        // вызываем тестируемый метод, проверяем результат работы
        assertThrows(IllegalArgumentException.class,
                () -> {
                    noteService.update(NOTE_ID, message, done, wrongUser);
                });

    }

    @Test
    void delete() {
        // настраиваем mock для вызова методов репозитория из тестируемого метода
        when(noteRepository.getOne(NOTE_ID)).thenReturn(noteModel);
        doAnswer((Answer<Void>) invocation -> {
            notes.remove(noteModel);
            return null;
        }).when(noteRepository).deleteById(NOTE_ID);

        // вызываем тестируемый метод
        noteService.delete(NOTE_ID, currentUser);

        // проверяем результат работы
        assertEquals(notesSize - 1, notes.size());
    }

    @Test
    void deleteWithWrongUser() {
        // настраиваем mock для вызова методов репозитория из тестируемого метода
        when(noteRepository.getOne(NOTE_ID)).thenReturn(noteModel);
        doAnswer((Answer<Void>) invocation -> {
            notes.remove(noteModel);
            return null;
        }).when(noteRepository).deleteById(NOTE_ID);

        // вызываем тестируемый метод, проверяем результат работы
        assertThrows(IllegalArgumentException.class,
                () -> {
                    noteService.delete(NOTE_ID, wrongUser);
                });
    }

    @Test
    void findByUserId() {
        int pageSize = 10;
        List<Note> list = notes.stream()
                .filter(e -> currentUser.equals(e.getUser()))
                .collect(Collectors.toList());

        Sort sort = new Sort(Sort.Direction.ASC, SORT_FIELD);
        PageRequest pageRequest = PageRequest.of(0, pageSize, sort);

        // настраиваем mock для вызова методов репозитория из тестируемого метода
        when(noteRepository.findByUserId(pageRequest, CURRENT_USER_ID))
                .thenReturn(new PageImpl<Note>(list, pageRequest, list.size()));

        // вызываем тестируемый метод
        Page<Note> page = noteService.findByUserId(pageRequest, CURRENT_USER_ID);

        // проверяем результат работы
        assertEquals(Math.min(pageSize, list.size()), page.getContent().size());
    }

    @Test
    void findByUserIdAndSearchParameters() {
        int pageSize = 10;
        Sort sort = new Sort(Sort.Direction.ASC, SORT_FIELD);
        PageRequest pageRequest = PageRequest.of(0, pageSize, sort);

        ////////////
        // All

        List<Note> list = notes.stream()
                .filter(e -> currentUser.equals(e.getUser()))
                .collect(Collectors.toList());

        // настраиваем mock для вызова методов репозитория из тестируемого метода
        when(noteRepository.findByUserId(pageRequest, CURRENT_USER_ID))
                .thenReturn(new PageImpl<Note>(list, pageRequest, list.size()));

        FilterAdjuster filterAdjuster = new FilterAdjuster("", DoneFilterOption.ALL);
        // вызываем тестируемый метод
        Page<Note> page = noteService.findByUserIdAndSearchParameters(pageRequest, CURRENT_USER_ID, filterAdjuster);

        // проверяем результат работы
        assertEquals(Math.min(pageSize, list.size()), page.getContent().size());

        ////////////
        // by Text

        list = notes.stream()
                .filter(e -> currentUser.equals(e.getUser()) && e.getMessage().contains(MSG))
                .collect(Collectors.toList());

        // настраиваем mock для вызова методов репозитория из тестируемого метода
        when(noteRepository.findByUserIdAndSearchText(pageRequest, CURRENT_USER_ID, MSG))
                .thenReturn(new PageImpl<Note>(list, pageRequest, list.size()));

        filterAdjuster = new FilterAdjuster(MSG, DoneFilterOption.ALL);
        // вызываем тестируемый метод
        page = noteService.findByUserIdAndSearchParameters(pageRequest, CURRENT_USER_ID, filterAdjuster);

        // проверяем результат работы
        assertEquals(Math.min(pageSize, list.size()), page.getContent().size());

        ////////////
        // by Done

        final boolean done = true;
        list = notes.stream()
                .filter(e -> currentUser.equals(e.getUser()) && e.isDone() == done)
                .collect(Collectors.toList());

        // настраиваем mock для вызова методов репозитория из тестируемого метода
        when(noteRepository.findByUserIdAndDone(pageRequest, CURRENT_USER_ID, done))
                .thenReturn(new PageImpl<Note>(list, pageRequest, list.size()));

        filterAdjuster = new FilterAdjuster("", DoneFilterOption.DONE);
        // вызываем тестируемый метод
        page = noteService.findByUserIdAndSearchParameters(pageRequest, CURRENT_USER_ID, filterAdjuster);

        // проверяем результат работы
        assertEquals(Math.min(pageSize, list.size()), page.getContent().size());

        //////////////////////
        // by Text and Done

        list = notes.stream()
                .filter(e -> currentUser.equals(e.getUser()) && e.getMessage().contains(MSG) && e.isDone() == done)
                .collect(Collectors.toList());

        // настраиваем mock для вызова методов репозитория из тестируемого метода
        when(noteRepository.findByUserIdAndDoneAndSearchText(pageRequest, CURRENT_USER_ID, done, MSG))
                .thenReturn(new PageImpl<Note>(list, pageRequest, list.size()));

        filterAdjuster = new FilterAdjuster(MSG, DoneFilterOption.DONE);
        // вызываем тестируемый метод
        page = noteService.findByUserIdAndSearchParameters(pageRequest, CURRENT_USER_ID, filterAdjuster);

        // проверяем результат работы
        assertEquals(Math.min(pageSize, list.size()), page.getContent().size());
    }
}
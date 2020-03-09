package com.example.notes.services;

import com.example.notes.filtering.FilterAdjuster;
import com.example.notes.persist.entities.Note;
import com.example.notes.persist.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Интерфейс сервиса заметок
 */
public interface NoteService {
    Note getById(Integer id, User user);
    void save(Note note);
    void update(Integer id, String message, boolean done, User user);
    void delete(Integer id, User user);

    Page<Note> findByUserId(Pageable pageable, Integer userId);
    Page<Note> findByUserIdAndSearchParameters(Pageable pageable, Integer userId, FilterAdjuster filterAdjuster);
}

package com.example.notes.services;

import com.example.notes.filtering.FilterAdjuster;
import com.example.notes.persist.entities.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface NoteService {
    Note getById(Integer id);
    void save(Note note);
    void update(Integer id, String message, boolean done);
    void delete(Integer id);

    Page<Note> findAll(Pageable pageable);
    Page<Note> findBySearchParameters(Pageable pageable, FilterAdjuster filterAdjuster);
}

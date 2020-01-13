package com.example.notes.services;

import com.example.notes.persist.entities.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService {
    Note getById(Integer id);
    void save(Note note);
    void update(Integer id, String message, boolean done);
    void delete(Integer id);
    List<Note> findAll();
}
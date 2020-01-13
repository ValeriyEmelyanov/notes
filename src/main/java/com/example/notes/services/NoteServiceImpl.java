package com.example.notes.services;

import com.example.notes.persist.entities.Note;
import com.example.notes.persist.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private NoteRepository repository;

    @Autowired
    public void setRepository(NoteRepository repository) {
        this.repository = repository;
    }


    @Override
    public Note getById(Integer id) {
        return repository.getOne(id);
    }

    @Override
    public void save(Note note) {
        repository.save(note);
    }

    @Override
    public void update(Integer id, String message, boolean done) {
        Note note = repository.getOne(id);
        note.setMessage(message);
        note.setDone(done);
        repository.save(note);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<Note> findAll() {
        return repository.findAll();
    }
}

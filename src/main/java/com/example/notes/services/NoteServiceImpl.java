package com.example.notes.services;

import com.example.notes.persist.entities.Note;
import com.example.notes.persist.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public Page<Note> findAllByOrOrderByDateAsc(Pageable pageable) {
        return repository.findAllByOrderByDateAsc(pageable);
    }

    @Override
    public Page<Note> findAllByOrOrderByDateDesc(Pageable pageable) {
        return repository.findAllByOrderByDateDesc(pageable);
    }

    @Override
    public Page<Note> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}

package com.example.notes.services.Impl;

import com.example.notes.filtering.DoneFilterOption;
import com.example.notes.filtering.FilterAdjuster;
import com.example.notes.persist.entities.Note;
import com.example.notes.persist.entities.User;
import com.example.notes.persist.repositories.NoteRepository;
import com.example.notes.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Реализация сервиса заметок.
 */
@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    /**
     * Репозиторий заметок
     */
    private NoteRepository repository;

    @Autowired
    public void setRepository(NoteRepository repository) {
        this.repository = repository;
    }


    @Override
    public Note getById(Integer id, User user) {
        Note note = repository.getOne(id);

        if (user == null
                || note == null
                || note.getUser() == null
                || !user.getId().equals(note.getUser().getId())) {
            throw new IllegalArgumentException();
        }

        return note;
    }

    @Override
    public void save(Note note) {
        repository.save(note);
    }

    @Override
    public void update(Integer id, String message, boolean done, User user) {
        Note note = repository.getOne(id);

        if (user == null
                || note == null
                || note.getUser() == null
                || !user.getId().equals(note.getUser().getId())) {
            throw new IllegalArgumentException();
        }

        note.setMessage(message);
        note.setDone(done);
        note.setUser(user);
        repository.save(note);
    }

    @Override
    public void delete(Integer id, User user) {
        Note note = repository.getOne(id);

        if (user == null
                || note == null
                || note.getUser() == null
                || !user.getId().equals(note.getUser().getId())) {
            throw new IllegalArgumentException();
        }

        repository.deleteById(id);
    }

    @Override
    public Page<Note> findByUser(Pageable pageable, User user) {
        return repository.findByUser(pageable, user);
    }

    @Override
    public Page<Note> findByUserAndSearchParameters(
            Pageable pageable, User user, FilterAdjuster filterAdjuster) {

        if (filterAdjuster.isAll()) {
            return findByUser(pageable, user);
        }
        if (filterAdjuster.getDone() == DoneFilterOption.ALL && !filterAdjuster.getSearchText().isEmpty()) {
            return repository.findByUserAndMessageContaining(
                    pageable, user, filterAdjuster.getSearchText());
        }
        if (filterAdjuster.getDone() != DoneFilterOption.ALL && filterAdjuster.getSearchText().isEmpty()) {
            return repository.findByUserAndDone(pageable,
                    user, filterAdjuster.getDone() == DoneFilterOption.DONE);
        }
        return repository.findByUserAndDoneAndMessageContaining(pageable,
                user, filterAdjuster.getDone() == DoneFilterOption.DONE, filterAdjuster.getSearchText());
    }
}

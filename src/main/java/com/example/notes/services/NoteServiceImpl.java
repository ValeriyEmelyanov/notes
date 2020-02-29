package com.example.notes.services;

import com.example.notes.filtering.DoneFilterOption;
import com.example.notes.filtering.FilterAdjuster;
import com.example.notes.persist.entities.Note;
import com.example.notes.persist.entities.User;
import com.example.notes.persist.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Реализация сервиса заметок
 */
@Service
@Transactional
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
    public void update(Integer id, String message, boolean done, User user) {
        Note note = repository.getOne(id);
        note.setMessage(message);
        note.setDone(done);
        note.setUser(user);
        repository.save(note);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Note> findByUserId(Pageable pageable, Integer userId) {
        return repository.findByUserId(pageable, userId);
    }

    @Override
    public Page<Note> findByUserIdAndSearchParameters(Pageable pageable,
                                                      Integer userId, FilterAdjuster filterAdjuster) {

        if (filterAdjuster.isAll()) {
            return findByUserId(pageable, userId);
        }
        if (filterAdjuster.getDone() == DoneFilterOption.ALL && !filterAdjuster.getSearchText().isEmpty()) {
            return repository.findByUserIdAndSearchText(pageable,
                    userId, filterAdjuster.getSearchText());
        }
        if (filterAdjuster.getDone() != DoneFilterOption.ALL && filterAdjuster.getSearchText().isEmpty()) {
            return repository.findByUserIdAndDone(pageable,
                    userId, filterAdjuster.getDone() == DoneFilterOption.DONE);
        }
        return repository.findByUserIdAndDoneAndSearchText(pageable,
                userId, filterAdjuster.getDone() == DoneFilterOption.DONE, filterAdjuster.getSearchText());
    }
}

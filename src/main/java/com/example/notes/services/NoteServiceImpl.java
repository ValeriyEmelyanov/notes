package com.example.notes.services;

import com.example.notes.filtering.DoneFilterOption;
import com.example.notes.filtering.FilterAdjuster;
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
    public Page<Note> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Note> findBySearchParameters(Pageable pageable, FilterAdjuster filterAdjuster) {

        if (filterAdjuster.isAll()) {
            return findAll(pageable);
        }
        if (filterAdjuster.getDone() == DoneFilterOption.ALL && !filterAdjuster.getSearchText().isEmpty()) {
            return repository.findBySearchText(pageable,
                    filterAdjuster.getSearchText());
        }
        if (filterAdjuster.getDone() != DoneFilterOption.ALL && filterAdjuster.getSearchText().isEmpty()) {
            return repository.findByDone(pageable,
                    filterAdjuster.getDone() == DoneFilterOption.DONE);
        }
        return repository.findByDoneAndSearchText(pageable,
                filterAdjuster.getDone() == DoneFilterOption.DONE, filterAdjuster.getSearchText());
    }
}

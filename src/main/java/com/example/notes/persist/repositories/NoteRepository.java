package com.example.notes.persist.repositories;

import com.example.notes.persist.entities.Note;
import com.example.notes.persist.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий заметок
 */
public interface NoteRepository extends JpaRepository<Note, Integer> {

    Page<Note> findByUser(Pageable pageable, User user);
    Page<Note> findByUserAndMessageContaining(Pageable pageable, User user, String message);
    Page<Note> findByUserAndDone(Pageable pageable, User user, boolean done);
    Page<Note> findByUserAndDoneAndMessageContaining(
            Pageable pageable, User user, boolean done, String message);

}

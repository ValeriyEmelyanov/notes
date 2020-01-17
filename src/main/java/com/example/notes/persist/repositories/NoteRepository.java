package com.example.notes.persist.repositories;

import com.example.notes.persist.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Integer> {
}

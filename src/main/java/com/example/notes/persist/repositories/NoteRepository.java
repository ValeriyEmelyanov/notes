package com.example.notes.persist.repositories;

import com.example.notes.persist.entities.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoteRepository extends JpaRepository<Note, Integer> {

    @Query("select n from Note n where lower(n.message) like lower(concat('%', :searchText, '%'))")
    Page<Note> findBySearchParameters(
            @Param("searchText") String searchText,
            Pageable pageable);

}

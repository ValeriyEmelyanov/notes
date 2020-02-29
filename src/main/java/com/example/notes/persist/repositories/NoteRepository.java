package com.example.notes.persist.repositories;

import com.example.notes.persist.entities.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Репозиторий заметок
 */
public interface NoteRepository extends JpaRepository<Note, Integer> {

    @Query("select n from Note n where n.user.id=:userId")
    Page<Note> findByUserId(
            Pageable pageable,
            @Param("userId") Integer userId);

    @Query("select n from Note n where n.user.id=:userId and lower(n.message) like lower(concat('%', :searchText, '%'))")
    Page<Note> findByUserIdAndSearchText(
            Pageable pageable,
            @Param("userId") Integer userId,
            @Param("searchText") String searchText);

    @Query("select n from Note n where n.user.id=:userId and n.done=:done")
    Page<Note> findByUserIdAndDone(
            Pageable pageable,
            @Param("userId") Integer userId,
            @Param("done") boolean done);

    @Query("select n from Note n where n.user.id=:userId and n.done=:done and lower(n.message) like lower(concat('%', :searchText, '%'))")
    Page<Note> findByUserIdAndDoneAndSearchText(
            Pageable pageable,
            @Param("userId") Integer userId,
            @Param("done") boolean done,
            @Param("searchText") String searchText);

}

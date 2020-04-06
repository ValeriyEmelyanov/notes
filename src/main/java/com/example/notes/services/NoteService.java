package com.example.notes.services;

import com.example.notes.filtering.FilterAdjuster;
import com.example.notes.persist.entities.Note;
import com.example.notes.persist.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Интерфейс сервиса заметок.
 */
public interface NoteService {
    /**
     * Получает заметку по идентификатору, если соотвествует пользователь заметки.
     *
     * @param id идентификатор заметки
     * @param user пользователь
     * @return заметка
     */
    Note getById(Integer id, User user);

    /**
     * Сохраняет заметку
     *
     * @param note заметка
     */
    void save(Note note);

    /**
     * Обновляет заметку, если соотвествует пользователь заметки.
     *
     * @param id идентификатор заметки
     * @param message текст заметки
     * @param done признак выполнения
     * @param user пользователь
     */
    void update(Integer id, String message, boolean done, User user);

    /**
     * Удаляет заметку.
     *
     * @param id идентификатор заметки
     * @param user пользователь
     */
    void delete(Integer id, User user);

    /**
     * Получает страницу со списком заметок пользователя.
     *
     * @param pageable настройки страницы
     * @param user пользователь
     * @return страница со списком заметок
     */
    Page<Note> findByUser(Pageable pageable, User user);

    /**
     * Получает страницу с отфильтрованным списком заметок пользователя.
     *
     * @param pageable параметры страницы
     * @param user пользователь
     * @param filterAdjuster фильтр
     * @return
     */
    Page<Note> findByUserAndSearchParameters(Pageable pageable, User user, FilterAdjuster filterAdjuster);
}

package com.example.notes.services;

import com.example.notes.persist.entities.User;
import com.example.notes.transfer.UserDto;
import com.example.notes.transfer.UserRegDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Интерфейс сервиса пользователей.
 * Удаление физическое отсутствует - у пользователя снимается флажок активности.
 */
public interface UserService {
    /**
     * Получает Optinal имени текущего аутотентифицированного пользователя.
     *
     * @return Optinal имени текущего аутотентифицированного пользователя
     */
    Optional<String> getCurrentUsername();

    /**
     * Получает Optinal текущего аутотентифицированного пользователя.
     *
     * @return Optinal текущего аутотентифицированного пользователя
     */
    Optional<User> getCurrentUser();

    /**
     * Ищет пользователя по имени.
     *
     * @param username имя пользователя
     * @return Optional передаваемых данных пользователя
     */
    Optional<UserDto> findByUsername(String username);

    /**
     * Получает данные пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return передаваемые данные пользователя
     */
    UserDto getById(Integer id);

    /**
     * Создает нового пользователя.
     *
     * @param userRegDto регистрационные данные пользователя.
     */
    void create(UserRegDto userRegDto);

    /**
     * Получает страницу со списком пользователей.
     *
     * @param pageable параметры страницы
     * @return страница со списком пользователей
     */
    Page<UserDto> findAll(Pageable pageable);

    /**
     * Обновляет данные пользователя.
     *
     * @param userDto передаваемые данные пользователя
     */
    void update(UserDto userDto);

    /**
     * Отключает пользователя.
     *
     * @param id идентификатор пользователя
     */
    void disable(Integer id);
}

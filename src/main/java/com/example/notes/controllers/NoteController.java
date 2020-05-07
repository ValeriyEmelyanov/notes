package com.example.notes.controllers;

import com.example.notes.filtering.DoneFilterOption;
import com.example.notes.filtering.FilterAdjuster;
import com.example.notes.persist.entities.Note;
import com.example.notes.persist.entities.User;
import com.example.notes.services.NoteService;
import com.example.notes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер заметок - текущих дел.
 */
@Controller
public class NoteController {

    /**
     * Константы для настройки параметров страницы.
     */
    private final static int PAGE_SIZE = 10;
    private final static String SORT_FIELD = "date";
    private final static String SORT_ORDER_ASC = "ASC";
    private final static String SORT_ORDER_DESC = "DESC";

    /**
     * Сервис для работы с заметками.
     */
    private NoteService noteService;

    /**
     * Сервис для работы с пользователями.
     */
    private UserService userService;

    @Autowired
    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Маппинг корневого каталога.
     *
     * @param pageable параметры страницы
     * @param sortDateOrder поле и порядок сортировки
     * @param filterAdjuster настройки фильтра данных страницы
     * @param model модель
     * @return возвращает имя страницы
     */
    @GetMapping("/")
    public String list(@PageableDefault(size = PAGE_SIZE) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "ASC") String sortDateOrder,
                       FilterAdjuster filterAdjuster,
                       Model model) {

        if (sortDateOrder != null && sortDateOrder.toUpperCase().equals(SORT_ORDER_DESC)) {
            sortDateOrder = SORT_ORDER_DESC;
        } else {
            sortDateOrder = SORT_ORDER_ASC;
        }
        Sort sort = new Sort(
                sortDateOrder.equals(SORT_ORDER_DESC) ? Sort.Direction.DESC : Sort.Direction.ASC,
                SORT_FIELD);

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Note> page = null;

        User currentUser = userService.getCurrentUser().orElseThrow(IllegalArgumentException::new);

        if (filterAdjuster.isAll()) {
            page = noteService.findByUser(pageRequest, currentUser);
        } else {
            page = noteService.findByUserAndSearchParameters(pageRequest, currentUser, filterAdjuster);
        }

        model.addAttribute("page", page);
        model.addAttribute("sortDateOrder", sortDateOrder);
        model.addAttribute("filterAdjuster", filterAdjuster);
        model.addAttribute("filterOptions", DoneFilterOption.values());

        return "index";
    }

    /**
     * Маппинг запроса на сортировку данных страницы.
     *
     * @param pageable параметры страницы
     * @param sortDateOrder поле и порядок сортировки
     * @param filterAdjuster настройки фильтра данных страницы
     * @param model модель
     * @return имя страницы
     */
    @GetMapping("/sort/{sortDateOrder}")
    public String sortChoose(@PageableDefault(size = PAGE_SIZE) Pageable pageable,
                             @PathVariable String sortDateOrder,
                             FilterAdjuster filterAdjuster,
                             Model model) {
        return list(pageable, sortDateOrder, filterAdjuster, model);
    }

    /**
     * Маппинг запроса на список заметок.
     *
     * @param pageable параметры страницы
     * @param sortDateOrder поле и порядок сортировки
     * @param filterAdjuster настройки фильтра данных страницы
     * @param model модель
     * @return имя страницы
     */
    @GetMapping("/list")
    public String page(@PageableDefault(size = PAGE_SIZE) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "ASC") String sortDateOrder,
                       FilterAdjuster filterAdjuster,
                       Model model) {
        return list(pageable, sortDateOrder, filterAdjuster, model);
    }

    /**
     * Маппинг запроса страницы ввода новой заметки.
     *
     * @return имя страницы
     */
    @GetMapping("/new")
    public String newNote() {
        return "operations/new";
    }

    /**
     * Маппинг запроса на сохранение заметки.
     *
     * @param message текст заметки
     * @return перенаправляет на корневую страницу
     */
    @PostMapping("/save")
    public String save(@RequestParam String message) {
        User user = userService.getCurrentUser().orElseThrow(IllegalArgumentException::new);
        noteService.save(new Note(message, user));
        return "redirect:/";
    }

    /**
     * Маппинг запроса страницы редактирования заметки.
     *
     * @param id идентификатор заметки
     * @param model модель
     * @return имя страницы
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        User user = userService.getCurrentUser().orElseThrow(IllegalArgumentException::new);
        Note note = noteService.getById(id, user);
        model.addAttribute("note", note);
        return "operations/edit";
    }

    /**
     * Маппинг запроса на сохранение измененной заметки.
     *
     * @param id идентификатор заметки
     * @param message текст заметки
     * @param done отметка о выполнении
     * @return перенаправляет на корневую страницу
     */
    @PostMapping("/update")
    public String update(@RequestParam Integer id, @RequestParam String message,
                         @RequestParam(value = "done", required = false) boolean done) {
        User user = userService.getCurrentUser().orElseThrow(IllegalArgumentException::new);
        noteService.update(id, message, done, user);
        return "redirect:/";
    }

    /**
     * Маппинг запроса на удаление заметки.
     *
     * @param id идентификатор заметки
     * @return перенаправляет на корневую страницу
     */
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        User user = userService.getCurrentUser().orElseThrow(IllegalArgumentException::new);
        noteService.delete(id, user);
        return "redirect:/";
    }
}

package com.example.notes.controllers;

import com.example.notes.filtering.DoneFilterOption;
import com.example.notes.filtering.FilterAdjuster;
import com.example.notes.persist.entities.Note;
import com.example.notes.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NoteController {

    private final static int PAGE_SIZE = 10;
    private final static String SORT_FIELD = "date";
    private final static String SORT_ORDER_ASC = "ASC";
    private final static String SORT_ORDER_DESC = "DESC";

    private NoteService noteService;

    @Autowired
    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }

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

        if (filterAdjuster.isAll()) {
            page = noteService.findAll(pageRequest);
        } else {
            page = noteService.findBySearchParameters(pageRequest, filterAdjuster);
        }

        model.addAttribute("page", page);
        model.addAttribute("sortDateOrder", sortDateOrder);
        model.addAttribute("filterAdjuster", filterAdjuster);
        model.addAttribute("filterOptions", DoneFilterOption.values());
        return "index";
    }

    @GetMapping("/sort/{sortDateOrder}")
    public String sortChoose(@PageableDefault(size = PAGE_SIZE) Pageable pageable,
                             @PathVariable String sortDateOrder,
                             FilterAdjuster filterAdjuster,
                             Model model) {
        return list(pageable, sortDateOrder, filterAdjuster, model);
    }

    @GetMapping("/list")
    public String page(@PageableDefault(size = PAGE_SIZE) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "ASC") String sortDateOrder,
                       FilterAdjuster filterAdjuster,
                       Model model) {
        return list(pageable, sortDateOrder, filterAdjuster, model);
    }

    @GetMapping("/new")
    public String newNote() {
        return "operations/new";
    }

    @PostMapping("/save")
    public String save(@RequestParam String message) {
        noteService.save(new Note(message));
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Note note = noteService.getById(id);
        model.addAttribute("note", note);
        return "operations/edit";
    }

    @PostMapping("/update")
    public String update(@RequestParam Integer id, @RequestParam String message,
                         @RequestParam(value = "done", required = false) boolean done) {
        noteService.update(id, message, done);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        noteService.delete(id);
        return "redirect:/";
    }
}

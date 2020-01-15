package com.example.notes.controllers;

import com.example.notes.persist.entities.Note;
import com.example.notes.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private NoteService noteService;
    private String sortDateMethod = "ASC";

    @Autowired
    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/")
    public String list(@PageableDefault(size = PAGE_SIZE) Pageable pageable, Model model) {
        Page<Note> page = getList(pageable);
        model.addAttribute("page", page);
        model.addAttribute("sortDate", sortDateMethod);
        return "index";
    }

    private Page<Note> getList(Pageable pageable) {
        if (sortDateMethod != null && sortDateMethod.toUpperCase().equals("DESC")) {
            return noteService.findAllByOrOrderByDateDesc(pageable);
        } else {
            return noteService.findAllByOrOrderByDateAsc(pageable);
        }
    }

    @GetMapping("/sort/{sortDate}")
    public String sortChoose(@PathVariable String sortDate, @PageableDefault(size = PAGE_SIZE) Pageable pageable, Model model) {
        sortDateMethod = sortDate;
        return list(pageable, model);
    }

    @GetMapping("/page")
    public String page(@PageableDefault(size = PAGE_SIZE) Pageable pageable, Model model) {
        return list(pageable, model);
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

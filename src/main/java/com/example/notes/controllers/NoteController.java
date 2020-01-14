package com.example.notes.controllers;

import com.example.notes.persist.entities.Note;
import com.example.notes.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class NoteController {

    private NoteService noteService;
    private String sortDateMethod = "ASC";

    @Autowired
    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/")
    public String list(Model model) {
        List<Note> notes = getList();
        model.addAttribute("notes", notes);
        model.addAttribute("sortDate", sortDateMethod);
        return "index";
    }

    @GetMapping("/sort/{sortDate}")
    public String sortChoose(@PathVariable String sortDate, Model model) {
        sortDateMethod = sortDate;
        return list(model);
    }

    private List<Note> getList() {
        if (sortDateMethod != null && sortDateMethod.toUpperCase().equals("DESC")) {
            return noteService.findAllByOrOrderByDateDesc();
        } else {
            return noteService.findAllByOrOrderByDateAsc();
        }
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

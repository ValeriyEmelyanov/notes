package com.example.notes.persist.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Сущность заметки (запись о деле, которое необхоимо сделать)
 */
@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "message")
    private String message;

    @Column(name = "date")
    private Date date;

    @Column(name = "done")
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Note() {
    }

    public Note(String message, User user) {
        this.message = message;
        this.date = new Date();
        this.done = false;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static NoteBuilder builder() {
        return new NoteBuilder();
    }

    /**
     * Билдер для создания новой заметки
     */
    public static class NoteBuilder {
        private Note note;

        public NoteBuilder() {
            this.note = new Note();
        }

        public NoteBuilder id(Integer id) {
            this.note.setId(id);
            return this;
        }

        public NoteBuilder message(String message) {
            this.note.setMessage(message);
            return this;
        }

        public NoteBuilder date(Date date) {
            this.note.setDate(date);
            return this;
        }

        public NoteBuilder done(boolean done) {
            this.note.setDone(done);
            return this;
        }

        public NoteBuilder user(User user) {
            this.note.setUser(user);
            return this;
        }

        public Note build() {
            return this.note;
        }
    }
}

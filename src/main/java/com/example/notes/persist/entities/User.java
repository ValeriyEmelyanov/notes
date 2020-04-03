package com.example.notes.persist.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

/**
 * Аккаунт пользователя.
 * Делаем, что у пользователя может быть только одна роль.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String username;

    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column
    private boolean active;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Note> notes;

    public User() {
    }

    public User(Integer id, String username, String encryptedPassword, Role role, boolean active) {
        this.id = id;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.role = role;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return active == user.active &&
                id.equals(user.id) &&
                username.equals(user.username) &&
                encryptedPassword.equals(user.encryptedPassword) &&
                role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, encryptedPassword, role, active);
    }
}

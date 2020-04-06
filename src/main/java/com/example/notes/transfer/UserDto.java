package com.example.notes.transfer;

import com.example.notes.persist.entities.Role;

/**
 * Класс для передачи данных пользователя между представлением и бэкэндом.
 */
public class UserDto {
    private Integer id;
    private String username;
    private Role role;
    private boolean active;

    public UserDto() {
    }

    public UserDto(int id, String username, Role role, boolean active) {
        this.id = id;
        this.username = username;
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
}

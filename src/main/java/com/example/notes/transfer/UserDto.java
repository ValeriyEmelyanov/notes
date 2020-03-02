package com.example.notes.transfer;

import com.example.notes.persist.entities.Role;

/**
 * Класс для передачи данных аккаунта пользователя на фронтэнд
 */
public class UserDto {
    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    /**
     * Билдер для содания нового пользователя для пережачи данных
     */
    public static class UserDtoBuilder {
        private UserDto userDto;

        UserDtoBuilder() {
            this.userDto = new UserDto();
        }

        public UserDtoBuilder id(int id) {
            this.userDto.setId(id);
            return this;
        }

        public UserDtoBuilder username(String username) {
            this.userDto.setUsername(username);
            return this;
        }

        public UserDtoBuilder role(Role role) {
            this.userDto.setRole(role);
            return this;
        }

        public UserDtoBuilder active(boolean active) {
            this.userDto.setActive(active);
            return this;
        }

        public UserDto build() {
            return this.userDto;
        }
    }

}

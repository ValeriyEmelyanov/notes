package com.example.notes.persist.entities;

import javax.persistence.*;

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

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    /**
     * Билдер для содания нового пользователя
     */
    public static class UserBuilder {
        private User user;

        UserBuilder() {
            this.user = new User();
        }

        public UserBuilder id(int id) {
            this.user.setId(id);
            return this;
        }

        public UserBuilder username(String username) {
            this.user.setUsername(username);
            return this;
        }

        public UserBuilder encryptedPassword(String encryptedPassword) {
            this.user.setEncryptedPassword(encryptedPassword);
            return this;
        }

        public UserBuilder role(Role role) {
            this.user.setRole(role);
            return this;
        }

        public UserBuilder active(boolean active) {
            this.user.setActive(active);
            return this;
        }

        public User build() {
            return this.user;
        }
    }

}

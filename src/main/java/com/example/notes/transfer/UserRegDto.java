package com.example.notes.transfer;

import javax.validation.constraints.NotEmpty;

/**
 * Регистрационные данные пользователя.
 * Служит для передачи данных пользователя при регистрации или аутентификации
 * из слоя представления на бэкэнд.
 */
public class UserRegDto {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String matchingPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }
}

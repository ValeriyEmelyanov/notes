package com.example.notes.transfer;

public class UserDto {

    private String username;
    private String password;
    private String matchingPassword;

    public UserDto() {
    }

//    public UserDto(String username, String password) {
//        this.username = username;
//        this.password = password;
//    }

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

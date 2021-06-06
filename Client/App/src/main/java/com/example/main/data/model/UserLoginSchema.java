package com.example.main.data.model;

public class UserLoginSchema {
    private String email;
    private String password;

    public UserLoginSchema(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

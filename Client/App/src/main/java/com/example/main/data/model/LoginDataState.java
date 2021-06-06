package com.example.main.data.model;

import androidx.annotation.Nullable;

public class LoginDataState {
    @Nullable
    final private String emailError;
    @Nullable
    final private String passwordError;

    private boolean isValid;

    public LoginDataState(@Nullable String emailError, @Nullable String passwordError){
        this.emailError = emailError;
        this.passwordError = passwordError;
    }
    public LoginDataState(boolean isValid){
        this.emailError = this.passwordError = null;
        this.isValid = isValid;
    }

    @Nullable
    public String getEmailError() {
        return emailError;
    }

    @Nullable
    public String getPasswordError() {
        return passwordError;
    }

    public boolean isValid() {
        return isValid;
    }
}

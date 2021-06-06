package com.example.main.data.model;

import androidx.annotation.Nullable;

public class LoginDataState {
    @Nullable
    final private String usernameError;
    @Nullable
    final private String passwordError;

    private boolean isValid;

    public LoginDataState(@Nullable String usernameError, @Nullable String passwordError){
        this.usernameError = usernameError;
        this.passwordError = passwordError;
    }
    public LoginDataState(boolean isValid){
        this.usernameError = this.passwordError = null;
        this.isValid = isValid;
    }

    @Nullable
    public String getEmailError() {
        return usernameError;
    }

    @Nullable
    public String getPasswordError() {
        return passwordError;
    }

    public boolean isValid() {
        return isValid;
    }
}

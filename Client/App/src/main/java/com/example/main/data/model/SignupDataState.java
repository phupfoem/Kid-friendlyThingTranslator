package com.example.main.data.model;

import androidx.annotation.Nullable;

public class SignupDataState {
    @Nullable
    final private String nameError;
    @Nullable
    final private String emailError;
    @Nullable
    final private String passwordError;
    @Nullable
    final private String passwordConfirmError;

    private boolean isValid;

    public SignupDataState(
            @Nullable String nameError,
            @Nullable String emailError,
            @Nullable String passwordError,
            @Nullable String passwordConfirmError
    ) {
        this.nameError = nameError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.passwordConfirmError = passwordConfirmError;
    }
    public SignupDataState(boolean isValid){
        this.nameError = this.emailError = this.passwordError = this.passwordConfirmError = null;
        this.isValid = isValid;
    }

    @Nullable
    public String getNameError() {
        return nameError;
    }

    @Nullable
    public String getEmailError() {
        return emailError;
    }

    @Nullable
    public String getPasswordError() {
        return passwordError;
    }

    @Nullable
    public String getPasswordConfirmError() {
        return passwordConfirmError;
    }

    public boolean isValid() {
        return isValid;
    }
}

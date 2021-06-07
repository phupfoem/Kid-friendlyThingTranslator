package com.example.main.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.main.data.model.Result;
import com.example.main.data.model.SignupDataState;
import com.example.main.data.remote.MainApiUtils;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupViewModel extends ViewModel {
    public final String NAME_ERROR_MSG = "Invalid person name";
    public final String EMAIL_ERROR_MSG = "Invalid email";
    public final String PASSWORD_ERROR_MSG = "Invalid password";
    public final String PASSWORD_CONFIRM_ERROR_MSG = "Passwords mismatched";

    @NonNull
    private final MutableLiveData<SignupDataState> dataState = new MutableLiveData<>();

    private final MutableLiveData<Result<String>> result = new MutableLiveData<>();
    private final MainApiUtils mainApiUtils = MainApiUtils.getInstance();

    public void signupDataChange(String name, String email, String password, String passwordConfirm) {
        if (!isNameValid(name)) {
            dataState.setValue(new SignupDataState(NAME_ERROR_MSG, null, null, null));
        }
        else if (!isEmailValid(email)) {
            dataState.setValue(new SignupDataState(null, EMAIL_ERROR_MSG, null, null));
        }
        else if (!isPasswordValid(password)) {
            dataState.setValue(new SignupDataState(null, null, PASSWORD_ERROR_MSG, null));
        }
        else if (!isPasswordConfirmValid(password, passwordConfirm)) {
            dataState.setValue(new SignupDataState(null, null, null, PASSWORD_CONFIRM_ERROR_MSG));
        }
        else {
            dataState.setValue(new SignupDataState(true));
        }
    }

    public void signup(String name, String email, String password) {
        Call<JsonObject> call = mainApiUtils.signup(name, email, password);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    result.postValue(new Result.Success<>(response.body() == null? "???": response.body().get("message").toString()));
                }
                else {
                    result.postValue(new Result.Error(new Exception(response.body() == null ? "Error encountered. Please try again!" : response.body().get("message").toString())));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                result.postValue(new Result.Error(new Exception(t.getMessage())));
            }
        });


    }

    @NonNull
    public LiveData<SignupDataState> getDataValid() {
        return dataState;
    }

    public LiveData<Result<String>> getResult() {
        return result;
    }


    private boolean isNameValid(String name) {
        return name != null;
    }

    private boolean isEmailValid(String email) {
        final String email_regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email != null && email.matches(email_regex);
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.length() >= 8;
    }

    private boolean isPasswordConfirmValid(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }
}

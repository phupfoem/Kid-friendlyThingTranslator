package com.example.main.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.main.data.model.LoginDataState;
import com.example.main.data.model.Result;
import com.example.main.data.remote.MainApiUtils;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class LoginViewModel extends ViewModel {
    public final String EMAIL_ERROR_MSG = "Invalid email";
    public final String PASSWORD_ERROR_MSG = "Invalid password";

    @NonNull
    private final MutableLiveData<LoginDataState> dataState = new MutableLiveData<>();

    private final MutableLiveData<Result<String>> result = new MutableLiveData<>();
    private final MainApiUtils mainApiUtils = MainApiUtils.getInstance();

    public void loginDataChange(String email, String password) {
        if (!isEmailValid(email)) {
            dataState.setValue(new LoginDataState(EMAIL_ERROR_MSG, null));
        }
        else if (!isPasswordValid(password)) {
            dataState.setValue(new LoginDataState(null, PASSWORD_ERROR_MSG));
        }
        else {
            dataState.setValue(new LoginDataState(true));
        }
    }

    public void checkToken(String accessToken) {
        Call<JsonObject> call = mainApiUtils.checkToken(accessToken);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(new Result.Success<>(response.body().get("access_token").toString()));
                }
                else {
                    result.postValue(new Result.Error<>(new Exception(response.body() == null ? "Token expired" : response.body().get("message").toString())));
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<JsonObject> call, Throwable t) {
                result.postValue(new Result.Error<>(new Exception(t.getMessage())));
            }
        });
    }

    public void login(String email, String password) {
        Call<JsonObject> call = mainApiUtils.login(email, password);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(new Result.Success<>(response.body().get("access_token").toString()));
                }
                else {
                    result.postValue(new Result.Error<>(new Exception(response.body() == null ? "Error encountered. Please try again!" : response.body().get("message").toString())));
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<JsonObject> call, Throwable t) {
                result.postValue(new Result.Error<>(new Exception(t.getMessage())));
            }
        });
    }

    @NonNull
    public LiveData<LoginDataState> getDataValid() {
        return dataState;
    }

    public LiveData<Result<String>> getResult() {
        return result;
    }

    private boolean isEmailValid(String email) {
        final String email_regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email != null && email.matches(email_regex);
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.length() >= 8;
    }
}

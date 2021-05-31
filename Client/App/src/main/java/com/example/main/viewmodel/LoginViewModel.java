package com.example.main.viewmodel;

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

public class LoginViewModel extends ViewModel {
    public final String EMAIL_ERROR_MSG = "Invalid email";
    public final String PASSWORD_ERROR_MSG = "Invalid password";
    private MutableLiveData<LoginDataState> loginDataState = new MutableLiveData<>();
    private MutableLiveData<Result<Boolean>> loginResult = new MutableLiveData<>();
    private MainApiUtils mainApiUtils = MainApiUtils.getInstance();
    public void loginDataChange(String email, String password){
        if (!isUsernameValid(email)){
            loginDataState.setValue(new LoginDataState(EMAIL_ERROR_MSG, null));
        }
        else if (!isPasswordValid(password)){
            loginDataState.setValue(new LoginDataState(null, PASSWORD_ERROR_MSG));
        }
        else{
            loginDataState.setValue(new LoginDataState(true));
        }
    }

    public void login(String email, String password){
        Call<JsonObject> call = mainApiUtils.login(email, password);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200){
                    loginResult.postValue(new Result.Success<>(true));
                }
                else{
                    loginResult.postValue(new Result.Error(new Exception(response.body() == null ? "Đã có lỗi. Vui lòng thử lại!" : response.body().get("message").toString())));
                }
//                Log.d("LOGIN", response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loginResult.postValue(new Result.Error(new Exception(t.getMessage())));
            }
        });
    }

    public LiveData<LoginDataState> getLoginDataValid() {
        return loginDataState;
    }

    public LiveData<Result<Boolean>> getLoginResult() {
        return loginResult;
    }

    private boolean isUsernameValid(String email){
        return email != null && email.length() > 4;
    }
    private boolean isPasswordValid(String password){
        return password != null && password.length() > 4;
    }
}

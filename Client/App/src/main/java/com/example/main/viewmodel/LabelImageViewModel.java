package com.example.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.main.data.model.Result;
import com.example.main.data.remote.MainApiUtils;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class LabelImageViewModel extends ViewModel {
    private final MutableLiveData<Result<String>> result = new MutableLiveData<>();
    private final MainApiUtils mainApiUtils = MainApiUtils.getInstance();

    public void labelImage(String imageBase64) {
        Call<JsonObject> call = mainApiUtils.labelImage(imageBase64);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    result.postValue(new Result.Success<>(response.body() == null? "???": response.body().get("message").toString()));
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

    public LiveData<Result<String>> getResult() {
        return result;
    }
}

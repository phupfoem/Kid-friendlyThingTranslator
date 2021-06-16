package com.example.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.main.data.model.ImageDescription;
import com.example.main.data.model.Result;
import com.example.main.data.remote.MainApiUtils;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class LabelImageViewModel extends ViewModel {
    private final MutableLiveData<Result<ImageDescription>> result = new MutableLiveData<>();

    private final MainApiUtils mainApiUtils = MainApiUtils.getInstance();

    public void labelImage(String accessToken, String imageBase64) {
        Call<JsonObject> call = mainApiUtils.labelImage(accessToken, imageBase64);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {

                    if (response.body() == null) {
                        result.postValue(new Result.Success<>(
                                new ImageDescription(imageBase64, "???", "???"))
                        );
                    }
                    else {
                        result.postValue(new Result.Success<>(new ImageDescription(
                                imageBase64,
                                response.body().get("word").toString(),
                                response.body().get("description").toString()
                        )));
                    }
                }
                else if (response.code() == 403) {
                    if (response.body() == null) {
                        result.postValue(new Result.Error<>(new Exception(
                                "403"
                        )));
                    }
                }
                else {
                    if (response.body() == null) {
                        result.postValue(new Result.Error<>(new Exception(
                                "Error encountered. Please try again!"
                        )));
                    }
                    else {
                        result.postValue(new Result.Error<>(new Exception(
                                response.body().get("message").toString()
                        )));
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<JsonObject> call, Throwable t) {
                result.postValue(new Result.Error<>(new Exception(t.getMessage())));
            }
        });


    }

    public LiveData<Result<ImageDescription>> getResult() {
        return result;
    }
}

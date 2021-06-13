package com.example.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.main.Item;
import com.example.main.data.model.Result;
import com.example.main.data.remote.MainApiUtils;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class LabelImageViewModel extends ViewModel {
    private final MutableLiveData<Result<String>> result = new MutableLiveData<>();
    private final MainApiUtils mainApiUtils = MainApiUtils.getInstance();

    public void labelImage(String imageBase64, ArrayList<Item> items) {
        Call<JsonObject> call = mainApiUtils.labelImage(imageBase64);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    if (response.body() == null) {
                        result.postValue(new Result.Success<>("???"));
                    }
                    else {
                        result.postValue(new Result.Success<>(response.body().get("message").toString()));

                        //Add new item to history
                        items.add(new Item(
                                imageBase64,
                                response.body().get("word").toString(),
                                response.body().get("description").toString(),
                                true)
                        );
                    }
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

package com.example.main.data.remote;

import com.example.main.data.model.UserLoginSchema;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MainApiService {
    @POST("/user/login")
    Call<JsonObject> login(@Body UserLoginSchema user);
}

package com.example.main.data.remote;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MainApiService {
    @POST("/user/login")
    @FormUrlEncoded
    Call<JsonObject> login(@Field("email") String email, @Field("password") String password);
}

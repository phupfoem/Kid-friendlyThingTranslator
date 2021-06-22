package com.example.main.data.remote;

import com.example.main.data.model.UserLoginSchema;
import com.example.main.data.model.UserSignupSchema;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface MainApiService {
    @POST("/check-token")
    Call<JsonObject> checkToken(@Header("Authorization") String accessToken);

    @POST("/user/login")
    Call<JsonObject> login(@Body UserLoginSchema user);

    @POST("/user/signup")
    Call<JsonObject> signup(@Body UserSignupSchema user);

    @POST("/label-image")
    Call<JsonObject> labelImage(@Header("Authorization") String accessToken, @Body String image);
}

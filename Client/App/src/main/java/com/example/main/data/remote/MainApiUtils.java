package com.example.main.data.remote;

import com.example.main.data.model.UserLoginSchema;
import com.example.main.data.model.UserSignupSchema;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class MainApiUtils {
    private MainApiUtils(){}
    public static final String BASE_URL = "http://[2402:800:63a7:dc74:f8a8:8655:2e14:782c]:8000/";
    private static final MainApiService apiService = new Retrofit.Builder()
                                            .baseUrl(BASE_URL)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build()
                                            .create(MainApiService.class);

    private static MainApiUtils instance = null;
    public static MainApiUtils getInstance(){
        if (instance == null) {
            synchronized (MainApiUtils.class) {
                if (instance == null) {
                    instance = new MainApiUtils();
                }
            }
        }
        return instance;
    }

    @EverythingIsNonNull
    public Call<JsonObject> checkToken(String accessToken) {
        return apiService.checkToken(accessToken);
    }

    @EverythingIsNonNull
    public Call<JsonObject> login(String email, String password) {
        return apiService.login(new UserLoginSchema(email, password));
    }

    @EverythingIsNonNull
    public Call<JsonObject> signup(String name, String email, String password) {
        return apiService.signup(new UserSignupSchema(email, password, name));
    }

    @EverythingIsNonNull
    public Call<JsonObject> labelImage(String accessToken, String imageBase64) {
        return apiService.labelImage(accessToken, imageBase64);
    }
}

package com.example.main.data.remote;

import com.example.main.data.model.UserLoginSchema;
import com.example.main.data.model.UserSignupSchema;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainApiUtils {
    private MainApiUtils(){}
    public static final String BASE_URL = "http://192.168.1.11:8000/"; //Resources.getSystem().getString(R.string.base_url);
    private static final MainApiService apiService = new Retrofit.Builder()
                                            .baseUrl(BASE_URL)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build()
                                            .create(MainApiService.class);

    private static MainApiUtils instance = null;
    public static MainApiUtils getInstance(){
        if (instance == null){
            synchronized (MainApiUtils.class){
                if (instance == null){
                    instance = new MainApiUtils();
                }
            }
        }
        return instance;
    }

    public Call<JsonObject> login(String email, String password) {
        return apiService.login(new UserLoginSchema(email, password));
    }

    public Call<JsonObject> signup(String name, String email, String password) {
        return apiService.signup(new UserSignupSchema(email, password, name));
    }

    public Call<JsonObject> labelImage(String imageBase64) {
        return apiService.labelImage(imageBase64);
    }
}

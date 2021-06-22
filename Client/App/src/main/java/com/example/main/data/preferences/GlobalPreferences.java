package com.example.main.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import static android.content.Context.MODE_PRIVATE;

public class GlobalPreferences {
    public final static String PREFERENCE_NAME = "login";

    private final static String ACCESS_TOKEN = "access_token";

    @NonNull
    private final SharedPreferences sharedPreferences;

    public GlobalPreferences(@NonNull Context context, @NonNull String name) {
        sharedPreferences = context.getSharedPreferences(name, MODE_PRIVATE);
    }

    public void setAccessToken(String token) {
        sharedPreferences.edit().putString(ACCESS_TOKEN, token).apply();
    }

    public String getAccessToken() {
        return sharedPreferences.getString(ACCESS_TOKEN, "");
    }
}
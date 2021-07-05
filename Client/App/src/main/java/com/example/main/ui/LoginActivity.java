package com.example.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.main.R;
import com.example.main.data.model.Result;
import com.example.main.data.preferences.GlobalPreferences;
import com.example.main.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    GlobalPreferences globalPreferences;

    LoginViewModel loginViewModel;

    private EditText emailEditText;
    private EditText passwordEditText;

    private Button loginBtn;
    private TextView signupTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();

        addListenerToView();

        checkPacketsFromServer();

        tryLoginUsingStoredToken();
    }

    private void initialize() {
        globalPreferences = new GlobalPreferences(this, GlobalPreferences.PREFERENCE_NAME);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login);

        signupTextView = findViewById(R.id.signup);
    }

    private void addListenerToView() {
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChange(
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };

        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        loginBtn.setOnClickListener(v -> {
            loginViewModel.login(
                    emailEditText.getText().toString(),
                    passwordEditText.getText().toString());
            loginBtn.setVisibility(View.INVISIBLE);
        });

        signupTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void checkPacketsFromServer() {
        loginViewModel.getDataValid().observe(this, loginDataState -> {
            if (loginDataState == null){
                loginBtn.setEnabled(false);
                return;
            }

            if (loginDataState.getEmailError() != null || loginDataState.getPasswordError() != null) {
                if (loginDataState.getEmailError() != null) {
                    emailEditText.setError(loginDataState.getEmailError());
                }
                else {
                    passwordEditText.setError(loginDataState.getPasswordError());
                }

                loginBtn.setEnabled(false);
            }
            else {
                loginBtn.setEnabled(true);
            }
        });

        loginViewModel.getResult().observe(this, result -> {
            if (result instanceof Result.Error) {
                Toast.makeText(this, ((Result.Error<String>) result).getError().getMessage(), Toast.LENGTH_SHORT).show();
                loginBtn.setVisibility(View.VISIBLE);
            }
            else if (result instanceof Result.Success) {
                String accessToken = ((Result.Success<String>) result).getData();
                accessToken = "Bearer " + accessToken.substring(1, accessToken.length()-1);

                globalPreferences.setAccessToken(accessToken);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void tryLoginUsingStoredToken() {
        loginViewModel.checkToken(globalPreferences.getAccessToken());
    }
}
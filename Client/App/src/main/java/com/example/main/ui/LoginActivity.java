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
import com.example.main.data.preference.Preference;
import com.example.main.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    Preference preference;

    LoginViewModel loginViewModel;

    private EditText emailEditText;
    private EditText passwordEditText;

    private Button loginBtn;
    private TextView signupTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preference = new Preference(this, Preference.PREFERENCE_NAME);

        // Make a login view model
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Get email and password edit text
        emailEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);

        // Add  listener
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChange(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);


        // Get login button and login logic
        loginBtn = (Button)findViewById(R.id.login);
        loginBtn.setText(getString(R.string.login_btn));
        loginBtn.setOnClickListener(v -> {
            loginViewModel.login(emailEditText.getText().toString(),
                    passwordEditText.getText().toString());
            loginBtn.setVisibility(View.INVISIBLE);
        });

        // Check packet from server
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
            if (result instanceof Result.Error){
                Toast.makeText(this, ((Result.Error) result).getError().getMessage(), Toast.LENGTH_SHORT).show();
                loginBtn.setVisibility(View.VISIBLE);
            }
            else if (result instanceof Result.Success) {
                String accessToken = ((Result.Success) result).getData().toString();
                accessToken = accessToken.substring(1, accessToken.length()-1);

                preference.setAccessToken(accessToken);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // Get sign up button, on click, go to sign-up activity
        signupTextView = (TextView) findViewById(R.id.signup);
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }


}
package com.example.main;

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

import com.example.main.data.model.Result;
import com.example.main.viewmodel.SignupViewModel;

public class SignupActivity extends AppCompatActivity {
    SignupViewModel signupViewModel;

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;

    private TextView loginBtn;
    private Button signupBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Get edit texts
        nameEditText = (EditText)findViewById(R.id.textNameSignUp);
        passwordEditText = (EditText)findViewById(R.id.textPasswordSignUp);
        emailEditText = (EditText)findViewById(R.id.textEmailSignUp);
        passwordConfirmEditText = (EditText)findViewById(R.id.textPasswordRetypeSignUp);

        // Add  listener
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                signupViewModel.signupDataChange(
                        nameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        passwordConfirmEditText.getText().toString()
                );
            }
        };
        nameEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordConfirmEditText.addTextChangedListener(afterTextChangedListener);

        //Go back to Log in Page
        signupBtn = (Button) findViewById(R.id.btnSignUp);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupViewModel.signup(
                        nameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString()
                );
                loginBtn.setVisibility(View.INVISIBLE);
            }
        });

        // Check packet from server
        signupViewModel.getDataValid().observe(this, dataState -> {
            if (dataState == null){
                signupBtn.setEnabled(false);
                return;
            }

            if (dataState.getNameError() != null
                    || dataState.getEmailError() != null
                    || dataState.getPasswordError() != null
                    || dataState.getPasswordConfirmError() != null
            ) {
                if (dataState.getNameError() != null) {
                    nameEditText.setError(dataState.getNameError());
                }
                else if (dataState.getEmailError() != null) {
                    emailEditText.setError(dataState.getEmailError());
                }
                else if (dataState.getPasswordError() != null) {
                    passwordEditText.setError(dataState.getPasswordError());
                }
                else {
                    passwordConfirmEditText.setError(dataState.getPasswordConfirmError());
                }

                loginBtn.setEnabled(false);
            }
            else {
                loginBtn.setEnabled(true);
            }
        });
        signupViewModel.getResult().observe(this, result -> {
            if (result instanceof Result.Error){
                Toast.makeText(this, ((Result.Error) result).getError().getMessage(), Toast.LENGTH_SHORT).show();
                signupBtn.setVisibility(View.VISIBLE);
            }
            else if (result instanceof Result.Success) {
                Toast.makeText(this, ((Result.Success) result).getData().toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Get login button, on click, go back to login page
        loginBtn = (TextView)findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
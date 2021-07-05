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
import com.example.main.viewmodel.SignupViewModel;

public class SignupActivity extends AppCompatActivity {
    SignupViewModel signupViewModel;

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;

    private TextView loginTextView;
    private Button signupBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Make a view model
        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);

        // Get edit texts
        nameEditText = (EditText)findViewById(R.id.textNameSignUp);
        emailEditText = (EditText)findViewById(R.id.textEmailSignUp);
        passwordEditText = (EditText)findViewById(R.id.textPasswordSignUp);
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

        signupBtn = (Button) findViewById(R.id.btnSignUp);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupViewModel.signup(
                        nameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString()
                );
                signupBtn.setVisibility(View.INVISIBLE);
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

                signupBtn.setEnabled(false);
            }
            else {
                signupBtn.setEnabled(true);
            }
        });
        signupViewModel.getResult().observe(this, result -> {
            if (result instanceof Result.Error){
                Toast.makeText(this, ((Result.Error) result).getError().getMessage(), Toast.LENGTH_SHORT).show();
                signupBtn.setVisibility(View.VISIBLE);
            }
            else if (result instanceof Result.Success) {
                Toast.makeText(this, getString(R.string.sign_up_success_message), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Get login button, on click, go back to login page
        loginTextView = (TextView)findViewById(R.id.login);
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
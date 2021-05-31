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
import androidx.lifecycle.ViewModelProvider;

import com.example.main.data.model.Result;
import com.example.main.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    LoginViewModel loginViewModel;

    private EditText emailEditText;
    private EditText passwordEditText;

    private Button loginBtn;

    private TextView signupBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Make a login view model
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Get email and password edit text
        emailEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);

        // Add  listener
        TextWatcher afterTextChangedListener = new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChange(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);


        // Get login button and login logic
        loginBtn =(Button) findViewById(R.id.login);
        loginBtn.setText(getString(R.string.login_btn));
        loginBtn.setOnClickListener(v -> {
            loginViewModel.login(emailEditText.getText().toString(),
                    passwordEditText.getText().toString());
            loginBtn.setVisibility(View.INVISIBLE);
        });

        // Check packet from server
        loginViewModel.getLoginDataValid().observe(this, loginDataState -> {
            if (loginDataState == null){
                loginBtn.setEnabled(false);
                return;
            }
            if (loginDataState.getUsernameError() != null){
                emailEditText.setError(loginDataState.getUsernameError());
            }
            else if (loginDataState.getPasswordError() != null){
                passwordEditText.setError(loginDataState.getPasswordError());
            }
            else{
                loginBtn.setEnabled(true);
            }
        });
        loginViewModel.getLoginResult().observe(this, result ->{
            if (result instanceof Result.Error){
                Toast.makeText(this, ((Result.Error) result).getError().getMessage(), Toast.LENGTH_SHORT).show();
                loginBtn.setVisibility(View.VISIBLE);
            }
            else{
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // Get sign up button
        signupBtn = (TextView) findViewById(R.id.signup);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }


}
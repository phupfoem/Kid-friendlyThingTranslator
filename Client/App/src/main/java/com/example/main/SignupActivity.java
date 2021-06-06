package com.example.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.main.viewmodel.LoginViewModel;

public class SignupActivity extends AppCompatActivity {
    LoginViewModel loginViewModel;

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

        signupBtn = (Button) findViewById(R.id.btnSignUp);
        loginBtn = (TextView)findViewById(R.id.login);
        nameEditText = (EditText)findViewById(R.id.textNameSignUp);
        passwordEditText = (EditText)findViewById(R.id.textPasswordSignUp);
        emailEditText = (EditText)findViewById(R.id.textEmailSignUp);
        passwordConfirmEditText = (EditText)findViewById(R.id.textPasswordRetypeSignUp);


        //Go back to Log in Page
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.login(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }
}
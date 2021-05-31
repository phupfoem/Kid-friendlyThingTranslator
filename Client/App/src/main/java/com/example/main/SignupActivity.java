package com.example.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText Name, Email, Password, Confirm;
    Button btnSignUp;
    TextView textViewLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        textViewLogin = (TextView)findViewById(R.id.login);
        Name = (EditText)findViewById(R.id.textNameSignUp);
        Password = (EditText)findViewById(R.id.textPasswordSignUp);
        Email = (EditText)findViewById(R.id.textEmailSignUp);
        Confirm = (EditText)findViewById(R.id.textPasswordRetypeSignUp);


        //Go back to Log in Page

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }
//    private Boolean validateName(){
//        String val = Name.getText().toString();
//        if (val.isEmpty()){
//            Name.setError("Field cannot be empty");
//            return false;
//
//        } else{
//            Name.setError(null);
//            return true;
//        }
//    }
//    private Boolean validateEmail(){
//        String val = Email.getText().toString();
//        if (val.isEmpty()){
//            Email.setError("Field cannot be empty");
//            return false;
//        }else{
//            Email.setError(null);
//            return true;
//        }
//    }
//    private Boolean validatePassword(){
//        String val = Password.getText().toString();
//        if (val.isEmpty()){
//            Password.setError("Field cannot be empty");
//            return false;
//
//        }else{
//            Password.setError(null);
//            return true;
//        }
//    }

}
package com.example.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText Name;
    private EditText Password;
    private Button Login;
    private TextView Signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //setText
        Login =(Button) findViewById(R.id.login);
        Login.setText("LOG IN");

        Signup = (TextView) findViewById(R.id.signup);

        Name = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edit this
                if(Name.getText().toString().equals("admin") && Password.getText().toString().equals("admin")){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(),"Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }

        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }


}
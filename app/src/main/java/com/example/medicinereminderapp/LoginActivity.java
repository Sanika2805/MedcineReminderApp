package com.example.medicinereminderapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Simple login check
            if (email.equals("sanika@gmail.com") && password.equals("1234")) {

                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                // 👉 Re direct to MainActivity (Home Screen)
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                // 👉 Finish login activity (so user can't go back)
                finish();

            } else {
                Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
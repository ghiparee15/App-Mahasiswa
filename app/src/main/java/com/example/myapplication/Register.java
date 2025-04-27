package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText  etUsername, etNim, etKampus, etPassword;
    private Button btnRegister;
    private TextView tvLogin;

    private DatabaseReference database;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etNim = findViewById(R.id.etNim);
        etKampus = findViewById(R.id.etKampus);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://myapplicationuts-default-rtdb.firebaseio.com/");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String nim = etNim.getText().toString();
                String kampus = etKampus.getText().toString();
                String password = etPassword.getText().toString();

                if (username.isEmpty() || nim.isEmpty() || kampus.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User(username, nim, kampus, password);
                    database.child("users").child(nim).setValue(user);

                    Toast.makeText(getApplicationContext(), "Register berhasil", Toast.LENGTH_SHORT).show();
                    Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                    profileIntent.putExtra("nim", nim);
                    startActivity(profileIntent);
                    finish();
                }
            }
        });


        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(), Login.class);
                startActivity(login);
                Toast.makeText(getApplicationContext(), "Masukkan username dan password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
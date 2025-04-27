package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText etUsername, etNim, etPassword;
    private Button btnLogin;
    private TextView tvDaftar;
    private DatabaseReference database;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etNim = findViewById(R.id.etNim);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvDaftar = findViewById(R.id.tvDaftar);

        database = FirebaseDatabase.getInstance().getReference("users");

        // Pindah ke halaman register
        tvDaftar.setOnClickListener(v -> {
            Intent register = new Intent(getApplicationContext(), Register.class);
            startActivity(register);
        });

        // Proses login
        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            String nim = etNim.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || nim.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Lengkapi semua data login", Toast.LENGTH_SHORT).show();
            } else {
                database.child(nim).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);

                            if (user != null) {
                                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                                    Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent masuk = new Intent(getApplicationContext(), HomeActivity.class);
                                    masuk.putExtra("NIM", user.getNim());
                                    startActivity(masuk);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Username atau Password salah", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "NIM belum terdaftar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Gagal membaca database", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView tvUsername, tvNim, tvKampus;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Inisialisasi komponen UI
        tvUsername = findViewById(R.id.username);
        tvNim = findViewById(R.id.nim);
        tvKampus = findViewById(R.id.kampus);

        // Mendapatkan NIM dari Intent
        String nim = getIntent().getStringExtra("NIM");

        // Referensi ke node pengguna di Firebase
        database = FirebaseDatabase.getInstance().getReference("users").child(nim);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if (item.getItemId() == R.id.profile) {
                    intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                    intent.putExtra("NIM", nim);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.home) {
                    intent = new Intent(ProfileActivity.this, HomeActivity.class);
                    intent.putExtra("NIM", nim);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.setting) {
                    intent = new Intent(ProfileActivity.this, SettingActivity.class);
                    intent.putExtra("NIM", nim);
                    startActivity(intent);
                }
                return false;
            }
        });

        // Mengambil data pengguna dari Firebase
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    tvUsername.setText(user.getUsername());
                    tvNim.setText(user.getNim());
                    tvKampus.setText(user.getKampus());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

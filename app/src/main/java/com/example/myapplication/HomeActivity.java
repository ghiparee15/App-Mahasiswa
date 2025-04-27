package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private LinearLayout menu_jadwal, menu_tugas, menu_catatan;
    private DatabaseReference database;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        menu_jadwal = findViewById(R.id.menu_jadwal);
        menu_tugas = findViewById(R.id.menu_tugas);
        menu_catatan = findViewById(R.id.menu_catatan);

        // Mendapatkan NIM dari Intent
        String nim = getIntent().getStringExtra("NIM");

        // Referensi ke node pengguna di Firebase
        database = FirebaseDatabase.getInstance().getReference("users").child(nim);

        menu_jadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jadwal = new Intent(HomeActivity.this, Jadwal.class);
                jadwal.putExtra("NIM", nim); // Kirim NIM ke Jadwal
                startActivity(jadwal);
            }
        });

        menu_tugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tugas = new Intent(HomeActivity.this, Tugas.class);
                tugas.putExtra("NIM", nim); // Kirim NIM ke Jadwal
                startActivity(tugas);
            }
        });

        menu_catatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent catatan = new Intent(HomeActivity.this, Catatan.class);
                catatan.putExtra("NIM", nim); // Kirim NIM ke Jadwal
                startActivity(catatan);
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    return true; // Tetap di halaman Home
                } else if (item.getItemId() == R.id.setting) {
                    Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                    intent.putExtra("NIM", nim);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    intent.putExtra("NIM", nim);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }
}

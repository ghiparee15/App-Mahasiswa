package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Switch switchDarkMode;
    private TextView logout;
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "settings_prefs";
    private static final String DARK_MODE_KEY = "dark_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.setting); // tetap active highlight setting

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.profile) {
                startActivity(new Intent(SettingActivity.this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });

        switchDarkMode = findViewById(R.id.switchDarkMode);
        logout = findViewById(R.id.logout);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkModeOn = sharedPreferences.getBoolean(DARK_MODE_KEY, false);
        switchDarkMode.setChecked(isDarkModeOn);

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            sharedPreferences.edit().putBoolean(DARK_MODE_KEY, isChecked).apply();
        });

        logout.setOnClickListener(v -> {
            // Logout logic
            Toast.makeText(SettingActivity.this, "Berhasil Logout", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingActivity.this, Login.class));
            finish();
        });
    }
}

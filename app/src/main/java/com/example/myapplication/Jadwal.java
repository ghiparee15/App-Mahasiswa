package com.example.myapplication;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ScheduleAdapter;
import com.example.myapplication.Models.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Calendar;

public class Jadwal extends AppCompatActivity {

    private FloatingActionButton fabAddSchedule;
    private RecyclerView recyclerViewSchedule;
    private TextView tvEmptyMessage;

    private DatabaseReference database;
    private ArrayList<Schedule> scheduleList;
    private ScheduleAdapter scheduleAdapter;

    private String userNim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Jadwal Kuliah"); // Nama aplikasi
        }

        // Tombol kembali (Back)
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(Jadwal.this, HomeActivity.class); // Berpindah ke HomeActivity
            startActivity(intent);
        });

        fabAddSchedule = findViewById(R.id.fabAddSchedule);
        recyclerViewSchedule = findViewById(R.id.recyclerViewSchedule);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);

        userNim = getIntent().getStringExtra("NIM");
        if (userNim == null || userNim.isEmpty()) {
            Toast.makeText(this, "User tidak ditemukan!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        database = FirebaseDatabase.getInstance().getReference();

        scheduleList = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(this, scheduleList, this::deleteSchedule);

        recyclerViewSchedule.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSchedule.setAdapter(scheduleAdapter);

        loadSchedulesFromFirebase();

        fabAddSchedule.setOnClickListener(view -> showAddScheduleDialog());
    }

    private void showAddScheduleDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_schedule, null);

        EditText etSubject = dialogView.findViewById(R.id.etSubject);
        EditText etDay = dialogView.findViewById(R.id.etDay);
        EditText etTime = dialogView.findViewById(R.id.etTime);
        EditText etLecturer = dialogView.findViewById(R.id.etLecturer);
        EditText etRoom = dialogView.findViewById(R.id.etRoom);

        etTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(Jadwal.this,
                    (view, selectedHour, selectedMinute) -> {
                        String time = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                        etTime.setText(time);
                    }, hour, minute, true);

            timePickerDialog.show();
        });

        new AlertDialog.Builder(this)
                .setTitle("Tambah Jadwal")
                .setView(dialogView)
                .setPositiveButton("Tambah", (dialog, which) -> {
                    String subject = etSubject.getText().toString().trim();
                    String day = etDay.getText().toString().trim();
                    String time = etTime.getText().toString().trim();
                    String lecturer = etLecturer.getText().toString().trim();
                    String room = etRoom.getText().toString().trim();

                    if (subject.isEmpty() || day.isEmpty() || time.isEmpty() || lecturer.isEmpty() || room.isEmpty()) {
                        Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
                    } else {
                        saveScheduleToFirebase(subject, day, time, lecturer, room);
                    }
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    private void saveScheduleToFirebase(String subject, String day, String time, String lecturer, String room) {
        String scheduleId = database.child("schedules").child(userNim).push().getKey();

        if (scheduleId != null) {
            Schedule newSchedule = new Schedule(scheduleId, subject, day, time, lecturer, room);
            Map<String, Object> scheduleValues = newSchedule.toMap();

            database.child("schedules").child(userNim).child(scheduleId).setValue(scheduleValues)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Jadwal berhasil ditambahkan", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Gagal menambahkan jadwal", Toast.LENGTH_SHORT).show());
        }
    }

    private void loadSchedulesFromFirebase() {
        database.child("schedules").child(userNim).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                scheduleList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Schedule schedule = data.getValue(Schedule.class);
                    if (schedule != null) {
                        // Set ID dari Firebase key (penting untuk delete)
                        schedule.setId(data.getKey());
                        scheduleList.add(schedule);
                    }
                }

                boolean isEmpty = scheduleList.isEmpty();
                tvEmptyMessage.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                recyclerViewSchedule.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

                scheduleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Jadwal.this, "Gagal memuat jadwal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSchedule(String scheduleId) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Jadwal")
                .setMessage("Apakah Anda yakin ingin menghapus jadwal ini?")
                .setPositiveButton("Hapus", (dialog, which) -> {
                    database.child("schedules").child(userNim).child(scheduleId).removeValue()
                            .addOnSuccessListener(aVoid -> Toast.makeText(this, "Jadwal berhasil dihapus", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(this, "Gagal menghapus jadwal", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Batal", null)
                .create()
                .show();
    }

}

package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.TaskAdapter;
import com.example.myapplication.Models.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Tugas extends AppCompatActivity {

    private FloatingActionButton fabAddTask;
    private RecyclerView rvTasks;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private DatabaseReference database;
    private String nim;
    private TextView tvEmptyMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Tugas Kuliah"); // Tambahin ini!
        }

        rvTasks = findViewById(R.id.rvTasks);
        fabAddTask = findViewById(R.id.fabAddTask);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);

        taskList = new ArrayList<>();
        nim = getIntent().getStringExtra("NIM");

        taskAdapter = new TaskAdapter(this, taskList, nim, this::showAddEditTaskDialog, this::deleteTask);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        rvTasks.setAdapter(taskAdapter);

        database = FirebaseDatabase.getInstance().getReference("tasks");

        loadTasks();

        fabAddTask.setOnClickListener(v -> showAddEditTaskDialog(null));
    }

    private void loadTasks() {
        database.child(nim).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                taskList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Task task = data.getValue(Task.class);
                    if (task != null) {
                        taskList.add(task);
                    }
                }

                if (taskList.isEmpty()) {
                    rvTasks.setVisibility(View.GONE);
                    tvEmptyMessage.setVisibility(View.VISIBLE);
                } else {
                    rvTasks.setVisibility(View.VISIBLE);
                    tvEmptyMessage.setVisibility(View.GONE);
                }

                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Tugas.this, "Gagal memuat tugas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("MissingInflatedId")
    private void showAddEditTaskDialog(Task editTask) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        builder.setView(view);

        EditText etTitle = view.findViewById(R.id.etTaskTitle);
        EditText etDeadline = view.findViewById(R.id.etDeadline);
        SeekBar sbProgress = view.findViewById(R.id.progressBar);
        CheckBox cbDone = view.findViewById(R.id.cbDone);

        if (editTask != null) {
            etTitle.setText(editTask.title);
            etTitle.setEnabled(false);
            etDeadline.setText(editTask.deadline);
            sbProgress.setProgress(editTask.progress);
            cbDone.setChecked(editTask.isDone);
        }

        etDeadline.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(Tugas.this, (view1, year, month, dayOfMonth) ->
                    etDeadline.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        builder.setPositiveButton(editTask == null ? "Simpan" : "Update", (dialog, which) -> {
            String title = etTitle.getText().toString();
            String deadline = etDeadline.getText().toString();
            int progress = sbProgress.getProgress();
            boolean done = cbDone.isChecked();

            if (title.isEmpty() || deadline.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            Task task = new Task(title, deadline, progress, done);
            database.child(nim).child(title).setValue(task);

            String message = editTask == null ? "Tugas ditambahkan" : "Tugas diperbarui";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        if (editTask != null) {
            builder.setNeutralButton("Hapus", (dialog, which) -> deleteTask(editTask));
        }

        builder.setNegativeButton("Batal", null);
        builder.create().show();
    }

    private void deleteTask(Task task) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Tugas")
                .setMessage("Apakah Anda yakin ingin menghapus tugas ini?")
                .setPositiveButton("Hapus", (dialog, which) -> {
                    database.child(nim).child(task.title).removeValue()
                            .addOnSuccessListener(aVoid -> Toast.makeText(Tugas.this, "Tugas dihapus", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(Tugas.this, "Gagal menghapus tugas", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Batal", null)
                .create()
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
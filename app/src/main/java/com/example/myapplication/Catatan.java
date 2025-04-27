package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.NoteAdapter;
import com.example.myapplication.Models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Catatan extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    private FloatingActionButton fabAddNote;
    private List<Note> notes;
    private NoteAdapter adapter;
    private DatabaseReference databaseReference;
    private String nim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catatan);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // <-- tombol back
            getSupportActionBar().setTitle("Catatan");
        }

        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        fabAddNote = findViewById(R.id.fabAddNote);

        notes = new ArrayList<>();
        adapter = new NoteAdapter(this, notes, new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onEdit(Note note) {
                showNoteDialog(note);
            }

            @Override
            public void onDelete(Note note) {
                deleteNote(note);
            }
        });

        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotes.setAdapter(adapter);

        nim = getIntent().getStringExtra("NIM");
        databaseReference = FirebaseDatabase.getInstance().getReference("Catatan").child(nim);

        fabAddNote.setOnClickListener(v -> showNoteDialog(null));

        loadNotes();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // ketika tombol back di toolbar diklik
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadNotes() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notes.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Note note = dataSnapshot.getValue(Note.class);
                    if (note != null) {
                        notes.add(note);
                    }
                }
                adapter.notifyDataSetChanged();
                findViewById(R.id.tvEmptyMessage).setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
                recyclerViewNotes.setVisibility(notes.isEmpty() ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Catatan.this, "Gagal memuat data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNoteDialog(Note existingNote) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_note, null);

        EditText etTitle = view.findViewById(R.id.etTitle);
        EditText etNote = view.findViewById(R.id.etNote);

        if (existingNote != null) {
            etTitle.setText(existingNote.getTitle());
            etNote.setText(existingNote.getNote());
        }

        builder.setView(view);
        builder.setTitle(existingNote == null ? "Tambah Catatan" : "Edit Catatan");
        builder.setNegativeButton("Batal", null);
        builder.setPositiveButton("Simpan", null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {
            Button saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            saveButton.setOnClickListener(v -> {
                String title = etTitle.getText().toString().trim();
                String noteText = etNote.getText().toString().trim();

                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(noteText)) {
                    Toast.makeText(this, "Semua field harus diisi.", Toast.LENGTH_SHORT).show();
                } else {
                    if (existingNote == null) {
                        String id = databaseReference.push().getKey();
                        Note newNote = new Note(id, title, noteText);
                        databaseReference.child(id).setValue(newNote);
                    } else {
                        Note updatedNote = new Note(existingNote.getId(), title, noteText);
                        databaseReference.child(existingNote.getId()).setValue(updatedNote);
                    }
                    dialog.dismiss();
                }
            });
        });
        dialog.show();
    }

    private void deleteNote(Note note) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Catatan")
                .setMessage("Apakah Anda yakin ingin menghapus catatan ini?")
                .setPositiveButton("Hapus", (dialog, which) -> {
                    databaseReference.child(note.getId()).removeValue();
                })
                .setNegativeButton("Batal", null)
                .create()
                .show();
    }
}

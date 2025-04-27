package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Task;
import com.example.myapplication.R;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import java.util.function.Consumer;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final Context context;
    private final List<Task> taskList;
    private final String nim;
    private final Consumer<Task> onEdit;
    private final Consumer<Task> onDelete;

    public TaskAdapter(Context context, List<Task> taskList, String nim,
                       Consumer<Task> onEdit, Consumer<Task> onDelete) {
        this.context = context;
        this.taskList = taskList;
        this.nim = nim;
        this.onEdit = onEdit;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.tvTitle.setText(task.title);
        holder.tvDeadline.setText(task.deadline);
        holder.seekBarProgress.setProgress(task.progress);
        holder.cbDone.setChecked(task.isDone);

        // Menampilkan Persentase Progres
        holder.tvProgress.setText("Progres Tugas: " + task.progress + "%");

        // Update progres saat seekBar dipindah
        holder.seekBarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                task.progress = progress;
                holder.tvProgress.setText("Progres Tugas: " + progress + "%");

                // Simpan perubahan ke Firebase
                FirebaseDatabase.getInstance()
                        .getReference("tasks")
                        .child(nim)
                        .child(task.title)
                        .setValue(task);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Update done status di Firebase
        holder.cbDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.isDone = isChecked;
            FirebaseDatabase.getInstance()
                    .getReference("tasks")
                    .child(nim)
                    .child(task.title)
                    .setValue(task);
            Toast.makeText(context, isChecked ? "Tugas telah terselesaikan" : "Belum selesai", Toast.LENGTH_SHORT).show();
        });

        // Klik biasa → Edit
        holder.itemView.setOnClickListener(v -> {
            if (onEdit != null) onEdit.accept(task);
        });

        // Klik lama → Hapus
        holder.ivDelete.setOnClickListener(v -> {
            if (onDelete != null) onDelete.accept(task);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDeadline, tvProgress;
        SeekBar seekBarProgress;
        CheckBox cbDone;
        ImageView ivDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
            tvProgress = itemView.findViewById(R.id.tvProgress);
            seekBarProgress = itemView.findViewById(R.id.seekBarProgress);
            cbDone = itemView.findViewById(R.id.cbDone);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}

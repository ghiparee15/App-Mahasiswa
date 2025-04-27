package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Schedule;
import com.example.myapplication.R;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private Context context;
    private List<Schedule> scheduleList;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(String scheduleId);
    }

    public ScheduleAdapter(Context context, List<Schedule> scheduleList, OnDeleteClickListener deleteClickListener) {
        this.context = context;
        this.scheduleList = scheduleList;
        this.deleteClickListener = deleteClickListener;
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.subject.setText(schedule.getSubject());
        holder.day.setText("Hari: " + schedule.getDay());
        holder.time.setText("Waktu: " + schedule.getTime());
        holder.lecturer.setText("Dosen: " + schedule.getLecturer());
        holder.room.setText("Ruangan: " + schedule.getRoom());

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(schedule.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView subject, day, time, lecturer, room;
        ImageView btnDelete;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.tvSubject);
            day = itemView.findViewById(R.id.tvDay);
            time = itemView.findViewById(R.id.tvTime);
            lecturer = itemView.findViewById(R.id.tvLecturer);
            room = itemView.findViewById(R.id.tvRoom);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

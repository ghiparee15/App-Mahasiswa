package com.example.myapplication.Models;

public class Task {
    public String title;
    public String deadline;
    public int progress;
    public boolean isDone;

    public Task() {
        // Required for Firebase
    }

    public Task(String title, String deadline, int progress, boolean isDone) {
        this.title = title;
        this.deadline = deadline;
        this.progress = progress;
        this.isDone = isDone;
    }
}

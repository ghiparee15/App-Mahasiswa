package com.example.myapplication.Models;

public class Note {
    private String id;
    private String title;
    private String note;

    public Note() {} // Default constructor needed for Firebase

    public Note(String id, String title, String note) {
        this.id = id;
        this.title = title;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }
}

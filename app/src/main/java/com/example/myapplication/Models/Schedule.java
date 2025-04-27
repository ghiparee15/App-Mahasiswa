package com.example.myapplication.Models;

import java.util.HashMap;
import java.util.Map;

public class Schedule {
    private String id;
    private String subject;
    private String day;
    private String time;
    private String lecturer;
    private String room;

    // Konstruktor kosong diperlukan untuk Firebase
    public Schedule() {
    }

    // Konstruktor penuh
    public Schedule(String id, String subject, String day, String time, String lecturer, String room) {
        this.id = id;
        this.subject = subject;
        this.day = day;
        this.time = time;
        this.lecturer = lecturer;
        this.room = room;
    }

    // Getter dan Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    // Untuk dikirim ke Firebase
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("subject", subject);
        result.put("day", day);
        result.put("time", time);
        result.put("lecturer", lecturer);
        result.put("room", room);
        return result;
    }
}

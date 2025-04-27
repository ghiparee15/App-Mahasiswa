package com.example.myapplication.Models;

public class User {
    private String username;
    private String nim;
    private String kampus;
    private String password;
    private String fotoUrl;


    public User() {
        // Default constructor diperlukan untuk Firebase
    }

    public User(String username, String nim, String kampus, String password) {
        this.username = username;
        this.nim = nim;
        this.kampus = kampus;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getNim() {
        return nim;
    }

    public String getKampus() {
        return kampus;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public void setKampus(String kampus) {
        this.kampus = kampus;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}

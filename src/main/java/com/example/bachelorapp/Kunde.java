package com.example.bachelorapp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Kunde {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;

    private String passord;
    private String sessionId;

    public Kunde(int id, String navn, String passord, String sessionId) {
        this.id = id;
        this.username = navn;
        this.passord = passord;
        this.sessionId = sessionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String navn) {
        this.username = navn;
    }

    public String getPassord() {
        return passord;
    }

    public void setPassord(String passord) {
        this.passord = passord;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}

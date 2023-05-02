package com.example.bachelorapp;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name="KUNDE")
public class Kunde {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name  = "USERNAME")
    private String username;
    @Column(name = "PASSORD")
    private String passord;
    @Column(name = "SESSION_ID")
    private String sessionId;

    public Kunde(String navn, String passord) {
        this.username = navn;
        this.passord = passord;

    }

    public Kunde(int id, String username, String passord, String sessionId) {
        this.id = id;
        this.username = username;
        this.passord = passord;
        this.sessionId = sessionId;
    }

    public Kunde() {

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

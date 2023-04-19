package com.example.bachelorapp;

public class Historikk {
    private int id;
    private String navn;
    private String status;
    private String inventory;
    private String startTime;
    private String finnishTime;

    public Historikk(int id, String navn, String status, String inventory, String startTime, String finnishTime) {
        this.id = id;
        this.navn = navn;
        this.status = status;
        this.inventory = inventory;
        this.startTime = startTime;
        this.finnishTime = finnishTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinnishTime() {
        return finnishTime;
    }

    public void setFinnishTime(String finnishTime) {
        this.finnishTime = finnishTime;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", name='" + navn + '\'' +
                ", inventory='" + inventory + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + finnishTime + '\'' +
                '}';
    }
}

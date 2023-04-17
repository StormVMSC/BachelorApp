package com.example.bachelorapp;

import java.util.ArrayList;

public class Patch {
    private int jobId;
    private String username;
    private String password;
    private ArrayList<String> hosts;

    public Patch(int jobId, String username, String password, ArrayList<String> hosts) {
        this.jobId = jobId;
        this.username = username;
        this.password = password;
        this.hosts = hosts;
    }

    public int getJobId() {
        return jobId;
    }
    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public ArrayList<String> getHosts() { return hosts; }
    public void setHosts(ArrayList<String> hosts) { this.hosts = hosts; }


}
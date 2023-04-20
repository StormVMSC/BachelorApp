package com.example.bachelorapp;

import java.util.ArrayList;
import java.util.List;

public class Patch {
    private int jobId;
    private String username;
    private String password;
    private List<Host> hosts;

    public Patch(int jobId, String username, String password, List<Host> hosts) {
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

    public List<Host> getHosts() { return hosts; }
    public void setHosts(List<Host> hosts) { this.hosts = hosts; }

    public List<String> getAllName(){
        List<String> names = new ArrayList<String>();
        for(Host hosts : hosts ){
            names.add(hosts.getName());
        }
        return names;
    }


}

package com.example.bachelorapp;

import java.util.ArrayList;
import java.util.List;

public class Patch {
    private int jobId;
    private List<String> hosts;

    public Patch(int jobId, List<String> hosts) {
        this.jobId = jobId;
        this.hosts = hosts;
    }

    public int getJobId() {
        return jobId;
    }
    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public List<String> getHosts() { return hosts; }
    public void setHosts(List<String> hosts) { this.hosts = hosts; }

    public List<String> getAllName(){
        return hosts;
    }


}

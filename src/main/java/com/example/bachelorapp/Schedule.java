package com.example.bachelorapp;

import java.util.List;

public class Schedule {
    private int id;
    private String rrule;
    private String playbookId;
    private String navn;
    private List<String> hosts;


    public Schedule(int id, String rrule, String playbookId, String navn, List<String> hosts) {
        this.id = id;
        this.rrule = rrule;
        this.playbookId = playbookId;
        this.navn = navn;
        this.hosts = hosts;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getRrule() { return rrule; }
    public void setRrule(String rrule) { this.rrule = rrule; }

    public String getPlaybookId() { return playbookId; }
    public void setPlaybookId(String playbookId) { this.playbookId = playbookId; }

    public String getNavn() {return navn;}
    public void setNavn(String navn) {this.navn = navn;}

    public List<String> getHosts() { return hosts; }
    public void setHosts(List<String> hosts) { this.hosts = hosts; }


}

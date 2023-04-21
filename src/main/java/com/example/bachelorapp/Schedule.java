package com.example.bachelorapp;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private String playbookId;
    private String frekvens;
    private String intervall;
    private String navn;
    private List<String> hosts;
    private String dato;
    private String tid;


    public Schedule(String playbookId, String frekvens, String intervall, String navn, List<String> hosts, String dato, String tid) {
        this.playbookId = playbookId;
        this.frekvens = frekvens;
        this.intervall = intervall;
        this.navn = navn;
        this.hosts = hosts;
        this.dato = dato;
        this.tid = tid;
    }

    public String getPlaybookId() { return playbookId; }
    public void setPlaybookId(String playbookId) { this.playbookId = playbookId; }

    public String getFrekvens() {return frekvens;}
    public void setFrekvens(String frekvens) {this.frekvens = frekvens;}

    public String getIntervall() {return intervall;}
    public void setIntervall(String intervall) {this.intervall = intervall;}

    public String getNavn() {return navn;}
    public void setNavn(String navn) {this.navn = navn;}

    public List<String> getHosts() { return hosts; }
    public void setHosts(List<String> hosts) { this.hosts = hosts; }

    public String getDato() {return dato;}
    public void setDato(String dato) {this.dato = dato;}

    public String getTid() {return tid;}
    public void setTid(String tid) {this.tid = tid;}

}

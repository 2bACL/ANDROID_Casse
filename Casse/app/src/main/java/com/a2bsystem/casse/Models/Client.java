package com.a2bsystem.casse.Models;

public class Client {

    private long id;
    private String ftgnr;
    private String ftgnamn;
    private String foretagkod;

    public Client() {
    }

    public Client(String ftgnr, String ftgnamn, String foretagkod) {
        this.ftgnr = ftgnr;
        this.ftgnamn = ftgnamn;
        this.foretagkod = foretagkod;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFtgnr() {
        return ftgnr;
    }

    public void setFtgnr(String ftgnr) {
        this.ftgnr = ftgnr;
    }

    public String getFtgnamn() {
        return ftgnamn;
    }

    public void setFtgnamn(String ftgnamn) {
        this.ftgnamn = ftgnamn;
    }

    public String getForetagkod() {
        return foretagkod;
    }

    public void setForetagkod(String foretagkod) {
        this.foretagkod = foretagkod;
    }
}

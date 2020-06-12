package com.a2bsystem.casse.Models;

public class Prix {
    private long id;
    private String foretagkod;
    private String lagstalle;
    private String ftgnr;
    private String artnr;
    private String panet;
    private String pa_brut;
    private String pvc;
    private String date;

    public Prix(String foretagkod, String lagstalle, String ftgnr, String artnr, String panet, String pa_brut, String pvc, String date) {
        this.foretagkod = foretagkod;
        this.lagstalle = lagstalle;
        this.ftgnr = ftgnr;
        this.artnr = artnr;
        this.panet = panet;
        this.pa_brut = pa_brut;
        this.pvc = pvc;
        this.date = date;
    }

    public Prix() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getForetagkod() {
        return foretagkod;
    }

    public void setForetagkod(String foretagkod) {
        this.foretagkod = foretagkod;
    }

    public String getLagstalle() {
        return lagstalle;
    }

    public void setLagstalle(String lagstalle) {
        this.lagstalle = lagstalle;
    }

    public String getFtgnr() {
        return ftgnr;
    }

    public void setFtgnr(String ftgnr) {
        this.ftgnr = ftgnr;
    }

    public String getArtnr() {
        return artnr;
    }

    public void setArtnr(String artnr) {
        this.artnr = artnr;
    }

    public String getPanet() {
        return panet;
    }

    public void setPanet(String panet) {
        this.panet = panet;
    }

    public String getPa_brut() {
        return pa_brut;
    }

    public void setPa_brut(String pa_brut) {
        this.pa_brut = pa_brut;
    }

    public String getPvc() {
        return pvc;
    }

    public void setPvc(String pvc) {
        this.pvc = pvc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

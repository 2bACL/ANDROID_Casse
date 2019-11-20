package com.a2bsystem.casse.Models;

import java.io.Serializable;

public class Casse implements Serializable {


    private long id;
    private String foretagkod;
    private String lagstalle;
    private String q_2b_merch_code;
    private String ftgnr;
    private String ftgnamn;
    private String q_2b_casse_dt_reprise;
    private String q_2b_casse_heure_deb;
    private String q_2b_casse_heure_fin;
    private String status;

    public Casse() {
    }

    public Casse(String foretagkod, String lagstalle, String q_2b_merch_code, String ftgnr, String ftgnamn, String q_2b_casse_dt_reprise, String q_2b_casse_heure_deb, String q_2b_casse_heure_fin, String status) {
        this.foretagkod = foretagkod;
        this.lagstalle = lagstalle;
        this.q_2b_merch_code = q_2b_merch_code;
        this.ftgnr = ftgnr;
        this.ftgnamn = ftgnamn;
        this.q_2b_casse_dt_reprise = q_2b_casse_dt_reprise;
        this.q_2b_casse_heure_deb = q_2b_casse_heure_deb;
        this.q_2b_casse_heure_fin = q_2b_casse_heure_fin;
        this.status = status;
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

    public String getQ_2b_merch_code() {
        return q_2b_merch_code;
    }

    public void setQ_2b_merch_code(String q_2b_merch_code) {
        this.q_2b_merch_code = q_2b_merch_code;
    }

    public String getFtgnr() {
        return ftgnr;
    }

    public void setFtgnr(String ftgnr) {
        this.ftgnr = ftgnr;
    }

    public String getQ_2b_casse_dt_reprise() {
        return q_2b_casse_dt_reprise;
    }

    public void setQ_2b_casse_dt_reprise(String q_2b_casse_dt_reprise) {
        this.q_2b_casse_dt_reprise = q_2b_casse_dt_reprise;
    }

    public String getQ_2b_casse_heure_deb() {
        return q_2b_casse_heure_deb;
    }

    public void setQ_2b_casse_heure_deb(String q_2b_casse_heure_deb) {
        this.q_2b_casse_heure_deb = q_2b_casse_heure_deb;
    }

    public String getQ_2b_casse_heure_fin() {
        return q_2b_casse_heure_fin;
    }

    public void setQ_2b_casse_heure_fin(String q_2b_casse_heure_fin) {
        this.q_2b_casse_heure_fin = q_2b_casse_heure_fin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFtgnamn() {
        return ftgnamn;
    }

    public void setFtgnamn(String ftgnamn) {
        this.ftgnamn = ftgnamn;
    }
}

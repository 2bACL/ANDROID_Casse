package com.a2bsystem.casse.Models;

public class Rpr {

    private long id;
    private String foretagkod;
    private String artnr;
    private String ftgnr;
    private String q_pvc_val;
    private String artstreckkod;
    private String artnrkund;

    public Rpr() {
    }

    public Rpr(String foretagkod, String artnr, String ftgnr, String q_pvc_val, String artstreckkod, String artnrkund) {
        this.foretagkod = foretagkod;
        this.artnr = artnr;
        this.ftgnr = ftgnr;
        this.q_pvc_val = q_pvc_val;
        this.artstreckkod = artstreckkod;
        this.artnrkund = artnrkund;
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

    public String getArtnr() {
        return artnr;
    }

    public void setArtnr(String artnr) {
        this.artnr = artnr;
    }

    public String getFtgnr() {
        return ftgnr;
    }

    public void setFtgnr(String ftgnr) {
        this.ftgnr = ftgnr;
    }

    public String getQ_pvc_val() {
        return q_pvc_val;
    }

    public void setQ_pvc_val(String q_pvc_val) {
        this.q_pvc_val = q_pvc_val;
    }

    public String getArtstreckkod() {
        return artstreckkod;
    }

    public void setArtstreckkod(String artstreckkod) {
        this.artstreckkod = artstreckkod;
    }

    public String getArtnrkund() {
        return artnrkund;
    }

    public void setArtnrkund(String artnrkund) {
        this.artnrkund = artnrkund;
    }
}

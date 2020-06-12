package com.a2bsystem.casse.Models;

import java.io.Serializable;

public class ArticleCasse implements Serializable {

    private long id;
    private String foretagkod;
    private String lagstalle;
    private String q_2b_merch_code;
    private String ftgnr;
    private String q_2b_casse_dt_reprise;
    private String q_2b_casse_ligne;
    private String artnr;
    private String qte;
    private String comm;
    private String panet;
    private String pvc;
    private String trans;
    private String date;
    private String pa_brut;
    private String momskod;


    public ArticleCasse() {
    }

    public ArticleCasse(String q_2b_casse_ligne, String artnr, String qte) {
        this.q_2b_casse_ligne = q_2b_casse_ligne;
        this.artnr = artnr;
        this.qte = qte;
    }

    public ArticleCasse(String foretagkod, String lagstalle, String q_2b_merch_code, String ftgnr, String q_2b_casse_dt_reprise, String q_2b_casse_ligne, String artnr, String qte, String comm, String panet, String pa_brut, String pvc, String trans, String date, String momskod) {
        this.id = -1;
        this.foretagkod = foretagkod;
        this.lagstalle = lagstalle;
        this.q_2b_merch_code = q_2b_merch_code;
        this.ftgnr = ftgnr;
        this.q_2b_casse_dt_reprise = q_2b_casse_dt_reprise;
        this.q_2b_casse_ligne = q_2b_casse_ligne;
        this.artnr = artnr;
        this.qte = qte;
        this.comm = comm;
        this.panet = panet;
        this.pvc = pvc;
        this.trans = trans;
        this.date = date;
        this.pa_brut = pa_brut;
        this.momskod = momskod;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getArtnr() {
        return artnr;
    }

    public void setArtnr(String artnr) {
        this.artnr = artnr;
    }

    public String getQte() {
        return qte;
    }

    public void setQte(String qte) {
        this.qte = qte;
    }

    public String getQ_2b_casse_ligne() {
        return q_2b_casse_ligne;
    }

    public void setQ_2b_casse_ligne(String q_2b_casse_ligne) {
        this.q_2b_casse_ligne = q_2b_casse_ligne;
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

    public String getComm() {
        return comm;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public String getPanet() {
        return panet;
    }

    public void setPanet(String panet) {
        this.panet = panet;
    }

    public String getPvc() {
        return pvc;
    }

    public void setPvc(String pvc) {
        this.pvc = pvc;
    }

    public String getPa_brut() {
        return pa_brut;
    }

    public void setPa_brut(String pa_brut) {
        this.pa_brut = pa_brut;
    }

    public String getMomskod() {
        return momskod;
    }

    public void setMomskod(String momskod) {
        this.momskod = momskod;
    }

    @Override
    public String toString() {
        return "ArticleCasse{" +
                "id=" + id +
                ", foretagkod='" + foretagkod + '\'' +
                ", lagstalle='" + lagstalle + '\'' +
                ", q_2b_merch_code='" + q_2b_merch_code + '\'' +
                ", ftgnr='" + ftgnr + '\'' +
                ", q_2b_casse_dt_reprise='" + q_2b_casse_dt_reprise + '\'' +
                ", q_2b_casse_ligne='" + q_2b_casse_ligne + '\'' +
                ", artnr='" + artnr + '\'' +
                ", qte='" + qte + '\'' +
                ", comm='" + comm + '\'' +
                ", panet='" + panet + '\'' +
                ", pvc='" + pvc + '\'' +
                ", trans='" + trans + '\'' +
                ", date='" + date + '\'' +
                ", pa_brut='" + pa_brut + '\'' +
                ", momskod='" + momskod + '\'' +
                '}';
    }
}

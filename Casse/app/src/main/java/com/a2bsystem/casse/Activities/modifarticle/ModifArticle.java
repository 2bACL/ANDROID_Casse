package com.a2bsystem.casse.Activities.modifarticle;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.a2bsystem.casse.Database.PrixController;
import com.a2bsystem.casse.Helper;
import com.a2bsystem.casse.Models.Prix;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import com.a2bsystem.casse.Database.ArticleCasseController;
import com.a2bsystem.casse.Models.ArticleCasse;
import com.a2bsystem.casse.Models.Casse;
import com.a2bsystem.casse.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ModifArticle extends AppCompatActivity {

    private int year, month, day;
    private EditText article;
    private EditText date;
    private EditText pa_net;
    private EditText pa_brut;
    private EditText quantite;
    private EditText pvc;
    private EditText comm;
    private BottomNavigationView bottomNavigationView;
    private ArticleCasseController articleCasseController;
    private ArticleCasse articleCasse;
    private Casse casse;
    private PrixController prixController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modif_article);

        article  = findViewById(R.id.modif_artcile2);
        date     = findViewById(R.id.modif_date2);
        pa_net   = findViewById(R.id.modif_pa_net2);
        pa_brut  = findViewById(R.id.modif_pa_brut2);
        pvc      = findViewById(R.id.modif_pvc2);
        comm     = findViewById(R.id.modif_comm2);
        quantite = findViewById(R.id.modif_quantite2);
        bottomNavigationView = findViewById(R.id.bottom_navigation_valid);

        getArticleCasse();
        getCurrentCasse();

        loadArticleCasse();
        getPrix();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.valid_onglet:

                        saveArticleCasse();

                        break;

                }
                return false;
            }
        });

    }

    private void getArticleCasse(){
        Intent intent = getIntent();
        articleCasse = (ArticleCasse) intent.getSerializableExtra("articleCasse");
    }




    private void loadArticleCasse(){
        article.setText(articleCasse.getArtnr());
        date.setText(articleCasse.getDate());
        pa_net.setText(articleCasse.getPanet());
        pa_brut.setText(articleCasse.getPa_brut());
        pvc.setText(articleCasse.getPvc());
        comm.setText(articleCasse.getComm());
        quantite.setText(articleCasse.getQte());

        String date = articleCasse.getDate();
        year = Integer.parseInt(date.substring(0,date.indexOf('-')));
        date = date.substring(date.indexOf('-') + 1);
        month = Integer.parseInt(date.substring(0,date.indexOf('-')));
        date = date.substring(date.indexOf('-') + 1);
        day = Integer.parseInt(date);

        showDate(year, month, day);
    }

    private void getPrix(){
        prixController = new PrixController(this);
        prixController.open();

        List<Prix> prices = prixController.getPrixByArticleCasse(articleCasse);

        if(prices.size() == 0) {
            showError("Pas de prix disponible pour cet aricle à cette date...", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    articleCasse.setPanet("0.0");
                    pa_net.setText(articleCasse.getPanet());

                    articleCasse.setPa_brut("0.0");
                    pa_brut.setText(articleCasse.getPa_brut());
                }
            });
        }
        else {
            articleCasse.setPanet(prices.get(0).getPanet());
            pa_net.setText(articleCasse.getPanet());

            articleCasse.setPa_brut(prices.get(0).getPa_brut());
            pa_brut.setText(articleCasse.getPa_brut());
        }

        quantite.requestFocus();
    }

    private Boolean verifArticle() {
        if (date.getText().toString().equalsIgnoreCase("")){
            showError("Veuillez saisir une date", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            });
            return false;
        }
        else if (article.getText().toString().equalsIgnoreCase("")){
            showError("Veuillez saisir un article", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            });
            return false;
        }
        else if (pa_net.getText().toString().equalsIgnoreCase("")){
            showError("Veuillez saisir un prix d'achat net", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            });
            return false;
        }
        else if (pa_brut.getText().toString().equalsIgnoreCase("")){
            showError("Veuillez saisir un prix d'achat brut", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            });
            return false;
        }
        else if (quantite.getText().toString().equalsIgnoreCase("")){
            showError("Veuillez saisir une quantité", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            });
            return false;
        }
        else {
            return true;
        }
    }


    private void saveArticleCasse(){

        if(verifArticle()) {
            articleCasseController = new ArticleCasseController(this);
            articleCasseController.open();

            articleCasse.setDate(date.getText().toString());
            articleCasse.setComm(comm.getText().toString());
            articleCasse.setPvc(pvc.getText().toString());
            articleCasse.setPanet(pa_net.getText().toString());
            articleCasse.setPa_brut(pa_brut.getText().toString());
            articleCasse.setQte(quantite.getText().toString());

            if(articleCasse.getId() == 0) {
                articleCasseController.createArticleCasse(articleCasse);

                articleCasseController.close();
                showOk("Article créé", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ModifArticle.this.finish();
                    }
                });
            }
            else {
                articleCasseController.updateArticleCasse(articleCasse);

                articleCasseController.close();
                showOk("Article modifié", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ModifArticle.this.finish();
                    }
                });
            }



        }
    }

    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month - 1, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        String m = Integer.toString(month);
        String d = Integer.toString(day);
        if(month < 10){
            m = "0"+ m;
        }
        if(day < 10){
            d = "0"+ d;
        }
        date.setText(new StringBuilder().append(year).append("-")
                .append(m).append("-").append(d));
    }

    private void getCurrentCasse(){
        Intent intent = getIntent();
        casse = (Casse) intent.getSerializableExtra("casse");
    }


    public void showError(String message, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Erreur");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", listener);
        builder.show();
    }

    public void showOk(String message, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("MàJ");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", listener);
        builder.show();
    }


}


package com.a2bsystem.casse.Activities.saisiearticle;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.a2bsystem.casse.Database.ArticleCasseController;
import com.a2bsystem.casse.Database.ArticleController;
import com.a2bsystem.casse.Models.Article;
import com.a2bsystem.casse.Models.Casse;
import com.a2bsystem.casse.R;

public class SaisieManuelle extends AppCompatActivity {

    private int year, month, day;
    private EditText article;
    private EditText date;
    private EditText pa_net;
    private EditText pa_brut;
    private EditText quantite;
    private EditText pvc;
    private EditText comm;
    private Button validate;
    private ArticleCasseController articleCasseController;
    private ArticleController articleController;
    private Article articleR;
    private Casse casse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saisie_manuelle);

        getCurrentIntent();
        initFileds();
        initListeners();

    }

    private void initFileds(){
        article  = findViewById(R.id.saisie_artcile2);
        date     = findViewById(R.id.saisie_date2);
        pa_net   = findViewById(R.id.saisie_pa_net2);
        pa_brut  = findViewById(R.id.saisie_pa_brut2);
        pvc      = findViewById(R.id.saisie_pvc2);
        comm     = findViewById(R.id.saisie_comm2);
        quantite = findViewById(R.id.saisie_quantite2);
        validate = findViewById(R.id.article_casse_saisie_button);

        article.setText(articleR.getQ_gcar_lib1());
        putDate();
    }

    private void initListeners(){
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createArticleCasse();
            }
        });
    }


    private void getCurrentIntent(){
        Intent intent = getIntent();
        casse    = (Casse) intent.getSerializableExtra("casse");
        articleR = (Article) intent.getSerializableExtra("article");

    }


    private Boolean verifArticle() {
        articleController = new ArticleController(this);
        articleController.open();

        if(articleController.existArticleByLib(article.getText().toString()) <= 0 ){
            articleController.close();
            return false;
        }
        else if (date.getText().toString().equalsIgnoreCase("")){
            showError("Veuillez saisir une date", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            });
            articleController.close();
            return false;
        }
        else if (article.getText().toString().equalsIgnoreCase("")){
            showError("Veuillez saisir un article", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            });
            articleController.close();
            return false;
        }
        else if (pa_net.getText().toString().equalsIgnoreCase("")){
            showError("Veuillez saisir un prix d'achat net", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            });
            articleController.close();
            return false;
        }
        else if (pa_brut.getText().toString().equalsIgnoreCase("")){
            showError("Veuillez saisir un prix d'achat brut", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            });
            articleController.close();
            return false;
        }
        else if (quantite.getText().toString().equalsIgnoreCase("")){
            showError("Veuillez saisir une quantité", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            });
            articleController.close();
            return false;
        }
        else if(!isADate()){
            articleController.close();
            return false;
        }
        else {
            articleController.close();
            return true;
        }
    }


    private Boolean isADate(){
        return true;
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

    private Article getArticle(){
        Intent intent = getIntent();
        return (Article) intent.getSerializableExtra("article");
    }



    private void createArticleCasse(){

        articleCasseController = new ArticleCasseController(this);
        articleCasseController.open();
        if(verifArticle()){

            String casseLigne = Integer.toString((articleCasseController.getNbArticlesCasseByCasse(casse) + 1) * 10);

            articleCasseController.createArticleCasse(
                    casse.getForetagkod(),
                    casse.getLagstalle(),
                    casse.getQ_2b_merch_code(),
                    casse.getFtgnr(),
                    casse.getQ_2b_casse_dt_reprise(),
                    casseLigne,
                    getArticle().getArtnr(),
                    quantite.getText().toString(),
                    comm.getText().toString(),
                    date.getText().toString(),
                    pa_net.getText().toString(),
                    pa_brut.getText().toString(),
                    pvc.getText().toString(),
                    "CREATED",
                    getArticle().getMomskod());

            showOk("Article ajouté à la casse", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SaisieManuelle.this.finish();
                }
            });
        }
        else {
            showError("Article non ajouté à la casse", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}
            });
        }
        articleCasseController.close();
    }

    private void putDate() {

        Time now = new Time(Time.getCurrentTimezone());
        now.setToNow();
        day = now.monthDay;
        month = now.month;
        year = now.year;

        showDate(year, month + 1, day);
    }

    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
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
}

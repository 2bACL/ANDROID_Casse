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

import com.a2bsystem.casse.Helper;
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

        setGetPa();


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

    private void setGetPa() {

        articleCasseController = new ArticleCasseController(this);

        // Construction de l'URL
        RequestParams params = Helper.GenerateParams(ModifArticle.this);
        String ordberlevdat = articleCasse.getDate();
        params.put("ForetagKod",casse.getForetagkod());
        params.put("PersSign","ADM");
        params.put("CodeArticle",articleCasse.getArtnr());
        params.put("CodeClient", casse.getFtgnr());
        params.put("DateLivraison", ordberlevdat);
        String URL = Helper.GenereateURI(ModifArticle.this, params, "getpa");



        // Call API JEE
        GetPa task = new GetPa();
        task.execute(new String[] { URL });
    }


    private class GetPa extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String output = null;
            for (String url : urls) {
                output = getOutputFromUrl(url);
            }
            return output;
        }

        private String getOutputFromUrl(String url) {
            StringBuffer output = new StringBuffer("");
            try {
                InputStream stream = getHttpConnection(url);
                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(stream));
                String s = "";
                while ((s = buffer.readLine()) != null)
                    output.append(s);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return output.toString();
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }

        @Override
        protected void onPostExecute(String output) {
            System.out.println(output);
            if(output.equalsIgnoreCase("-1"))
            {
                showError("Erreur lors du chargement des prix...", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
            else {

                try {


                    JSONArray jsonArray = new JSONArray(output);

                    JSONObject currentRow = jsonArray.getJSONObject(0);

                    if(!currentRow.getString("pa_net").equalsIgnoreCase("null")){
                        articleCasse.setPanet(currentRow.getString("pa_net"));
                        pa_net.setText(articleCasse.getPanet());
                    }
                    if(!currentRow.getString("pa_brut").equalsIgnoreCase("null")){
                        articleCasse.setPa_brut(currentRow.getString("pa_brut"));
                        pa_brut.setText(articleCasse.getPa_brut());
                    }
                    quantite.requestFocus();
                    /*
                    if(!currentRow.getString("pvc").equalsIgnoreCase("null")){
                        articleCasse.setPvc(currentRow.getString("pvc"));
                        pvc.setText(articleCasse.getPvc());
                    }
                    */


                    articleCasseController.open();

                    articleCasseController.updateArticleCasse(articleCasse);

                    articleCasseController.close();

                } catch (Exception ex) {System.out.println("errrreeeuuurrrr" + ex);}



            }
        }
    }
}


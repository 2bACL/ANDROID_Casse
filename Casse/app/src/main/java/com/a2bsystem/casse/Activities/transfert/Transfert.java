package com.a2bsystem.casse.Activities.transfert;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.a2bsystem.casse.Activities.config.Configuration;
import com.a2bsystem.casse.Helper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.a2bsystem.casse.Activities.listecasses.ListeCasses;
import com.a2bsystem.casse.Database.ArticleCasseController;
import com.a2bsystem.casse.Database.CasseController;
import com.a2bsystem.casse.Models.ArticleCasse;
import com.a2bsystem.casse.Models.Casse;
import com.a2bsystem.casse.R;

import static com.a2bsystem.casse.Activities.config.Config.Config;

public class Transfert extends AppCompatActivity {

    private TextView mText;
    BottomNavigationView bottomNavigationView;

    private String Foretagkod;
    private String Merchandiser;
    private String Depot;
    private int nbCasses;

    private CasseController casseController;
    private ArticleCasseController articleCasseController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfert);
        this.configureToolbar();

        initFields();
        initListeners();
        getPrefs();
        setText();
        initButton();
    }

    private void getPrefs() {
        SharedPreferences sharedPreferences = this.getApplicationContext().getSharedPreferences(Config, 0);
        Merchandiser = sharedPreferences.getString("Merchandiser", "");
        Depot = sharedPreferences.getString("Depot", "");
        Foretagkod = sharedPreferences.getString("Foretagkod","");
    }

    private void initFields() {
        mText        = findViewById(R.id.transfert_text);
        bottomNavigationView = findViewById(R.id.bottom_navigation_transfert);
    }

    private void initListeners() {


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.transfert:
                        if (nbCasses > 0 ){
                            final Toast success = Toast.makeText(Transfert.this, "Transfert en cours...",
                                    Toast.LENGTH_LONG);

                            success.show();
                            transferArticles();
                            transferCasses();
                            showOk("Transfert complété", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    success.cancel();
                                    Intent Transfert = new Intent(Transfert.this, Transfert.class);
                                    Transfert.this.finish();
                                    startActivity(Transfert);
                                }
                            });
                        }

                        break;

                }
                return false;
            }
        });
    }

    private void transferArticles() {
        articleCasseController = new ArticleCasseController(this);
        articleCasseController.open();

        casseController = new CasseController(this);
        casseController.open();

        final ArrayList<Casse> casses = new ArrayList<>();
        casses.addAll(casseController.getAllCasses());

        final ArrayList<ArticleCasse> articlesCasse = new ArrayList<>();
        for( int i = 0 ; i < casses.size() ; i++ ){
            Casse casse = casses.get(i);
            articlesCasse.clear();
            articlesCasse.addAll(articleCasseController.getArticlesCasseByCasse(casse));

            for( int j = 0 ; j < articlesCasse.size() ; j++ ){
                ArticleCasse articleCasse = articlesCasse.get(j);
                articleCasse.setTrans("TRANSFERED");
                articleCasseController.updateArticleCasse(articleCasse);
                setAddArticle(articlesCasse.get(j));
            }

        }
        casseController.close();

        articleCasseController.close();
    }

    private void transferCasses() {
        casseController = new CasseController(this);
        casseController.open();

        final ArrayList<Casse> casses = new ArrayList<>();
        casses.addAll(casseController.getAllCasses());

        for( int i = 0 ; i < casses.size() ; i++ ){
            Casse casse = casses.get(i);
            casse.setStatus("TRANSFERED");
            casseController.updateCasse(casse);
            setCreateCasse(casse);
        }
        casseController.close();
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

    private void setText() {
        casseController = new CasseController(this);
        casseController.open();

        nbCasses = casseController.getNbCassetoTransfer();

        if(nbCasses > 1){
            mText.setText(nbCasses + " Casses magasin à tranférer");
        }
        else {
            mText.setText(nbCasses + " Casse magasin à tranférer");
        }
    }

    private void initButton() {
        if (nbCasses > 0 ){
            bottomNavigationView.setEnabled(true);
            bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        else {
            bottomNavigationView.setEnabled(false);
            bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.grey));
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    private void configureToolbar(){
        Toolbar toolbar = findViewById(R.id.activity_transfert_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TRANSFERT CASSE");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_activity_conf:

                Intent ConfigActivity = new Intent(Transfert.this, Configuration.class);
                Transfert.this.finish();
                startActivity(ConfigActivity);

                return true;
            case R.id.menu_activity_casse:

                Intent MainActivity = new Intent(Transfert.this, ListeCasses.class);
                Transfert.this.finish();
                startActivity(MainActivity);

                return true;
            case R.id.menu_activity_transf:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setCreateCasse(Casse casse) {
        // Construction de l'URL
        RequestParams params = Helper.GenerateParams(Transfert.this);
        String h_debut = "1900-01-01 "+ casse.getQ_2b_casse_heure_deb() + ":00.000";
        String h_fin   = "1900-01-01 "+ casse.getQ_2b_casse_heure_fin() + ":00.000";

        params.put("ForetagKod",Foretagkod);
        params.put("ftgnr",casse.getFtgnr());
        params.put("lagstalle",Depot);
        params.put("q_2b_casse_dt_reprise",casse.getQ_2b_casse_dt_reprise()+ " 00:00:00.000");
        params.put("q_2b_merch_code",Merchandiser);
        params.put("q_2b_casse_heure_deb", h_debut);
        params.put("q_2b_casse_heure_fin", h_fin);
        params.put("q_2b_casse_avoir", "0");
        String URL = Helper.GenereateURI(Transfert.this, params, "createcasse");


        // Call API JEE
        CreateCasse task = new CreateCasse();
        task.execute(new String[] { URL });
    }

    private void setAddArticle(ArticleCasse articleCasse) {
        // Construction de l'URL
        RequestParams params = Helper.GenerateParams(Transfert.this);
        String ordberlevdat = articleCasse.getDate() + " 00:00:00.000" ;
        params.put("ForetagKod",Foretagkod);
        params.put("artnr",articleCasse.getArtnr());
        params.put("momskod",articleCasse.getMomskod());
        params.put("q_2b_casse_dt_reprise",articleCasse.getQ_2b_casse_dt_reprise()+ " 00:00:00.000");
        params.put("ordberlevdat", ordberlevdat);
        params.put("q_2b_art_divers", "0");
        params.put("q_2b_casse_ligne", articleCasse.getQ_2b_casse_ligne());
        params.put("q_2b_observation", articleCasse.getComm());
        params.put("q_2b_qte_reprise", articleCasse.getQte());
        params.put("q_pvc_val", articleCasse.getPvc());
        params.put("ftgnr", articleCasse.getFtgnr());
        params.put("lagstalle", Depot);
        params.put("q_2b_merc_code", Merchandiser);
        params.put("q_2b_casse_pa_brut", articleCasse.getPanet());
        params.put("q_2b_casse_pa_net", articleCasse.getPanet());
        String URL = Helper.GenereateURI(Transfert.this, params, "addarticle");


        // Call API JEE
        AddArticle task = new AddArticle();
        task.execute(new String[] { URL });
    }


    private class AddArticle extends AsyncTask<String, Void, String> {
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
            System.out.println("aaaaaaaaa " +output);
            if(output.equalsIgnoreCase("-1"))
            {
                showError("Problème de transfert d'article...", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }

        }
    }


    private class CreateCasse extends AsyncTask<String, Void, String> {
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
            if(output.equalsIgnoreCase("-1"))
            {
                showError("Problème de transfert...", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
        }
    }

}

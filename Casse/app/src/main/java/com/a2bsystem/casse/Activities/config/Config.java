package com.a2bsystem.casse.Activities.config;

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
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.a2bsystem.casse.Activities.listecasses.ListeCasses;
import com.a2bsystem.casse.Activities.transfert.Transfert;
import com.a2bsystem.casse.Helper;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.a2bsystem.casse.Database.ArticleController;
import com.a2bsystem.casse.Database.ClientController;
import com.a2bsystem.casse.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Config extends AppCompatActivity {

    public static final String Config = "Config";
    public static final String url = "URL";
    public static final String bdd = "BDD";
    public static final String Merchandiser = "Merchandiser";
    public static final String foretagkod = "Foretagkod";
    public static final String Depot = "Depot";
    SharedPreferences sharedPreferences;

    private EditText mUrl;
    private EditText mBdd;
    private EditText mForetagkod;
    private EditText mMerchandiser;
    private EditText mDepot;
    ProgressBar pbbar;

    private ClientController clientController;
    private ArticleController articleController;

    Toast successArticles;
    Toast successClients;


    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        initFields();
        initListeners();
        this.configureToolbar();

        successArticles = Toast.makeText(Config.this, "Chargement des articles...",
                Toast.LENGTH_LONG);

        successClients = Toast.makeText(Config.this, "Chargement des clients...",
                Toast.LENGTH_LONG);
    }

    public void initFields() {

        mUrl          = findViewById(R.id.config_url);
        mBdd      = findViewById(R.id.config_bdd);
        mMerchandiser = findViewById(R.id.config_merchandiser);
        mForetagkod   = findViewById(R.id.config_foretagkod);
        mDepot        = findViewById(R.id.config_depot);
        pbbar = findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);


        sharedPreferences = getBaseContext().getSharedPreferences(Config,MODE_PRIVATE);

        bottomNavigationView = findViewById(R.id.bottom_navigation_config);

        //Rempli avec les données existantes
        if(sharedPreferences.contains(url)
                && sharedPreferences.contains(foretagkod)
                && sharedPreferences.contains(bdd)
                && sharedPreferences.contains(Merchandiser)
                && sharedPreferences.contains(Depot))
        {
            mForetagkod.setText(sharedPreferences.getString(foretagkod,null));
            mUrl.setText(sharedPreferences.getString(url,null));
            mBdd.setText(sharedPreferences.getString(bdd,null));
            mMerchandiser.setText(sharedPreferences.getString(Merchandiser,null));
            mDepot.setText(sharedPreferences.getString(Depot,null));
        }
    }

    public void initListeners() {

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.config_valid_onglet:

                        sharedPreferences
                                .edit()
                                .putString(foretagkod,mForetagkod.getText().toString())
                                .putString(url,mUrl.getText().toString())
                                .putString(bdd,mBdd.getText().toString())
                                .putString(Merchandiser,mMerchandiser.getText().toString())
                                .putString(Depot,mDepot.getText().toString())
                                .apply();

                        Config.this.finish();

                        break;

                    case R.id.config_maj_articles:

                        sharedPreferences
                                .edit()
                                .putString(foretagkod,mForetagkod.getText().toString())
                                .putString(url,mUrl.getText().toString())
                                .putString(bdd,mBdd.getText().toString())
                                .putString(Merchandiser,mMerchandiser.getText().toString())
                                .putString(Depot,mDepot.getText().toString())
                                .apply();

                        setGetArticles();

                        break;

                    case R.id.config_maj_clients:

                        sharedPreferences
                                .edit()
                                .putString(foretagkod,mForetagkod.getText().toString())
                                .putString(url,mUrl.getText().toString())
                                .putString(bdd,mBdd.getText().toString())
                                .putString(Merchandiser,mMerchandiser.getText().toString())
                                .putString(Depot,mDepot.getText().toString())
                                .apply();

                        setGetClients();

                        break;
                }
                return false;
            }
        });

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


    private void unLockUI(Boolean bool){
        bottomNavigationView.setEnabled(bool);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    private void configureToolbar(){
        Toolbar toolbar = findViewById(R.id.activity_config_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CONFIGURATION");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_activity_conf:
                return true;
            case R.id.menu_activity_casse:

                Intent MainActivity = new Intent(com.a2bsystem.casse.Activities.config.Config.this, ListeCasses.class);
                com.a2bsystem.casse.Activities.config.Config.this.finish();
                startActivity(MainActivity);

                return true;
            case R.id.menu_activity_transf:

                Intent TransfertActivity = new Intent(com.a2bsystem.casse.Activities.config.Config.this, Transfert.class);
                com.a2bsystem.casse.Activities.config.Config.this.finish();
                startActivity(TransfertActivity);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setGetArticles() {

        articleController = new ArticleController(this);

        RequestParams params = Helper.GenerateParams(Config.this);

        String URL = Helper.GenereateURI(Config.this, params, "getarticles");

        //Verouillage de l'interface
        unLockUI(false);

        // Call API JEE

        successArticles.show();
        pbbar.setVisibility(View.VISIBLE);

        GetArticles task = new GetArticles();
        task.execute(new String[] { URL });
    }

    private void setGetClients() {

        clientController = new ClientController(this);

        RequestParams params = Helper.GenerateParams(Config.this);
        String URL = Helper.GenereateURI(Config.this, params, "getclients");

        //Verouillage de l'interface
        unLockUI(false);

        // Call API JEE
        successClients.show();
        pbbar.setVisibility(View.VISIBLE);

        GetClients task = new GetClients();
        task.execute(new String[] { URL });
    }


    private class GetArticles extends AsyncTask<String, Void, String> {
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
            unLockUI(true);
            successArticles.cancel();
            pbbar.setVisibility(View.GONE);
            if(output.equalsIgnoreCase("-1"))
            {
                showError("Erreur lors du chargement des articles1...", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
            else {

                try {

                    JSONArray jsonArray = new JSONArray(output);

                    articleController.open();
                    articleController.deleteAll();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject currentRow = jsonArray.getJSONObject(i);
                        articleController.createArticle(currentRow.getString("artnr"),
                                currentRow.getString("momskod"),
                                currentRow.getString("q_gcar_lib1"));
                    }
                    articleController.close();

                    showOk("Mise à jour des articles réussie", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });



                } catch (JSONException e) {
                    showError("Erreur lors du chargement des articles...", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { }
                    });
                    e.printStackTrace();
                }
            }
        }
    }


    private class GetClients extends AsyncTask<String, Void, String> {
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
            unLockUI(true);
            successArticles.cancel();
            pbbar.setVisibility(View.GONE);
            if(output.equalsIgnoreCase("-1"))
            {
                showError("Erreur lors du chargement des clients...", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
            else {

                try {

                    JSONArray jsonArray = new JSONArray(output);

                    clientController.open();
                    clientController.deleteAll();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject currentRow = jsonArray.getJSONObject(i);
                        clientController.createClient(foretagkod,
                                currentRow.getString("FtgNr"),
                                currentRow.getString("FtgNamn"));
                    }
                    showOk("Mise à jour des clients réussie", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { }
                    });
                    clientController.close();



                } catch (JSONException e) {
                    showError("Erreur lors du chargement des clients...", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { }
                    });
                    e.printStackTrace();
                }
            }
        }
    }

}

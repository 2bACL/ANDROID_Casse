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
import com.a2bsystem.casse.Activities.modifarticle.ModifArticle;
import com.a2bsystem.casse.Activities.transfert.Transfert;
import com.a2bsystem.casse.Database.ArticleCasseController;
import com.a2bsystem.casse.Database.PrixController;
import com.a2bsystem.casse.Helper;
import com.a2bsystem.casse.Models.Prix;
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

public class Configuration extends AppCompatActivity {

    public static final String Config = "Config";
    public static final String Merchandiser = "Merchandiser";
    public static final String Depot = "Depot";
    SharedPreferences sharedPreferences;

    private EditText mMerchandiser;
    private EditText mDepot;
    ProgressBar pbbar;

    private ClientController clientController;
    private ArticleController articleController;
    private PrixController prixController;

    Toast successArticles;
    Toast successClients;
    Toast successSave;


    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);
        initFields();
        initListeners();
        this.configureToolbar();

        successArticles = Toast.makeText(Configuration.this, "Chargement des articles...",
                Toast.LENGTH_LONG);

        successClients = Toast.makeText(Configuration.this, "Chargement des clients...",
                Toast.LENGTH_LONG);

        successSave = Toast.makeText(Configuration.this, "Configuration sauvegardée",
                Toast.LENGTH_LONG);
    }

    public void initFields() {

        mMerchandiser = findViewById(R.id.config_merchandiser);
        mDepot        = findViewById(R.id.config_depot);
        pbbar = findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);


        sharedPreferences = getBaseContext().getSharedPreferences(Config,MODE_PRIVATE);

        bottomNavigationView = findViewById(R.id.bottom_navigation_configuration);

        //Rempli avec les données existantes
        if(sharedPreferences.contains(Merchandiser)
                && sharedPreferences.contains(Depot))
        {
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
                                .putString(Merchandiser,mMerchandiser.getText().toString())
                                .putString(Depot,mDepot.getText().toString())
                                .apply();

                        successSave.show();

                        break;

                    case R.id.config_maj_articles:

                        sharedPreferences
                                .edit()
                                .putString(Merchandiser,mMerchandiser.getText().toString())
                                .putString(Depot,mDepot.getText().toString())
                                .apply();

                        setGetArticles();

                        break;

                    case R.id.config_maj_clients:

                        sharedPreferences
                                .edit()
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
        Toolbar toolbar = findViewById(R.id.activity_configuration_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CONFIGURATION");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_activity_conf:
                return true;
            case R.id.menu_activity_casse:

                Intent MainActivity = new Intent(Configuration.this, ListeCasses.class);
                Configuration.this.finish();
                startActivity(MainActivity);

                return true;
            case R.id.menu_activity_transf:

                Intent TransfertActivity = new Intent(Configuration.this, Transfert.class);
                Configuration.this.finish();
                startActivity(TransfertActivity);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setGetArticles() {

        articleController = new ArticleController(this);

        RequestParams params = Helper.GenerateParams(Configuration.this);

        String URL = Helper.GenereateURI(Configuration.this, params, "getarticles");

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

        RequestParams params = Helper.GenerateParams(Configuration.this);
        String URL = Helper.GenereateURI(Configuration.this, params, "getclients");

        //Verouillage de l'interface
        unLockUI(false);

        // Call API JEE
        successClients.show();
        pbbar.setVisibility(View.VISIBLE);

        GetClients task = new GetClients();
        task.execute(new String[] { URL });
    }


    private void setGetPa() {

        prixController = new PrixController(this);

        // Construction de l'URL
        RequestParams params = Helper.GenerateParams(Configuration.this);
        params.put("ForetagKod","9999");
        params.put("PersSign","ADM");
        params.put("Lagstalle",mDepot.getText().toString());
        String URL = Helper.GenereateURI(Configuration.this, params, "getpa");



        // Call API JEE
        GetPa task = new GetPa();
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
            if(output.equalsIgnoreCase("-1"))
            {
                showError("Erreur lors du chargement des articles...", new DialogInterface.OnClickListener() {
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

                    setGetPa();



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

                    SharedPreferences sharedPreferences = getSharedPreferences(Config, 0);
                    String Foretagkod = sharedPreferences.getString("URL",null);

                    JSONArray jsonArray = new JSONArray(output);

                    clientController.open();
                    clientController.deleteAll();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject currentRow = jsonArray.getJSONObject(i);
                        clientController.createClient(Foretagkod,
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
            unLockUI(true);
            successArticles.cancel();
            pbbar.setVisibility(View.GONE);
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

                    prixController.open();
                    prixController.deleteAll();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject currentRow = jsonArray.getJSONObject(i);

                        Prix prix = new Prix(currentRow.getString("FORETAGKOD"),
                                currentRow.getString("LAGSTALLE"),
                                currentRow.getString("FTGNR"),
                                currentRow.getString("ARTNR"),
                                currentRow.getString("PANET"),
                                currentRow.getString("PABRUT"),
                                currentRow.getString("PVC"),
                                currentRow.getString("DATE").substring(0,10)
                        );

                        prixController.createPrix(prix);
                    }
                    showOk("Mise à jour des articles réussie", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { }
                    });
                    prixController.close();



                } catch (JSONException e) {
                    showError("Erreur lors du chargement des prix...", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { }
                    });
                    e.printStackTrace();
                }
            }
        }
    }

}

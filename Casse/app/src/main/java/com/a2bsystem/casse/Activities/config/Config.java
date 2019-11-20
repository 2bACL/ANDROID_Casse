package com.a2bsystem.casse.Activities.config;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import com.a2bsystem.casse.Activities.listecasses.ListeCasses;
import com.a2bsystem.casse.Activities.transfert.Transfert;
import com.a2bsystem.casse.Database.ArticleController;
import com.a2bsystem.casse.Database.ClientController;
import com.a2bsystem.casse.R;
import com.a2bsystem.casse.http.Helper;

public class Config extends AppCompatActivity {

    public static final String Config = "Config";
    public static final String Url = "Url";
    public static final String ApiNode = "ApiNode";
    public static final String Merchandiser = "Merchandiser";
    public static final String Foretagkod = "Foretagkod";
    public static final String Depot = "Depot";
    SharedPreferences sharedPreferences;

    private EditText mUrl;
    private EditText mApiNode;
    private EditText mForetagkod;
    private EditText mMerchandiser;
    private EditText mDepot;

    private Button mValidButton;
    private Button mClients;
    private Button mArticles;

    private ClientController clientController;
    private ArticleController articleController;


    AsyncHttpClient client = new AsyncHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        this.configureToolbar();

        initFields();
        initListeners();
    }

    public void initFields() {

        mUrl          = findViewById(R.id.config_url2);
        mApiNode      = findViewById(R.id.config_api_node2);
        mMerchandiser = findViewById(R.id.config_merchandiser2);
        mForetagkod   = findViewById(R.id.config_foretagkod2);
        mDepot        = findViewById(R.id.config_depot2);

        mValidButton  = findViewById(R.id.config_valid_button);
        mClients      = findViewById(R.id.clients_button);
        mArticles     = findViewById(R.id.articles_button);

        sharedPreferences = getBaseContext().getSharedPreferences(Config,MODE_PRIVATE);

        //Rempli avec les données existantes
        if(sharedPreferences.contains(Url)
                && sharedPreferences.contains(Foretagkod)
                && sharedPreferences.contains(ApiNode)
                && sharedPreferences.contains(Merchandiser)
                && sharedPreferences.contains(Depot))
        {
            mForetagkod.setText(sharedPreferences.getString(Foretagkod,null));
            mUrl.setText(sharedPreferences.getString(Url,null));
            mApiNode.setText(sharedPreferences.getString(ApiNode,null));
            mMerchandiser.setText(sharedPreferences.getString(Merchandiser,null));
            mDepot.setText(sharedPreferences.getString(Depot,null));
        }
    }

    public void initListeners() {
        mValidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences
                        .edit()
                        .putString(Foretagkod,mForetagkod.getText().toString())
                        .putString(Url,mUrl.getText().toString())
                        .putString(ApiNode,mApiNode.getText().toString())
                        .putString(Merchandiser,mMerchandiser.getText().toString())
                        .putString(Depot,mDepot.getText().toString())
                        .apply();

                Intent ListeCasses = new Intent(Config.this, ListeCasses.class);
                startActivity(ListeCasses);
            }
        });

        mClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences
                        .edit()
                        .putString(Foretagkod,mForetagkod.getText().toString())
                        .putString(Url,mUrl.getText().toString())
                        .putString(ApiNode,mApiNode.getText().toString())
                        .putString(Merchandiser,mMerchandiser.getText().toString())
                        .putString(Depot,mDepot.getText().toString())
                        .apply();

                getClients();
            }
        });

        mArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences
                        .edit()
                        .putString(Foretagkod,mForetagkod.getText().toString())
                        .putString(Url,mUrl.getText().toString())
                        .putString(ApiNode,mApiNode.getText().toString())
                        .putString(Merchandiser,mMerchandiser.getText().toString())
                        .putString(Depot,mDepot.getText().toString())
                        .apply();

                getArticles();
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


    private void getClients() {

        final Toast success = Toast.makeText(Config.this, "Chargement des clients...",
                Toast.LENGTH_LONG);

        clientController = new ClientController(this);

        RequestParams params = Helper.GenerateParams(this,"GetClients");

        client.setSSLSocketFactory(new SSLSocketFactory(Helper.getSslContext(), SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));
        client.get(Helper.GenereateURI(this, params), new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                success.show();
                unLockUI(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                unLockUI(true);

                if(!Helper.GetSuccess(response)) {
                    success.cancel();
                    showError("Erreur lors du chargement des clients", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });
                }
                else {
                    try {

                        JSONArray jsonArray = Helper.GetList(response);

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
                        success.cancel();
                        clientController.close();


                    } catch (JSONException e) {
                        success.cancel();
                        showError("Erreur lors du chargement des clients2", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        });
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                unLockUI(true);
                success.cancel();
                showError("Erreur lors du chargement des clients", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
            }

            @Override public void onRetry(int retryNo) { }
        });
    }



    private void getArticles() {

        final Toast success = Toast.makeText(Config.this, "Chargement des articles...",
                Toast.LENGTH_LONG);

        articleController = new ArticleController(this);

        RequestParams params = Helper.GenerateParams(this,"GetArticles");

        client.setSSLSocketFactory(new SSLSocketFactory(Helper.getSslContext(), SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));
        client.get(Helper.GenereateURI(this, params), new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                success.show();
                unLockUI(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                unLockUI(true);

                if(!Helper.GetSuccess(response)) {
                    success.cancel();
                    showError("Erreur lors du chargement des articles", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });
                }
                else {
                    try {

                        JSONArray jsonArray = Helper.GetList(response);

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
                        success.cancel();


                    } catch (JSONException e) {
                        success.cancel();
                        showError("Erreur lors du chargment des articles", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        });
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                unLockUI(true);
                success.cancel();
                showError("Erreur lors du chargment des articles", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
            }

            @Override public void onRetry(int retryNo) { }
        });
    }

    private void unLockUI(Boolean bool){
        mValidButton.setEnabled(bool);
        mClients.setEnabled(bool);
        mArticles.setEnabled(bool);
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
}

package com.a2bsystem.casse.Activities.saisiearticle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import com.a2bsystem.casse.Activities.modifarticle.ModifArticle;
import com.a2bsystem.casse.Database.ArticleCasseController;
import com.a2bsystem.casse.Database.ArticleController;
import com.a2bsystem.casse.Models.ArticleCasse;
import com.a2bsystem.casse.Models.Casse;
import com.a2bsystem.casse.R;
import com.a2bsystem.casse.http.Helper;

public class SaisieArticle extends AppCompatActivity {

    private Button manuelle;
    private Button scan;
    private Casse casse;

    private IntentIntegrator qrScan;

    private ArticleCasseController articleCasseController;
    private ArticleController articleController;

    private ArticleCasse articleCasse;

    AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saisie_article);

        getCurrentCasse();

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        initFields();
        initListeners();
    }

    private void initFields() {
        manuelle = findViewById(R.id.saisie_manu);
        scan     = findViewById(R.id.saisie_qr);
    }

    private void initListeners() {
        manuelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ChoixArticle = new Intent(SaisieArticle.this, ChoixArticle.class);
                ChoixArticle.putExtra("casse", casse);
                SaisieArticle.this.finish();
                startActivity(ChoixArticle);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //initiating the qr code scan
                qrScan.initiateScan();
            }
        });
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {

                    articleCasseController = new ArticleCasseController(this);
                    articleCasseController.open();

                    articleCasse = new ArticleCasse();
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());

                    System.out.println(obj.toString());

                    String casseLigne = Integer.toString((articleCasseController.getNbArticlesCasseByCasse(casse) + 1) * 10);

                    articleCasse.setForetagkod(casse.getForetagkod());
                    articleCasse.setLagstalle(casse.getLagstalle());
                    articleCasse.setQ_2b_merch_code(casse.getQ_2b_merch_code());
                    articleCasse.setQ_2b_casse_dt_reprise(casse.getQ_2b_casse_dt_reprise());
                    articleCasse.setFtgnr(casse.getFtgnr());
                    articleCasse.setQ_2b_casse_ligne(casseLigne);
                    articleCasse.setTrans("CREATED");
                    articleCasse.setMomskod(getMomsKod(obj.getString("artnr")));
                    articleCasse.setDate((obj.getString("date")));
                    articleCasse.setArtnr((obj.getString("artnr")));
                    articleCasse.setQte("");
                    articleCasse.setComm("");
                    articleCasse.setPa_brut("");
                    articleCasse.setPanet("");
                    articleCasse.setPvc((obj.getString("pvc")));

                    //getPrice();

                    articleCasseController.createArticleCasse(articleCasse);


                    Intent ModifArticleActivity = new Intent(SaisieArticle.this, ModifArticle.class);
                    ModifArticleActivity.putExtra("articleCasse",articleCasse);
                    ModifArticleActivity.putExtra("casse",casse);
                    SaisieArticle.this.finish();
                    startActivity(ModifArticleActivity);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "QRCode non valide", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getCurrentCasse(){
        Intent intent = getIntent();
        casse = (Casse) intent.getSerializableExtra("casse");
    }

    private String getMomsKod(String artnr){
        articleController = new ArticleController(this);
        articleController.open();
        String momskod = articleController.getArticleByArtnr(artnr).getMomskod();

        articleController.close();

        return momskod;
    }

    private void getPrice() {

        RequestParams params = Helper.GenerateParams(this,"GetPa");

        String ordberlevdat = articleCasse.getDate();
        params.put("ForetagKod",casse.getForetagkod());
        params.put("PersSign","ADM");
        params.put("CodeArticle",articleCasse.getArtnr());
        params.put("CodeClient", casse.getFtgnr());
        params.put("DateLivraison", ordberlevdat);

        client.setSSLSocketFactory(new SSLSocketFactory(Helper.getSslContext(), SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));
        client.get(Helper.GenereateURI(this, params), new AsyncHttpResponseHandler() {

            @Override
            public void onStart() { }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                if(!Helper.GetSuccess(response)) {
                showError("Erreur lors du chargement des prix", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
            }
            else {
                try {

                    JSONArray jsonArray = Helper.GetList(response);

                    JSONObject currentRow = jsonArray.getJSONObject(0);

                    articleCasse.setPa_brut(currentRow.getString("pa_brut"));
                    articleCasse.setPanet(currentRow.getString("pa_net"));
                    articleCasse.setPvc(currentRow.getString("pvc"));

                    articleCasseController.updateArticleCasse(articleCasse);
                    articleCasseController.close();


                } catch (JSONException e) {
                    showError("Erreur lors du chargement des prix", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { }
                    });
                    e.printStackTrace();
                }
            } }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) { }

            @Override public void onRetry(int retryNo) { }
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
}

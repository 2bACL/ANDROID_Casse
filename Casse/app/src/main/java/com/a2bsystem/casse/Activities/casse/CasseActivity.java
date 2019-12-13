package com.a2bsystem.casse.Activities.casse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.a2bsystem.casse.Activities.modifarticle.ModifArticle;
import com.a2bsystem.casse.Activities.saisiearticle.SaisieArticle;
import com.a2bsystem.casse.Adapters.ArticleCasseAdapter;
import com.a2bsystem.casse.Database.ArticleCasseController;
import com.a2bsystem.casse.Database.CasseController;
import com.a2bsystem.casse.Models.ArticleCasse;
import com.a2bsystem.casse.Models.Casse;
import com.a2bsystem.casse.R;

public class CasseActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private TextView mClient;
    private TextView mHdebut;
    private TextView mHfin;

    private ArticleCasseController articleCasseController;
    private CasseController casseController;

    private Casse casse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.casse);

        bottomNavigationView = findViewById(R.id.bottom_navigation_add);
        mClient    = findViewById(R.id.casse_client2);
        mHdebut    = findViewById(R.id.casse_H_debut2);
        mHfin      = findViewById(R.id.casse_H_fin2);

        loadClient();
        loadArticleCasse();
        updateHour();
        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadArticleCasse();
        updateHour();
        initListeners();
    }

    private void initListeners() {

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.add_onglet:

                        Intent intent = getIntent();
                        Casse casse = (Casse) intent.getSerializableExtra("casse");

                        Intent SaisieActivity = new Intent(CasseActivity.this, SaisieArticle.class);
                        SaisieActivity.putExtra("casse", casse);
                        startActivity(SaisieActivity);

                        break;

                }
                return false;
            }
        });
    }

    private void updateHour(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.FRANCE);
        String hour = format.format(new Date());
        Intent intent = getIntent();
        Casse casse = (Casse) intent.getSerializableExtra("casse");

        casseController = new CasseController(this);
        casseController.open();

        if(casse.getQ_2b_casse_heure_deb().equalsIgnoreCase("")){
            casse.setQ_2b_casse_heure_deb(hour);
            mHdebut.setText(hour);
        }
        else {
            casse.setQ_2b_casse_heure_fin(hour);
            mHfin.setText(hour);
        }

        casseController.updateCasse(casse);

        casseController.close();
    }


    private void loadClient(){
        Intent intent = getIntent();
        com.a2bsystem.casse.Models.Casse casse = (com.a2bsystem.casse.Models.Casse) intent.getSerializableExtra("casse");

        mClient.setText(casse.getFtgnamn());
        mHdebut.setText(casse.getQ_2b_casse_heure_deb());
        mHfin.setText(casse.getQ_2b_casse_heure_fin());
    }

    private void loadArticleCasse(){
        Intent intent = getIntent();
        casse = (com.a2bsystem.casse.Models.Casse) intent.getSerializableExtra("casse");

        final ArrayList<ArticleCasse> articlesCasse = new ArrayList<>();
        ListView listView = findViewById(R.id.CasseListView);
        articleCasseController = new ArticleCasseController(this);

        articleCasseController.open();

        articlesCasse.addAll(articleCasseController.getArticlesCasseByCasse(casse));
        ArticleCasseAdapter adapter = new ArticleCasseAdapter(CasseActivity.this, R.layout.casse_line, articlesCasse);
        listView.setAdapter(adapter);

        // Ecoute des clicks sur les lignes
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(articlesCasse.get(position).getTrans().equalsIgnoreCase("TRANSFERED")){
                    showError("Article déja transféré", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { }
                    });
                }
                else {
                    Intent ModifArticleActivity = new Intent(CasseActivity.this, ModifArticle.class);
                    ModifArticleActivity.putExtra("articleCasse",articlesCasse.get(position));
                    ModifArticleActivity.putExtra("casse",casse);
                    startActivity(ModifArticleActivity);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if(articlesCasse.get(position).getTrans().equalsIgnoreCase("TRANSFERED")){
                    showError("Article déja transféré", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { }
                    });
                }
                else {
                    deleteArticle(articlesCasse.get(position));
                }
                return true;
            }
        });
        articleCasseController.close();
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

    public void deleteArticle(final ArticleCasse article){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Suppression de l'article");
        builder.setMessage("Confirmer suppression?");
        builder.setPositiveButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        builder.setNegativeButton("Oui", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                articleCasseController.open();
                articleCasseController.deleteAricleCasse(article);
                articleCasseController.close();
                loadArticleCasse();
            }
        });
        builder.show();
    }
}

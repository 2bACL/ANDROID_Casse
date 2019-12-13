package com.a2bsystem.casse.Activities.listecasses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import com.a2bsystem.casse.Activities.config.Config;
import com.a2bsystem.casse.Activities.nouvellecasse.NouvelleCasse;
import com.a2bsystem.casse.Activities.transfert.Transfert;
import com.a2bsystem.casse.Adapters.CasseAdapter;
import com.a2bsystem.casse.Database.ArticleCasseController;
import com.a2bsystem.casse.Database.CasseController;
import com.a2bsystem.casse.Models.ArticleCasse;
import com.a2bsystem.casse.Models.Casse;
import com.a2bsystem.casse.R;

public class ListeCasses extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private CasseController casseController;
    private ArticleCasseController articleCasseController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_casses);
        this.configureToolbar();

        initFields();
        initListeners();

        deleteOldTransferedCasse();
        updateCasse();
        loadCasses();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCasse();
        loadCasses();
    }

    private void initFields(){

        bottomNavigationView = findViewById(R.id.bottom_navigation_add);

    }

    private void initListeners(){


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.add_onglet:

                        Intent NewCasseActivity = new Intent(ListeCasses.this, NouvelleCasse.class);
                        startActivity(NewCasseActivity);

                        break;

                }
                return false;
            }
        });

    }


    private void loadCasses() {
        final ArrayList<Casse> casses = new ArrayList<>();
        ListView listView = findViewById(R.id.MainListView);
        casseController = new CasseController(this);

        casseController.open();

        casses.addAll(casseController.getAllCasses());
        CasseAdapter adapter = new CasseAdapter(ListeCasses.this, R.layout.liste_casses_lines, casses);
        listView.setAdapter(adapter);

        // Ecoute des clicks sur les lignes
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent CasseActivity = new Intent(ListeCasses.this, com.a2bsystem.casse.Activities.casse.CasseActivity.class);
                CasseActivity.putExtra("casse",casses.get(position));
                startActivity(CasseActivity);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if(casses.get(position).getStatus().equalsIgnoreCase("TRANSFERED")){
                    showError("Casse déja transféré", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { }
                    });
                }
                else {
                    deleteCasse(casses.get(position));
                }
                return true;
            }
        });
        casseController.close();
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

    public void deleteCasse(final Casse casse){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Suppression de la Casse");
        builder.setMessage("Confirmer la suppression?");
        builder.setPositiveButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        builder.setNegativeButton("Oui", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {

                articleCasseController.open();
                articleCasseController.deleteArticlesCasseByCasse(casse);
                articleCasseController.close();

                casseController.open();
                casseController.deleteCasse(casse);
                casseController.close();

                updateCasse();
                loadCasses();
            }
        });
        builder.show();
    }

    private void deleteOldTransferedCasse(){
        final ArrayList<Casse> casses = new ArrayList<>();

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        int month = calendar.get(Calendar.MONTH) + 1;
        int day   = calendar.get(Calendar.DAY_OF_MONTH);
        String y  = Integer.toString(calendar.get(Calendar.YEAR));
        String m  = Integer.toString(month);
        String d  = Integer.toString(day);


        if(month < 10){
            m = "0"+ m;
        }
        if(day < 10){
            d = "0"+ d;
        }

        String today = y + "-" + m + "-" + d;

        casseController = new CasseController(this);
        articleCasseController = new ArticleCasseController(this);


        casseController.open();
        casses.addAll(casseController.getAllCasses());
        for(int i = 0 ; i < casses.size(); i++){

            System.out.println("casse : " + casses.get(i).getQ_2b_casse_dt_reprise());
            System.out.println("today : " + today);

            if(casses.get(i).getStatus().equalsIgnoreCase("TRANSFERED") &&
                    !casses.get(i).getQ_2b_casse_dt_reprise().equalsIgnoreCase(today)) {

                articleCasseController.open();
                articleCasseController.deleteArticlesCasseByCasse(casses.get(i));
                articleCasseController.close();

                casseController.deleteCasse(casses.get(i));
                casseController.close();
            }
        }
    }


    private void updateCasse(){
        casseController = new CasseController(this);
        articleCasseController = new ArticleCasseController(this);

        articleCasseController.open();
        casseController.open();

        final ArrayList<Casse> casses = new ArrayList<>();
        casses.addAll(casseController.getAllCasses());

        for( int i = 0 ; i < casses.size() ; i++ ){

            final ArrayList<ArticleCasse> articlesCasse = new ArrayList<>();
            articlesCasse.addAll(articleCasseController.getArticlesCasseByCasse(casses.get(i)));

            int created = 0;
            int transfered = 0;

            for( int j = 0 ; j < articlesCasse.size() ; j++ ) {

                if(articlesCasse.get(j).getTrans().equalsIgnoreCase("CREATED")){
                    created++;
                }
                else if (articlesCasse.get(j).getTrans().equalsIgnoreCase("TRANSFERED")){
                    transfered++;
                }
            }

            casseController.open();

            if(transfered == 0){
                casses.get(i).setStatus("CREATED");
            }
            else if (transfered > 0 && created > 0) {
                casses.get(i).setStatus("INPROGRESS");
            }

            casseController.updateCasse(casses.get(i));
        }
        casseController.close();
        articleCasseController.close();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    private void configureToolbar(){
        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("LISTE CASSE");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity_conf:

                Intent ConfigActivity = new Intent(ListeCasses.this, Config.class);
                ListeCasses.this.finish();
                startActivity(ConfigActivity);

                return true;
            case R.id.menu_activity_casse:
                return true;
            case R.id.menu_activity_transf:

                Intent TransfertActivity = new Intent(ListeCasses.this, Transfert.class);
                ListeCasses.this.finish();
                startActivity(TransfertActivity);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

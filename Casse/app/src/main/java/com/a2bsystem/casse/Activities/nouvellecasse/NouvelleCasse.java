package com.a2bsystem.casse.Activities.nouvellecasse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import com.a2bsystem.casse.Activities.listecasses.ListeCasses;
import com.a2bsystem.casse.Adapters.ListViewAdapter;
import com.a2bsystem.casse.Database.CasseController;
import com.a2bsystem.casse.Database.ClientController;
import com.a2bsystem.casse.Helper;
import com.a2bsystem.casse.Models.Casse;
import com.a2bsystem.casse.Models.Client;
import com.a2bsystem.casse.R;

import static com.a2bsystem.casse.Activities.config.Config.Config;


public class NouvelleCasse extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ListView list;
    private SearchView editsearch;
    private ListViewAdapter adapter;
    private ArrayList<com.a2bsystem.casse.Models.Client> clients = new ArrayList<>();
    private ClientController clientController;
    private CasseController casseController;

    private String Lagstalle;
    private String Merchandiser;
    private String Foretagkod;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nouvelle_casse);
        initFileds();
        initListeners();
        loadCasses();
    }

    private void initFileds(){
        list       = findViewById(R.id.listview);
        editsearch = findViewById(R.id.search);
    }

    private void initListeners(){

    }

    private void loadCasses() {
        clientController = new ClientController(this);
        casseController = new CasseController(this);


        clientController.open();
        clients.addAll(clientController.getAllClients());
        clientController.close();

        adapter = new ListViewAdapter(NouvelleCasse.this, clients);
        list.setAdapter(adapter);
        editsearch.setOnQueryTextListener(this);

        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createCasse((int)id);
            }
        });
    }

    private void createCasse(int id){
        getPrefs();
        Client client = clients.get(id);
        Casse casse = new Casse(Foretagkod,
                Lagstalle,
                Merchandiser,
                client.getFtgnr(),
                client.getFtgnamn(),
                Helper.getDate(),
                "",
                "",
                "CREATED");

        casseController.open();
        if(casseController.existCasse(casse) != 0){
            showError("Casse déja existante", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent MainActivity = new Intent(NouvelleCasse.this, ListeCasses.class);
                    NouvelleCasse.this.finish();
                    startActivity(MainActivity);
                }
            });
        }
        else {
            casseController.createCasse(casse);
            showOk("Casse créée", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent MainActivity = new Intent(NouvelleCasse.this, ListeCasses.class);
                    NouvelleCasse.this.finish();
                    startActivity(MainActivity);
                }
            });
        }
        casseController.close();

    }

    private void getPrefs() {
        SharedPreferences sharedPreferences = this.getApplicationContext().getSharedPreferences(Config, 0);
        Merchandiser = sharedPreferences.getString("Merchandiser", "");
        Lagstalle = sharedPreferences.getString("Depot", "");
        Foretagkod = sharedPreferences.getString("Foretagkod","");
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);

        return false;
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

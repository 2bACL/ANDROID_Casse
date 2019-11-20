package com.a2bsystem.casse.Activities.saisiearticle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import com.a2bsystem.casse.Adapters.ListViewArticleAdapter;
import com.a2bsystem.casse.Database.ArticleController;
import com.a2bsystem.casse.Models.Article;
import com.a2bsystem.casse.Models.Casse;
import com.a2bsystem.casse.R;

public class ChoixArticle extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ListView list;
    private SearchView editsearch;
    private ListViewArticleAdapter adapter;
    private ArrayList<Article> articles = new ArrayList<>();
    private ArticleController articleController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choix_article);
        initFileds();
        loadArticles();
    }

    private void initFileds(){
        list       = findViewById(R.id.listviewArticle);
        editsearch = findViewById(R.id.searchArticle);
    }


    private void loadArticles() {
        articleController = new ArticleController(this);


        articleController.open();
        articles.addAll(articleController.getAllArticles());
        articleController.close();

        adapter = new ListViewArticleAdapter(ChoixArticle.this, articles);
        list.setAdapter(adapter);
        editsearch.setOnQueryTextListener(this);

        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addArticle((int)id);
            }
        });
    }

    private Casse getCurrentCasse(){
        Intent intent = getIntent();
        return (Casse) intent.getSerializableExtra("casse");
    }

    private void addArticle(int id){

        Article article = articles.get(id);
        Casse casse = getCurrentCasse();

        Intent SaisieManuelle = new Intent(ChoixArticle.this, SaisieManuelle.class);
        SaisieManuelle.putExtra("casse", casse);
        SaisieManuelle.putExtra("article", article);
        ChoixArticle.this.finish();
        startActivity(SaisieManuelle);
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
}

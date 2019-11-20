package com.a2bsystem.casse.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.a2bsystem.casse.Models.ArticleCasse;
import com.a2bsystem.casse.Models.Casse;

public class ArticleCasseController {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            DatabaseHelper.KEY_ID,
            DatabaseHelper.KEY_FORETAGKOD,
            DatabaseHelper.KEY_LAGSTALLE,
            DatabaseHelper.KEY_Q_2B_MERCH_CODE,
            DatabaseHelper.KEY_FTGNR,
            DatabaseHelper.KEY_Q_2B_CASSE_DT_REPRISE,
            DatabaseHelper.KEY_Q_2B_CASSE_LIGNE,
            DatabaseHelper.KEY_ARTNR,
            DatabaseHelper.KEY_QUANTITE,
            DatabaseHelper.KEY_COMM,
            DatabaseHelper.KEY_DATE,
            DatabaseHelper.KEY_PANET,
            DatabaseHelper.KEY_PA_BRUT,
            DatabaseHelper.KEY_PVC,
            DatabaseHelper.KEY_TRANS,
            DatabaseHelper.KEY_MOMSKOD};


    public ArticleCasseController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public ArticleCasse createArticleCasse(String foretagkod, String lagstalle, String q_2b_merch_code, String ftgnr, String q_2b_casse_dt_reprise, String q_2b_casse_ligne, String artnr, String qte, String comm, String date, String panet, String pa_brut, String pvc, String trans, String momskod) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_FORETAGKOD, foretagkod);
        values.put(DatabaseHelper.KEY_LAGSTALLE, lagstalle);
        values.put(DatabaseHelper.KEY_Q_2B_MERCH_CODE, q_2b_merch_code);
        values.put(DatabaseHelper.KEY_FTGNR, ftgnr);
        values.put(DatabaseHelper.KEY_Q_2B_CASSE_DT_REPRISE, q_2b_casse_dt_reprise);
        values.put(DatabaseHelper.KEY_Q_2B_CASSE_LIGNE, q_2b_casse_ligne);
        values.put(DatabaseHelper.KEY_ARTNR, artnr);
        values.put(DatabaseHelper.KEY_QUANTITE, qte);
        values.put(DatabaseHelper.KEY_COMM, comm);
        values.put(DatabaseHelper.KEY_DATE, date);
        values.put(DatabaseHelper.KEY_PVC, pvc);
        values.put(DatabaseHelper.KEY_PANET, panet);
        values.put(DatabaseHelper.KEY_PA_BRUT, pa_brut);
        values.put(DatabaseHelper.KEY_TRANS, trans);
        values.put(DatabaseHelper.KEY_MOMSKOD, momskod);


        long insertId = database.insert(DatabaseHelper.TABLE_ARTICLE_CASSE, null,
                values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_ARTICLE_CASSE,
                allColumns, DatabaseHelper.KEY_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        ArticleCasse newArticleCasse = cursorToArticleCasse(cursor);
        cursor.close();

        return newArticleCasse;
    }

    public ArticleCasse createArticleCasse(ArticleCasse articleCasse) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_FORETAGKOD, articleCasse.getForetagkod());
        values.put(DatabaseHelper.KEY_LAGSTALLE, articleCasse.getLagstalle());
        values.put(DatabaseHelper.KEY_Q_2B_MERCH_CODE, articleCasse.getQ_2b_merch_code());
        values.put(DatabaseHelper.KEY_FTGNR, articleCasse.getFtgnr());
        values.put(DatabaseHelper.KEY_Q_2B_CASSE_DT_REPRISE, articleCasse.getQ_2b_casse_dt_reprise());
        values.put(DatabaseHelper.KEY_Q_2B_CASSE_LIGNE, articleCasse.getQ_2b_casse_ligne());
        values.put(DatabaseHelper.KEY_ARTNR, articleCasse.getArtnr());
        values.put(DatabaseHelper.KEY_QUANTITE, articleCasse.getQte());
        values.put(DatabaseHelper.KEY_COMM, articleCasse.getComm());
        values.put(DatabaseHelper.KEY_DATE, articleCasse.getDate());
        values.put(DatabaseHelper.KEY_PVC, articleCasse.getPvc());
        values.put(DatabaseHelper.KEY_PANET, articleCasse.getPanet());
        values.put(DatabaseHelper.KEY_PA_BRUT, articleCasse.getPa_brut());
        values.put(DatabaseHelper.KEY_TRANS, articleCasse.getTrans());
        values.put(DatabaseHelper.KEY_MOMSKOD, articleCasse.getMomskod());


        long insertId = database.insert(DatabaseHelper.TABLE_ARTICLE_CASSE, null,
                values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_ARTICLE_CASSE,
                allColumns, DatabaseHelper.KEY_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        ArticleCasse newArticleCasse = cursorToArticleCasse(cursor);
        cursor.close();

        return newArticleCasse;
    }

    public void updateArticleCasse(ArticleCasse articleCasse ) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_FORETAGKOD, articleCasse.getForetagkod());
        values.put(DatabaseHelper.KEY_LAGSTALLE, articleCasse.getLagstalle());
        values.put(DatabaseHelper.KEY_Q_2B_MERCH_CODE, articleCasse.getQ_2b_merch_code());
        values.put(DatabaseHelper.KEY_FTGNR, articleCasse.getFtgnr());
        values.put(DatabaseHelper.KEY_Q_2B_CASSE_DT_REPRISE, articleCasse.getQ_2b_casse_dt_reprise());
        values.put(DatabaseHelper.KEY_Q_2B_CASSE_LIGNE, articleCasse.getQ_2b_casse_ligne());
        values.put(DatabaseHelper.KEY_ARTNR, articleCasse.getArtnr());
        values.put(DatabaseHelper.KEY_QUANTITE, articleCasse.getQte());
        values.put(DatabaseHelper.KEY_COMM, articleCasse.getComm());
        values.put(DatabaseHelper.KEY_DATE, articleCasse.getDate());
        values.put(DatabaseHelper.KEY_PANET, articleCasse.getPanet());
        values.put(DatabaseHelper.KEY_PA_BRUT, articleCasse.getPa_brut());
        values.put(DatabaseHelper.KEY_PVC, articleCasse.getPvc());
        values.put(DatabaseHelper.KEY_TRANS, articleCasse.getTrans());
        values.put(DatabaseHelper.KEY_MOMSKOD, articleCasse.getMomskod());

        String whereClause = DatabaseHelper.KEY_FORETAGKOD + " = ? AND "
                + DatabaseHelper.KEY_LAGSTALLE + " = ? AND "
                + DatabaseHelper.KEY_Q_2B_MERCH_CODE + " = ? AND "
                + DatabaseHelper.KEY_FTGNR + " = ? AND "
                + DatabaseHelper.KEY_Q_2B_CASSE_LIGNE + " = ? AND "
                + DatabaseHelper.KEY_Q_2B_CASSE_DT_REPRISE + " = ?";

        String[] whereArgs =
                {
                        articleCasse.getForetagkod(),
                        articleCasse.getLagstalle(),
                        articleCasse.getQ_2b_merch_code(),
                        articleCasse.getFtgnr(),
                        articleCasse.getQ_2b_casse_ligne(),
                        articleCasse.getQ_2b_casse_dt_reprise()
                };


        database.update(DatabaseHelper.TABLE_ARTICLE_CASSE,values, whereClause, whereArgs);

    }

    public void deleteAricleCasse(ArticleCasse articleCasse) {
        long id = articleCasse.getId();

        database.delete(DatabaseHelper.TABLE_ARTICLE_CASSE, DatabaseHelper.KEY_ID
                + " = " + id, null);
    }

    public List<ArticleCasse> getAllArticlesCasse() {
        List<ArticleCasse> articlesCasse = new ArrayList<ArticleCasse>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_ARTICLE_CASSE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ArticleCasse articleCasse = cursorToArticleCasse(cursor);
            articlesCasse.add(articleCasse);
            cursor.moveToNext();
        }
        cursor.close();
        return articlesCasse;
    }

    public List<ArticleCasse> getArticlesCasseByCasse(Casse casse) {
        List<ArticleCasse> articlesCasse = new ArrayList<ArticleCasse>();

        String whereClause = DatabaseHelper.KEY_FORETAGKOD + " = ? AND "
                + DatabaseHelper.KEY_LAGSTALLE + " = ? AND "
                + DatabaseHelper.KEY_Q_2B_MERCH_CODE + " = ? AND "
                + DatabaseHelper.KEY_FTGNR + " = ? AND "
                + DatabaseHelper.KEY_Q_2B_CASSE_DT_REPRISE + " = ?";

        String[] whereArgs =
                {
                        casse.getForetagkod(),
                        casse.getLagstalle(),
                        casse.getQ_2b_merch_code(),
                        casse.getFtgnr(),
                        casse.getQ_2b_casse_dt_reprise()
                };

        Cursor cursor = database.query(DatabaseHelper.TABLE_ARTICLE_CASSE,
                allColumns, whereClause, whereArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            ArticleCasse articleCasse = cursorToArticleCasse(cursor);
            articlesCasse.add(articleCasse);
            cursor.moveToNext();
        }
        cursor.close();
        return articlesCasse;
    }

    public void deleteArticlesCasseByCasse(Casse casse) {

        String whereClause = DatabaseHelper.KEY_FORETAGKOD + " = ? AND "
                + DatabaseHelper.KEY_LAGSTALLE + " = ? AND "
                + DatabaseHelper.KEY_Q_2B_MERCH_CODE + " = ? AND "
                + DatabaseHelper.KEY_FTGNR + " = ? AND "
                + DatabaseHelper.KEY_Q_2B_CASSE_DT_REPRISE + " = ?";

        String[] whereArgs =
                {
                        casse.getForetagkod(),
                        casse.getLagstalle(),
                        casse.getQ_2b_merch_code(),
                        casse.getFtgnr(),
                        casse.getQ_2b_casse_dt_reprise()
                };

        Cursor cursor = database.query(DatabaseHelper.TABLE_ARTICLE_CASSE,
                allColumns, whereClause, whereArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            ArticleCasse articleCasse = cursorToArticleCasse(cursor);
            deleteAricleCasse(articleCasse);
            cursor.moveToNext();
        }
        cursor.close();
    }

    public int getNbArticlesCasseByCasse(Casse casse) {

        String whereClause = DatabaseHelper.KEY_FORETAGKOD + " = ? AND "
                + DatabaseHelper.KEY_LAGSTALLE + " = ? AND "
                + DatabaseHelper.KEY_Q_2B_MERCH_CODE + " = ? AND "
                + DatabaseHelper.KEY_FTGNR + " = ? AND "
                + DatabaseHelper.KEY_Q_2B_CASSE_DT_REPRISE + " = ?";

        String[] whereArgs =
                {
                        casse.getForetagkod(),
                        casse.getLagstalle(),
                        casse.getQ_2b_merch_code(),
                        casse.getFtgnr(),
                        casse.getQ_2b_casse_dt_reprise()
                };

        Cursor cursor = database.query(DatabaseHelper.TABLE_ARTICLE_CASSE,
                allColumns, whereClause, whereArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
    }
        cursor.close();
        return cursor.getCount();
    }



    public ArticleCasse getArticleCasseById (long id) {

        String selectQuery =
                "SELECT  * FROM " + DatabaseHelper.TABLE_ARTICLE_CASSE +
                        " WHERE "
                        + DatabaseHelper.KEY_ID + " = " + id;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        ArticleCasse articleCasse = new ArticleCasse();
        articleCasse.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.KEY_ID)));
        articleCasse.setForetagkod(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FORETAGKOD)));
        articleCasse.setLagstalle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_LAGSTALLE)));
        articleCasse.setQ_2b_merch_code(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_Q_2B_MERCH_CODE)));
        articleCasse.setFtgnr(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FTGNR)));
        articleCasse.setQ_2b_casse_dt_reprise(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_Q_2B_CASSE_DT_REPRISE)));
        articleCasse.setQ_2b_casse_ligne(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_Q_2B_CASSE_LIGNE)));
        articleCasse.setArtnr(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ARTNR)));
        articleCasse.setQte(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_QUANTITE)));
        articleCasse.setComm(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_COMM)));
        articleCasse.setDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_DATE)));
        articleCasse.setPanet(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_PANET)));
        articleCasse.setPa_brut(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_PA_BRUT)));
        articleCasse.setPvc(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_PVC)));
        articleCasse.setTrans(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TRANS)));
        articleCasse.setMomskod(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_MOMSKOD)));


        cursor.close();

        return articleCasse;
    }



    private ArticleCasse cursorToArticleCasse(Cursor cursor) {
        ArticleCasse articleCasse = new ArticleCasse();
        articleCasse.setId(cursor.getLong(0));
        articleCasse.setForetagkod(cursor.getString(1));
        articleCasse.setLagstalle(cursor.getString(2));
        articleCasse.setQ_2b_merch_code(cursor.getString(3));
        articleCasse.setFtgnr(cursor.getString(4));
        articleCasse.setQ_2b_casse_dt_reprise(cursor.getString(5));
        articleCasse.setQ_2b_casse_ligne(cursor.getString(6));
        articleCasse.setArtnr(cursor.getString(7));
        articleCasse.setQte(cursor.getString(8));
        articleCasse.setComm(cursor.getString(9));
        articleCasse.setDate(cursor.getString(10));
        articleCasse.setPanet(cursor.getString(11));
        articleCasse.setPa_brut(cursor.getString(12));
        articleCasse.setPvc(cursor.getString(13));
        articleCasse.setTrans(cursor.getString(14));
        articleCasse.setMomskod(cursor.getString(15));
        return articleCasse;
    }
}

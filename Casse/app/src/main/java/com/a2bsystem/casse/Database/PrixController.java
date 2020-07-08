package com.a2bsystem.casse.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.a2bsystem.casse.Models.ArticleCasse;
import com.a2bsystem.casse.Models.Casse;
import com.a2bsystem.casse.Models.Prix;

import java.util.ArrayList;
import java.util.List;

public class PrixController {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            DatabaseHelper.KEY_ID,
            DatabaseHelper.KEY_FORETAGKOD,
            DatabaseHelper.KEY_LAGSTALLE,
            DatabaseHelper.KEY_FTGNR,
            DatabaseHelper.KEY_ARTNR,
            DatabaseHelper.KEY_DATE,
            DatabaseHelper.KEY_PANET,
            DatabaseHelper.KEY_PA_BRUT,
            DatabaseHelper.KEY_PVC};


    public PrixController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public Prix createPrix(Prix prix) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_FORETAGKOD, prix.getForetagkod());
        values.put(DatabaseHelper.KEY_LAGSTALLE, prix.getLagstalle());
        values.put(DatabaseHelper.KEY_FTGNR, prix.getFtgnr());
        values.put(DatabaseHelper.KEY_ARTNR, prix.getArtnr());
        values.put(DatabaseHelper.KEY_DATE, prix.getDate());
        values.put(DatabaseHelper.KEY_PVC, prix.getPvc());
        values.put(DatabaseHelper.KEY_PANET, prix.getPanet());
        values.put(DatabaseHelper.KEY_PA_BRUT, prix.getPa_brut());


        long insertId = database.insert(DatabaseHelper.TABLE_PRIX, null,
                values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_PRIX,
                allColumns, DatabaseHelper.KEY_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Prix newPrix = cursorToPrix(cursor);
        cursor.close();

        return newPrix;
    }


    public List<Prix> getPrixByArticleCasse(ArticleCasse articleCasse) {
        List<Prix> prix = new ArrayList<Prix>();

        System.out.println("azzzz " + articleCasse.toString());

        String whereClause = DatabaseHelper.KEY_FORETAGKOD + " = ? AND "
                + DatabaseHelper.KEY_LAGSTALLE + " = ? AND "
                + DatabaseHelper.KEY_DATE + " = ? AND "
                + DatabaseHelper.KEY_FTGNR + " = ? AND "
                + DatabaseHelper.KEY_ARTNR + " = ?";

        System.out.println(whereClause);

        String[] whereArgs =
                {
                        articleCasse.getForetagkod().trim(),
                        articleCasse.getLagstalle().trim(),
                        articleCasse.getDate().trim(),
                        articleCasse.getFtgnr().trim().toLowerCase(),
                        articleCasse.getArtnr().trim().toLowerCase()
                };

        for(int i = 0; i < whereArgs.length; i++){
            System.out.println(whereArgs[i]);
        }


        Cursor cursor = database.query(DatabaseHelper.TABLE_PRIX,
                allColumns, whereClause, whereArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Prix pri = cursorToPrix(cursor);
            prix.add(pri);
            cursor.moveToNext();
        }
        cursor.close();

        return prix;
    }

    public void deleteAll() {
        database.delete(DatabaseHelper.TABLE_PRIX, null, null);
    }




    private Prix cursorToPrix(Cursor cursor) {
        Prix prix = new Prix();
        prix.setId(cursor.getLong(0));
        prix.setForetagkod(cursor.getString(1));
        prix.setLagstalle(cursor.getString(2));
        prix.setFtgnr(cursor.getString(3));
        prix.setArtnr(cursor.getString(4));
        prix.setDate(cursor.getString(5));
        prix.setPanet(cursor.getString(6));
        prix.setPa_brut(cursor.getString(7));
        prix.setPvc(cursor.getString(8));
        return prix;
    }
}

package com.a2bsystem.casse.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.a2bsystem.casse.Models.Casse;

public class CasseController {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            DatabaseHelper.KEY_ID,
            DatabaseHelper.KEY_FORETAGKOD,
            DatabaseHelper.KEY_LAGSTALLE,
            DatabaseHelper.KEY_Q_2B_MERCH_CODE,
            DatabaseHelper.KEY_FTGNR,
            DatabaseHelper.KEY_FTGNAMN,
            DatabaseHelper.KEY_Q_2B_CASSE_DT_REPRISE,
            DatabaseHelper.KEY_Q_2B_CASSE_HEURE_DEB,
            DatabaseHelper.KEY_Q_2B_CASSE_HEURE_FIN,
            DatabaseHelper.KEY_STATUS};


    public CasseController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Casse createCasse(Casse casse) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_FORETAGKOD, casse.getForetagkod());
        values.put(DatabaseHelper.KEY_LAGSTALLE, casse.getLagstalle());
        values.put(DatabaseHelper.KEY_Q_2B_MERCH_CODE, casse.getQ_2b_merch_code());
        values.put(DatabaseHelper.KEY_FTGNR, casse.getFtgnr());
        values.put(DatabaseHelper.KEY_FTGNAMN, casse.getFtgnamn());
        values.put(DatabaseHelper.KEY_Q_2B_CASSE_DT_REPRISE, casse.getQ_2b_casse_dt_reprise());
        values.put(DatabaseHelper.KEY_Q_2B_CASSE_HEURE_DEB, casse.getQ_2b_casse_heure_deb());
        values.put(DatabaseHelper.KEY_Q_2B_CASSE_HEURE_FIN, casse.getQ_2b_casse_heure_fin());
        values.put(DatabaseHelper.KEY_STATUS, casse.getStatus());

        long insertId = database.insert(DatabaseHelper.TABLE_CASSE, null,
                values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_CASSE,
                allColumns, DatabaseHelper.KEY_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Casse newCasse = cursorToCasse(cursor);
        cursor.close();

        return newCasse;
    }

    public void updateCasse(Casse casse) {
        long id = casse.getId();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_FORETAGKOD, casse.getForetagkod());
        values.put(DatabaseHelper.KEY_LAGSTALLE, casse.getLagstalle());
        values.put(DatabaseHelper.KEY_Q_2B_MERCH_CODE, casse.getQ_2b_merch_code());
        values.put(DatabaseHelper.KEY_FTGNR, casse.getFtgnr());
        values.put(DatabaseHelper.KEY_FTGNAMN, casse.getFtgnamn());
        values.put(DatabaseHelper.KEY_Q_2B_CASSE_DT_REPRISE, casse.getQ_2b_casse_dt_reprise());
        values.put(DatabaseHelper.KEY_Q_2B_CASSE_HEURE_DEB, casse.getQ_2b_casse_heure_deb());
        values.put(DatabaseHelper.KEY_Q_2B_CASSE_HEURE_FIN, casse.getQ_2b_casse_heure_fin());
        values.put(DatabaseHelper.KEY_STATUS, casse.getStatus());

        database.update(DatabaseHelper.TABLE_CASSE,values, DatabaseHelper.KEY_ID + " = " + id, null);
    }

    public void deleteCasse(Casse casse) {
        long id = casse.getId();

        database.delete(DatabaseHelper.TABLE_CASSE, DatabaseHelper.KEY_ID
                + " = " + id, null);
    }

    public List<Casse> getAllCasses() {
        List<Casse> casses = new ArrayList<Casse>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_CASSE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Casse casse = cursorToCasse(cursor);
            casses.add(casse);
            cursor.moveToNext();
        }
        cursor.close();
        return casses;
    }



    public Casse getCasseById (long id) {

        String selectQuery =
                "SELECT  * FROM " + DatabaseHelper.TABLE_CASSE +
                        " WHERE "
                        + DatabaseHelper.KEY_ID + " = " + id;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Casse casse = new Casse();
        casse.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.KEY_ID)));
        casse.setForetagkod(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FORETAGKOD)));
        casse.setLagstalle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_LAGSTALLE)));
        casse.setQ_2b_merch_code(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_Q_2B_MERCH_CODE)));
        casse.setFtgnr(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FTGNR)));
        casse.setFtgnamn(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FTGNAMN)));
        casse.setQ_2b_casse_dt_reprise(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_Q_2B_CASSE_DT_REPRISE)));
        casse.setQ_2b_casse_heure_deb(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_Q_2B_CASSE_HEURE_DEB)));
        casse.setQ_2b_casse_heure_fin(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_Q_2B_CASSE_HEURE_FIN)));
        casse.setStatus(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_STATUS)));


        cursor.close();

        return casse;
    }

    public int existCasse (Casse casse) {

        String selectQuery =
                "SELECT  * FROM " + DatabaseHelper.TABLE_CASSE +
                        " WHERE "
                        + DatabaseHelper.KEY_FORETAGKOD + " = " + casse.getForetagkod() + " AND "
                        + DatabaseHelper.KEY_LAGSTALLE + " = " + casse.getLagstalle() + " AND "
                        + DatabaseHelper.KEY_Q_2B_MERCH_CODE + " = " + casse.getQ_2b_merch_code() + " AND "
                        + DatabaseHelper.KEY_FTGNR + " = " + casse.getFtgnr() + " AND "
                        + DatabaseHelper.KEY_Q_2B_CASSE_DT_REPRISE + " = " + casse.getQ_2b_casse_dt_reprise();


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

        Cursor cursor = database.query(DatabaseHelper.TABLE_CASSE,
                allColumns, whereClause, whereArgs, null, null, null);


        if(cursor != null){
            cursor.moveToFirst();
        }


        cursor.close();

        return cursor.getCount();
    }

    public int getNbCassetoTransfer () {

        String selectQuery =
                "SELECT  * FROM " + DatabaseHelper.TABLE_CASSE +
                        " WHERE "
                        + DatabaseHelper.KEY_STATUS + " != 'TRANSFERED'";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        cursor.close();

        return cursor.getCount();
    }



    private Casse cursorToCasse(Cursor cursor) {
        Casse casse = new Casse();
        casse.setId(cursor.getLong(0));
        casse.setForetagkod(cursor.getString(1));
        casse.setLagstalle(cursor.getString(2));
        casse.setQ_2b_merch_code(cursor.getString(3));
        casse.setFtgnr(cursor.getString(4));
        casse.setFtgnamn(cursor.getString(5));
        casse.setQ_2b_casse_dt_reprise(cursor.getString(6));
        casse.setQ_2b_casse_heure_deb(cursor.getString(7));
        casse.setQ_2b_casse_heure_fin(cursor.getString(8));
        casse.setStatus(cursor.getString(9));
        return casse;
    }

}

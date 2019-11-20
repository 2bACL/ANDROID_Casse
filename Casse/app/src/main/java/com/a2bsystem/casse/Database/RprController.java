package com.a2bsystem.casse.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.a2bsystem.casse.Models.Rpr;


public class RprController {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            DatabaseHelper.KEY_ID,
            DatabaseHelper.KEY_FORETAGKOD,
            DatabaseHelper.KEY_ARTNR,
            DatabaseHelper.KEY_FTGNR,
            DatabaseHelper.KEY_Q_PVC_VAL,
            DatabaseHelper.KEY_ARTSTRECKKOD,
            DatabaseHelper.KEY_ARTNRKUND};


    public RprController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Rpr createRpr(String foretagkod, String artnr, String ftgnr, String q_pvc_val, String artstreckkod, String artnrkund ) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_FORETAGKOD, foretagkod);
        values.put(DatabaseHelper.KEY_ARTNR, artnr);
        values.put(DatabaseHelper.KEY_FTGNR, ftgnr);
        values.put(DatabaseHelper.KEY_Q_PVC_VAL, q_pvc_val);
        values.put(DatabaseHelper.KEY_ARTSTRECKKOD, artstreckkod);
        values.put(DatabaseHelper.KEY_ARTNRKUND, artnrkund);

        long insertId = database.insert(DatabaseHelper.TABLE_RPR, null,
                values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_RPR,
                allColumns, DatabaseHelper.KEY_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Rpr newRpr = cursorToRpr(cursor);
        cursor.close();

        return newRpr;
    }

    public void deleteRpr(Rpr rpr) {
        long id = rpr.getId();

        database.delete(DatabaseHelper.TABLE_RPR, DatabaseHelper.KEY_ID
                + " = " + id, null);
    }



    public Rpr getRprById (long id) {

        String selectQuery =
                "SELECT  * FROM " + DatabaseHelper.TABLE_RPR +
                        " WHERE "
                        + DatabaseHelper.KEY_ID + " = " + id;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Rpr rpr = new Rpr();
        rpr.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.KEY_ID)));
        rpr.setForetagkod(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FORETAGKOD)));
        rpr.setArtnr(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_LAGSTALLE)));
        rpr.setFtgnr(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_Q_2B_MERCH_CODE)));
        rpr.setQ_pvc_val(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FTGNR)));
        rpr.setArtstreckkod(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_Q_2B_CASSE_DT_REPRISE)));
        rpr.setArtnrkund(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_Q_2B_CASSE_LIGNE)));


        cursor.close();

        return rpr;
    }



    private Rpr cursorToRpr(Cursor cursor) {
        Rpr rpr = new Rpr();
        rpr.setId(cursor.getLong(0));
        rpr.setForetagkod(cursor.getString(1));
        rpr.setArtnr(cursor.getString(2));
        rpr.setFtgnr(cursor.getString(3));
        rpr.setQ_pvc_val(cursor.getString(4));
        rpr.setArtstreckkod(cursor.getString(5));
        rpr.setArtnrkund(cursor.getString(6));
        return rpr;
    }
}

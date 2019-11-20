package com.a2bsystem.casse.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.a2bsystem.casse.Models.Client;

public class ClientController {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            DatabaseHelper.KEY_ID,
            DatabaseHelper.KEY_FORETAGKOD,
            DatabaseHelper.KEY_FTGNR,
            DatabaseHelper.KEY_FTGNAMN};

    public ClientController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }



    public Client createClient(String foretagkod, String ftgnr, String ftgnamn) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_FORETAGKOD, foretagkod);
        values.put(DatabaseHelper.KEY_FTGNR, ftgnr);
        values.put(DatabaseHelper.KEY_FTGNAMN, ftgnamn);

        long insertId = database.insert(DatabaseHelper.TABLE_CLIENT, null,
                values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_CLIENT,
                allColumns, DatabaseHelper.KEY_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Client newClient = cursorToClient(cursor);
        cursor.close();

        return newClient;
    }

    public void deleteAll() {
        database.delete(DatabaseHelper.TABLE_CLIENT, null, null);
    }



    public void deleteClient(Client client) {
        long id = client.getId();

        database.delete(DatabaseHelper.TABLE_CLIENT, DatabaseHelper.KEY_ID
                + " = " + id, null);
    }



    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<Client>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_CLIENT,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Client client = cursorToClient(cursor);
            clients.add(client);
            cursor.moveToNext();
        }
        cursor.close();
        return clients;
    }

    public Client getClientById (long id) {

        String selectQuery =
                "SELECT  * FROM " + DatabaseHelper.TABLE_CLIENT +
                " WHERE "
                + DatabaseHelper.KEY_ID + " = " + id;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Client client = new Client();
        client.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_ID)));
        client.setForetagkod(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FORETAGKOD)));
        client.setFtgnr(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FTGNR)));
        client.setFtgnamn(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FTGNAMN)));

        cursor.close();

        return client;
    }

    public Client getClientByFtgnamn (String ftgnamn) {

        String selectQuery =
                "SELECT  * FROM " + DatabaseHelper.TABLE_CLIENT +
                        " WHERE "
                        + DatabaseHelper.KEY_FTGNAMN + " = " + ftgnamn;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Client client = new Client();
        client.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_ID)));
        client.setForetagkod(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FORETAGKOD)));
        client.setFtgnr(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FTGNR)));
        client.setFtgnamn(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FTGNAMN)));

        cursor.close();

        return client;
    }


    private Client cursorToClient(Cursor cursor) {
        Client client = new Client();
        client.setId(cursor.getLong(0));
        client.setForetagkod(cursor.getString(1));
        client.setFtgnr(cursor.getString(2));
        client.setFtgnamn(cursor.getString(3));
        return client;
    }


}

package com.a2bsystem.casse.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.a2bsystem.casse.Models.Config;

public class ConfigController {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            DatabaseHelper.KEY_ID,
            DatabaseHelper.KEY_PARAM,
            DatabaseHelper.KEY_VALUE};

    public ConfigController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }



    public Config createConfig(String param, String value) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_PARAM, param);
        values.put(DatabaseHelper.KEY_VALUE, value);

        long insertId = database.insert(DatabaseHelper.TABLE_CONFIG, null,
                values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_CONFIG,
                allColumns, DatabaseHelper.KEY_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Config newConfig = cursorToConfig(cursor);
        cursor.close();

        return newConfig;
    }



    public void deleteConfig(Config config) {
        long id = config.getId();

        database.delete(DatabaseHelper.TABLE_CONFIG, DatabaseHelper.KEY_ID
                + " = " + id, null);
    }



    public List<Config> getAllConfigs() {
        List<Config> configs = new ArrayList<Config>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_CONFIG,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Config config = cursorToConfig(cursor);
            configs.add(config);
            cursor.moveToNext();
        }
        cursor.close();
        return configs;
    }

    public Config getConfigById (long id) {

        String selectQuery =
                "SELECT  * FROM " + DatabaseHelper.TABLE_CONFIG +
                        " WHERE "
                        + DatabaseHelper.KEY_ID + " = " + id;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Config config = new Config();
        config.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_ID)));
        config.setParam(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_PARAM)));
        config.setValue(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_Q_GCAR_LIB1)));

        cursor.close();

        return config;
    }

    public Config getConfigByParam (String param) {

        String selectQuery =
                "SELECT  * FROM " + DatabaseHelper.TABLE_CONFIG +
                        " WHERE "
                        + DatabaseHelper.KEY_PARAM + " = " + param;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Config config = new Config();
        config.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_ID)));
        config.setParam(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_PARAM)));
        config.setValue(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_Q_GCAR_LIB1)));

        cursor.close();

        return config;
    }



    private Config cursorToConfig(Cursor cursor) {
        Config config = new Config();
        config.setId(cursor.getLong(0));
        config.setParam(cursor.getString(1));
        config.setValue(cursor.getString(2));
        return config;
    }

}

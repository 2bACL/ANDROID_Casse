package com.a2bsystem.casse.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    public static final String LOG = "DatabaseHelper";

    // Database Version
    public static final int DATABASE_VERSION = 2;

    // Database Name
    public static final String DATABASE_NAME = "casseManager";

    // Table Names
    public static final String TABLE_ARTICLE        = "articles";
    public static final String TABLE_ARTICLE_CASSE  = "articles_casse";
    public static final String TABLE_CASSE          = "casses";
    public static final String TABLE_CLIENT         = "clients";
    public static final String TABLE_CONFIG         = "config";
    public static final String TABLE_RPR            = "rpr";

    // Common column names
    public static final String KEY_ID = "id";

    // ARTICLE  Table - column names
    public static final String KEY_ARTNR       = "artnr";
    public static final String KEY_Q_GCAR_LIB1 = "q_gcar_lib1";
    public static final String KEY_MOMSKOD     = "momskod";

    // CLIENT  Table - column names
    public static final String KEY_FTGNR   = "ftgnr";
    public static final String KEY_FTGNAMN = "ftgnamn";

    // ARTICLE_CASSE Table - column names
    public static final String KEY_QUANTITE         = "quantity";
    public static final String KEY_Q_2B_CASSE_LIGNE = "q_2b_casse_ligne";
    public static final String KEY_COMM             = "comm";
    public static final String KEY_DATE             = "date";
    public static final String KEY_TRANS            = "trans";
    public static final String KEY_PANET            = "panet";
    public static final String KEY_PA_BRUT          = "pa_brut";
    public static final String KEY_PVC              = "pvc";


    // CONFIG Table - column names
    public static final String KEY_PARAM = "param";
    public static final String KEY_VALUE = "value";

    // CASSE Table - column names
    public static final String KEY_FORETAGKOD            = "foretagkod";
    public static final String KEY_LAGSTALLE             = "lagstalle";
    public static final String KEY_Q_2B_MERCH_CODE       = "q_2b_merch_code";
    public static final String KEY_Q_2B_CASSE_DT_REPRISE = "q_2b_casse_dt_reprise";
    public static final String KEY_Q_2B_CASSE_HEURE_DEB  = "q_2b_casse_heure_deb";
    public static final String KEY_Q_2B_CASSE_HEURE_FIN  = "q_2b_casse_heure_fin";
    public static final String KEY_STATUS                = "status";

    // RPR Table - column names
    public static final String KEY_Q_PVC_VAL    = "q_pvc_val";
    public static final String KEY_ARTSTRECKKOD = "artstreckkod";
    public static final String KEY_ARTNRKUND    = "artnrkund";



    // Article table create statement
    public static final String CREATE_TABLE_ARTICLE = "CREATE TABLE "
            + TABLE_ARTICLE
            + "("
            + KEY_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ARTNR       + " TEXT NOT NULL,"
            + KEY_MOMSKOD     + " TEXT NOT NULL,"
            + KEY_Q_GCAR_LIB1 + " TEXT NOT NULL"
            + ")";

    // Client table create statement
    public static final String CREATE_TABLE_CLIENT = "CREATE TABLE "
            + TABLE_CLIENT
            + "("
            + KEY_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_FORETAGKOD + " TEXT NOT NULL,"
            + KEY_FTGNR      + " TEXT NOT NULL,"
            + KEY_FTGNAMN    + " TEXT NOT NULL"
            + ")";

    // Article Casse table create statement
    public static final String CREATE_TABLE_ARTICLE_CASSE = "CREATE TABLE "
            + TABLE_ARTICLE_CASSE
            + "("
            + KEY_ID                    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_FORETAGKOD            + " TEXT NOT NULL,"
            + KEY_LAGSTALLE             + " TEXT NOT NULL,"
            + KEY_Q_2B_MERCH_CODE       + " TEXT NOT NULL,"
            + KEY_FTGNR                 + " TEXT NOT NULL,"
            + KEY_Q_2B_CASSE_DT_REPRISE + " TEXT NOT NULL,"
            + KEY_Q_2B_CASSE_LIGNE      + " TEXT NOT NULL,"
            + KEY_ARTNR                 + " TEXT NOT NULL,"
            + KEY_QUANTITE              + " TEXT NOT NULL,"
            + KEY_COMM                  + " TEXT NOT NULL,"
            + KEY_DATE                  + " TEXT NOT NULL,"
            + KEY_PANET                 + " TEXT NOT NULL,"
            + KEY_PA_BRUT               + " TEXT NOT NULL,"
            + KEY_PVC                   + " TEXT NOT NULL,"
            + KEY_TRANS                 + " TEXT NOT NULL,"
            + KEY_MOMSKOD               + " TEXT NOT NULL"
            + ")";

    // Config table create statement
    public static final String CREATE_TABLE_CONFIG = "CREATE TABLE "
            + TABLE_CONFIG
            + "("
            + KEY_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PARAM + " TEXT NOT NULL,"
            + KEY_VALUE + " TEXT NOT NULL"
            + ")";

    // CasseActivity table create statement
    public static final String CREATE_TABLE_CASSE = "CREATE TABLE "
            + TABLE_CASSE
            + "("
            + KEY_ID                    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_FORETAGKOD            + " TEXT NOT NULL,"
            + KEY_LAGSTALLE             + " TEXT NOT NULL,"
            + KEY_Q_2B_MERCH_CODE       + " TEXT NOT NULL,"
            + KEY_FTGNR                 + " TEXT NOT NULL,"
            + KEY_FTGNAMN               + " TEXT NOT NULL,"
            + KEY_Q_2B_CASSE_DT_REPRISE + " TEXT NOT NULL,"
            + KEY_Q_2B_CASSE_HEURE_DEB  + " TEXT NOT NULL,"
            + KEY_Q_2B_CASSE_HEURE_FIN  + " TEXT NOT NULL,"
            + KEY_STATUS                + " TEXT NOT NULL"
            + ")";

    // Config table create statement
    public static final String CREATE_TABLE_RPR = "CREATE TABLE "
            + TABLE_RPR
            + "("
            + KEY_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_FORETAGKOD   + " TEXT NOT NULL,"
            + KEY_ARTNR        + " TEXT NOT NULL,"
            + KEY_FTGNR        + " TEXT NOT NULL,"
            + KEY_Q_PVC_VAL    + " TEXT NOT NULL,"
            + KEY_ARTSTRECKKOD + " TEXT NOT NULL,"
            + KEY_ARTNRKUND    + " TEXT NOT NULL"
            + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_ARTICLE);
        db.execSQL(CREATE_TABLE_ARTICLE_CASSE);
        db.execSQL(CREATE_TABLE_CLIENT);
        db.execSQL(CREATE_TABLE_CONFIG);
        db.execSQL(CREATE_TABLE_CASSE);
        db.execSQL(CREATE_TABLE_RPR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE_CASSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RPR);

        // create new tables
        onCreate(db);
    }

}

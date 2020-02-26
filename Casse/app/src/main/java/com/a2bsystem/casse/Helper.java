package com.a2bsystem.casse;

import android.app.Activity;
import android.content.SharedPreferences;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.a2bsystem.casse.Activities.config.Config.Config;


public class Helper {

    // Vendeur
    public static String User;
    public static String Mdp;


    public static JSONArray GetList(byte[] source) {
        String s = new String(source);
        try {
            return new JSONObject(s).getJSONArray("recordsets").getJSONArray(0);
        } catch (JSONException e) {
            return null;
        }
    }

    // Retourne la colonne x
    public static JSONObject GetRowAt(byte[] source, int index) {
        try {
            return GetList(source).getJSONObject(index);
        } catch (JSONException e) {
            return null;
        }
    }

    // Génère les paramètres de la requêtes depuis la config
    public static RequestParams GenerateParams(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getApplicationContext().getSharedPreferences(Config, 0);
        String BDD = sharedPreferences.getString("BDD",null);
        String Foretagkod = sharedPreferences.getString("Foretagkod",null);
        RequestParams params = new RequestParams();
        params.setUseJsonStreamer(true);
        params.put("BDD", BDD);
        params.put("Foretagkod", Foretagkod);
        params.put("User", User);
        params.put("Mdp", Mdp);
        return params;
    }

    // Génère l'url
    public static String GenereateURI(Activity activity, RequestParams params, String route) {
        SharedPreferences sharedPreferences = activity.getApplicationContext().getSharedPreferences(Config, 0);
        String URL = sharedPreferences.getString("URL",null);

            return "http://"
                    + URL
                    + "/Casse/"
                    + route
                    +"?"
                    + params.toString();
    }

    public static String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


}

package com.a2bsystem.casse.http;


import android.app.Activity;
import android.content.SharedPreferences;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.Date;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.a2bsystem.casse.Activities.config.Config.Config;

public class Helper {

    // Préparateur
    public static String Preparateur;

    // Récupère le statut de la requête
    public static Boolean GetSuccess(byte[] source) {
        String s = new String(source);
        try {
            return(new JSONObject(s).getInt("status")==100);
        }catch(JSONException e) {
            return false;
        }
    }

    //Retourne la premiere colonne
    public static JSONObject GetFirstRow(byte[] source) {
        return GetRowAt(source, 0);
    }

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

    // Retourne le statut de la requête
    public static int GetStatus(byte[] source)
    {
        String s = new String(source);

        try
        {
            return new JSONObject(s).getInt("status");
        }
        catch(JSONException e)
        {
            return 0;
        }
    }

    // Génère les paramètres de la requêtes depuis la config
    public static RequestParams GenerateParams(Activity activity, String route)
    {
        SharedPreferences sharedPreferences = activity.getApplicationContext().getSharedPreferences(Config, 0);
        String ApiNode = sharedPreferences.getString("ApiNode",null);
        RequestParams params = new RequestParams();
        params.setUseJsonStreamer(true);
        params.put("api_node", ApiNode);
        params.put("act", route);
        return params;
    }

    // Génère l'url
    public static String GenereateURI(Activity activity, RequestParams params)
    {
        SharedPreferences sharedPreferences = activity.getApplicationContext().getSharedPreferences(Config, 0);
        String URL = sharedPreferences.getString("Url",null);

        return URL + "/api.php?" + params.toString();
    }

    public static String getDate()
    {
        Date date = new Date(System.currentTimeMillis());
        return date.toString();
    }

    public static SSLContext getSslContext() {

        TrustManager[] byPassTrustManagers = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        } };

        SSLContext sslContext=null;

        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sslContext.init(null, byPassTrustManagers, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return sslContext;
    }
}

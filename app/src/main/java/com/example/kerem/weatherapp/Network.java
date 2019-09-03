package com.example.kerem.weatherapp;


import android.content.Context;
import android.net.ConnectivityManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;




public class Network {

    public static boolean Internet(Context context)
    {
        return ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo()!=null;
    }


    public static String getData(String src) {
        URL url;
        HttpURLConnection connection;
        try {
            url = new URL(src);
            connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\n');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            return null;
        }

    }
}
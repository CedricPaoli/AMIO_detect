package com.example.amio_detect.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.amio_detect.ui.home.HomeFragment;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetSensors extends AsyncTask<String , Void ,String> {
    private ArrayList<Data> result = new ArrayList<>();
    private final WeakReference<Context> weakActivity;

    public GetSensors(Context mainActivity) {
        this.weakActivity = new WeakReference<>(mainActivity);
    }

    /** Envoi de la requete GET en HTTP **/
    @Override
    protected String doInBackground(String... strings) {
        URL url;
        HttpURLConnection urlConnection = null;
        InputStream stream;

        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(3000);
            urlConnection.setDoInput(true);
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                stream = urlConnection.getInputStream();

                if (stream != null) {
                    MyParser parser = new MyParser();
                    this.result = parser.readJsonStream(stream);
                }

                Log.e("HTTP", "Success");
            } else {
                Context context = this.weakActivity.get();

                if (context == null)
                    return null;

                Log.e("HTTP", "Error");
                Toast.makeText(context, "Erreur HTTP: " + responseCode, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return null;
    }

    /** Si le systeme n'est pas connecté à internet, on annule la connexion **/
    @Override
    protected void onPreExecute() {
        Context context = weakActivity.get();

        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();

            if (networkInfo == null || !networkInfo.isConnected() || (networkInfo.getType() != ConnectivityManager.TYPE_WIFI && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                Toast.makeText(context, "Erreur: pas de connexion", Toast.LENGTH_SHORT).show();
                cancel(true);
            }
        }
    }

    /** On charge les données récupérées sur la page **/
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        HomeFragment.loadData(this.result);
    }
}

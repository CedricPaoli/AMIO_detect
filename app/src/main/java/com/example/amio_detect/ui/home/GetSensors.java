package com.example.amio_detect.ui.home;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetSensors extends AsyncTask<String , Void ,String> {
    private List result = new ArrayList();
    private final WeakReference<Activity> weakActivity;
    private final HomeFragment fragment;

    public GetSensors(Activity mainActivity, HomeFragment fragment) {
        this.weakActivity = new WeakReference<>(mainActivity);
        this.fragment = fragment;
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
                    result = parser.readJsonStream(stream);
                }
            } else {
                Activity activity = weakActivity.get();

                if (activity == null || activity.isFinishing() || activity.isDestroyed())
                    return null;

                Log.e("HTTP", "Success");
                Toast.makeText(activity, "Erreur HTTP:" + responseCode, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return null;
    }

    /** If no connectivity, cancel task**/
    @Override
    protected void onPreExecute() {
        Activity activity = weakActivity.get();

        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();

            if (networkInfo == null || !networkInfo.isConnected() || (networkInfo.getType() != ConnectivityManager.TYPE_WIFI && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                Toast.makeText(activity, "Erreur: pas de connexion", Toast.LENGTH_SHORT).show();
                cancel(true);
            }
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.e("HTTP", "Success");

        this.fragment.loadData(result);
    }
}

package com.example.amio_detect.utils;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.amio_detect.MainActivity;
import com.example.amio_detect.MainService;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetSensors extends AsyncTask<String , Void ,String> {
    private ArrayList<Data> result = new ArrayList<>();
    private final WeakReference<Context> weakContext;
    private final WeakReference<Service> weakService;

    public GetSensors(Context context, Service mainService) {
        this.weakContext = new WeakReference<>(context);
        this.weakService = new WeakReference<>(mainService);
    }

    /** Envoi de la requete GET en HTTP **/
    @Override
    protected String doInBackground(String... strings) {
        URL url;
        HttpURLConnection urlConnection = null;
        InputStream stream;
        int responseCode = 0;

        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(3000);
            urlConnection.setDoInput(true);
            urlConnection.connect();

            responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                stream = urlConnection.getInputStream();

                if (stream != null) {
                    MyParser parser = new MyParser();
                    this.result = parser.readJsonStream(stream);
                }

                Log.e("HTTP", "Success");
            }
        // Erreur HTTP, affichage d'un toast
        } catch (IOException e) {
            Context context = this.weakContext.get();

            if (context == null)
                return null;

            Log.e("HTTP", "Error");

            if(context instanceof MainActivity)
                Toast.makeText(context, "Erreur HTTP: " + responseCode, Toast.LENGTH_SHORT).show();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return null;
    }

    /** Si le systeme n'est pas connecté à internet, on annule la connexion **/
    @Override
    protected void onPreExecute() {
        Context context = this.weakContext.get();

        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();

            if (networkInfo == null || !networkInfo.isConnected() || (networkInfo.getType() != ConnectivityManager.TYPE_WIFI && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                if(context instanceof MainActivity)
                    Toast.makeText(context, "Erreur: pas de connexion", Toast.LENGTH_SHORT).show();

                cancel(true);
            }
        }
    }

    /** On envoi le résultat au service pour les notifications et les mails et on met à jour le fragment si l'application est ouverte **/
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Context context = weakContext.get();
        Context service = weakService.get();

        if(context instanceof MainActivity)
            ((MainActivity) context).updateRecyclerView(this.result);

        if(service != null)
            ((MainService) service).updateList(this.result);
    }
}

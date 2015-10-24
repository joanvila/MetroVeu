package com.metroveu.metroveu.tasks;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.metroveu.metroveu.data.MetroContract;
import com.metroveu.metroveu.data.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Florencia Tarditti on 13/10/15.
 */
public class FetchParadesTask extends AsyncTask<String, Void, String[]> {

    private final String LOG_TAG = FetchParadesTask.class.getSimpleName();
    private final Context mContext;

    public FetchParadesTask(Context context) {
        mContext = context;
    }

    long addMapa(String nomMapa) {

        long mapaId;

        Cursor mapaCursor = mContext.getContentResolver().query(
                MetroContract.MapaEntry.CONTENT_URI,
                null,
                MetroContract.MapaEntry.COLUMN_MAPA_NOM + " = ?",
                new String[]{nomMapa},
                null);

        if (mapaCursor.moveToFirst()) {
            int mapaNomIndex = mapaCursor.getColumnIndex(MetroContract.MapaEntry.COLUMN_MAPA_NOM);
            mapaId = mapaCursor.getLong(mapaNomIndex);
        } else {

            ContentValues mapaValues = new ContentValues();
            mapaValues.put(MetroContract.MapaEntry.COLUMN_MAPA_NOM, nomMapa);

            Uri insertedUri = mContext.getContentResolver().insert(
                    MetroContract.MapaEntry.CONTENT_URI,
                    mapaValues
            );

            mapaId = ContentUris.parseId(insertedUri);
        }

        mapaCursor.close();
        return mapaId;
    }

    long addLinia(String nomLinia, int ordreLinia, String colorLinia, String frequencia, String nomMapa) {
        long liniaId;

        Cursor liniaCursor = mContext.getContentResolver().query(
                MetroContract.LiniaEntry.CONTENT_URI,
                null,
                MetroContract.LiniaEntry.COLUMN_LINIA_NOM + " = ?",
                new String[]{nomLinia},
                null);

        if (liniaCursor.moveToFirst()) {
            int liniaNomIndex = liniaCursor.getColumnIndex(MetroContract.LiniaEntry.COLUMN_LINIA_NOM);
            liniaId = liniaCursor.getLong(liniaNomIndex);
        } else {
            ContentValues liniaValues = new ContentValues();
            liniaValues.put(MetroContract.LiniaEntry.COLUMN_LINIA_NOM, nomLinia);
            liniaValues.put(MetroContract.LiniaEntry.COLUMN_LINIA_ORDRE, ordreLinia);
            liniaValues.put(MetroContract.LiniaEntry.COLUMN_LINIA_COLOR, colorLinia);
            liniaValues.put(MetroContract.LiniaEntry.COLUMN_LINIA_FREQUENCIA, frequencia);
            liniaValues.put(MetroContract.LiniaEntry.COLUMN_LINIA_MAPA, nomMapa);

            Uri insertedUri = mContext.getContentResolver().insert(
                    MetroContract.LiniaEntry.CONTENT_URI,
                    liniaValues
            );

            liniaId = ContentUris.parseId(insertedUri);
        }

        liniaCursor.close();
        return liniaId;
    }



    long addPertany(String mapa, String linia, String parada) {
        long pertanyId;

        Cursor pertanyCursor = mContext.getContentResolver().query(
                MetroContract.PertanyEntry.CONTENT_URI,
                null,
                MetroContract.PertanyEntry.COLUMN_PERTANY_MAPA + " = ? AND " +
                        MetroContract.PertanyEntry.COLUMN_PERTANY_LINIA + " = ? AND " +
                        MetroContract.PertanyEntry.COLUMN_PERTANY_PARADA + " = ?",
                new String[]{mapa, linia, parada},
                null);

        if (pertanyCursor.moveToFirst()) {
            int pertanyIndex = pertanyCursor.getColumnIndex(MetroContract.PertanyEntry.COLUMN_PERTANY_MAPA);
            pertanyId = pertanyCursor.getLong(pertanyIndex);
        } else {
            ContentValues pertanyValues = new ContentValues();
            pertanyValues.put(MetroContract.PertanyEntry.COLUMN_PERTANY_MAPA, mapa);
            pertanyValues.put(MetroContract.PertanyEntry.COLUMN_PERTANY_LINIA, linia);
            pertanyValues.put(MetroContract.PertanyEntry.COLUMN_PERTANY_PARADA, parada);

            Uri insertedUri = mContext.getContentResolver().insert(
                    MetroContract.PertanyEntry.CONTENT_URI,
                    pertanyValues
            );

            pertanyId = ContentUris.parseId(insertedUri);
        }

        pertanyCursor.close();
        return pertanyId;
    }

    long addParada(String nomParada, String accessibilitat) {
        long paradaId;

        Cursor paradaCursor = mContext.getContentResolver().query(
                MetroContract.ParadaEntry.CONTENT_URI,
                null,
                MetroContract.ParadaEntry.COLUMN_PARADA_NOM + " = ?",
                new String[] {nomParada},
                null);

        if (paradaCursor.moveToFirst()) {
            int paradaNomIndex = paradaCursor.getColumnIndex(MetroContract.ParadaEntry.COLUMN_PARADA_NOM);
            paradaId = paradaCursor.getLong(paradaNomIndex);
        } else {
            ContentValues paradaValues = new ContentValues();
            paradaValues.put(MetroContract.ParadaEntry.COLUMN_PARADA_NOM, nomParada);
            paradaValues.put(MetroContract.ParadaEntry.COLUMN_PARADA_ACCESSIBILITAT, accessibilitat);

            Uri insertedUri = mContext.getContentResolver().insert(
                    MetroContract.ParadaEntry.CONTENT_URI,
                    paradaValues
            );

            paradaId = ContentUris.parseId(insertedUri);
        }

        paradaCursor.close();
        return paradaId;
    }

    private String[] getParadesDataFromJson(String paradesJsonStr)
            throws JSONException {

        try {
            JSONObject paradesJson = new JSONObject(paradesJsonStr);
            paradesJson = paradesJson.getJSONObject("data");
            JSONArray metroArray = paradesJson.getJSONArray("metro");
            ArrayList<Pair<String, String>> paradesArray = new ArrayList<>();
            ArrayList<Pair<Integer,String>> liniesArray = new ArrayList<>();
            ArrayList<Pair<String,String>> pertanyensaList = new ArrayList<>();
            String liniaNom;
            for (int i = 0; i < metroArray.length(); ++i) {
                String parada = metroArray.getJSONObject(i).getString("name");
                String accessibilitatParada = metroArray.getJSONObject(i).getString("accessibility");
                paradesArray.add(new Pair(parada, accessibilitatParada));
                liniaNom = metroArray.getJSONObject(i).getString("line");
                if (liniaNom.equals("L9|L10")) {
                    int linia9Index = 5; //TODO: Maybe there is a way to get the index
                    int linia10Index = 6;
                    pertanyensaList.add(new Pair("L9", parada));
                    pertanyensaList.add(new Pair("L10", parada));
                    if (!liniesArray.contains(new Pair(linia9Index, "L9"))) {
                        liniesArray.add(new Pair(linia9Index, "L9"));
                    }
                    if (!liniesArray.contains(new Pair(linia10Index, "L10"))) {
                        liniesArray.add(new Pair(linia10Index, "L10"));
                    }
                } else {
                    int liniaIndex = Integer.parseInt(metroArray.getJSONObject(i).getString("lineorder"));
                    pertanyensaList.add(new Pair(liniaNom, parada));
                    if (!liniesArray.contains(new Pair(liniaIndex, liniaNom))) {
                        liniesArray.add(new Pair(liniaIndex, liniaNom));
                    }
                }
            }

            addMapa("Barcelona");
            for (int i = 0; i < liniesArray.size(); ++i) {
                addLinia(liniesArray.get(i).getR(), liniesArray.get(i).getL(), null, null, "Barcelona");
            }

            for (int i = 0; i < paradesArray.size(); ++i) {
                addParada(paradesArray.get(i).getL(), paradesArray.get(i).getR());
            }
            for (int i = 0; i < pertanyensaList.size(); ++i) {
                addPertany("Barcelona", pertanyensaList.get(i).getL(), pertanyensaList.get(i).getR());
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected String[] doInBackground(String... params) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String paradesJsonStr = null;

        try {
            // Construct the URL for the barcelonaapi query
            final String PARADES_URL = "https://bcn-metro-api.herokuapp.com/stations";
            Log.v(LOG_TAG, "URL DEFINIDO");

            Uri builtUri = Uri.parse(PARADES_URL);

            URL url = new URL(builtUri.toString());

            // Create the request to API, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            Log.v(LOG_TAG, "CONEXION HECHA");

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            paradesJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getParadesDataFromJson(paradesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }
}

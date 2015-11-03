package com.metroveu.metroveu.tasks;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.metroveu.metroveu.data.MetroContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

        if (mapaCursor != null && mapaCursor.moveToFirst()) {
            int mapaNomIndex = mapaCursor.getColumnIndex(MetroContract.MapaEntry.COLUMN_MAPA_NOM);
            mapaId = mapaCursor.getLong(mapaNomIndex);
            mapaCursor.close();
        } else {

            ContentValues mapaValues = new ContentValues();
            mapaValues.put(MetroContract.MapaEntry.COLUMN_MAPA_NOM, nomMapa);

            Uri insertedUri = mContext.getContentResolver().insert(
                    MetroContract.MapaEntry.CONTENT_URI,
                    mapaValues
            );

            mapaId = ContentUris.parseId(insertedUri);
        }
        return mapaId;
    }

    long addLinia(String nomLinia, int ordreLinia, String colorLinia, int frequencia, String nomMapa) {
        long liniaId;

        Cursor liniaCursor = mContext.getContentResolver().query(
                MetroContract.LiniaEntry.CONTENT_URI,
                null,
                MetroContract.LiniaEntry.COLUMN_LINIA_NOM + " = ?",
                new String[]{nomLinia},
                null);

        if (liniaCursor != null && liniaCursor.moveToFirst()) {
            int liniaNomIndex = liniaCursor.getColumnIndex(MetroContract.LiniaEntry.COLUMN_LINIA_NOM);
            liniaId = liniaCursor.getLong(liniaNomIndex);
            liniaCursor.close();
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
        return liniaId;
    }

    long addTarifa(String tarifa_nom, String tarifa_tipus, String tarifa_descripcio, String tarifa_preu, String nomMapa, String idioma) {
        long tarifaId;

        Cursor tarifaCursor = mContext.getContentResolver().query(
                MetroContract.TarifaEntry.CONTENT_URI,
                null,
                MetroContract.TarifaEntry.COLUMN_TARIFA_NOM + " = ?",
                new String[]{tarifa_nom},
                null);

        if (tarifaCursor != null && tarifaCursor.moveToFirst()) {
            int tarifaNomIndex = tarifaCursor.getColumnIndex(MetroContract.TarifaEntry.COLUMN_TARIFA_NOM);
            tarifaId = tarifaCursor.getLong(tarifaNomIndex);
            tarifaCursor.close();
        } else {
            ContentValues tarifaValues = new ContentValues();
            tarifaValues.put(MetroContract.TarifaEntry.COLUMN_TARIFA_NOM, tarifa_nom);
            tarifaValues.put(MetroContract.TarifaEntry.COLUMN_TARIFA_TIPUS, tarifa_tipus);
            tarifaValues.put(MetroContract.TarifaEntry.COLUMN_TARIFA_DESCRIPCIO, tarifa_descripcio);
            tarifaValues.put(MetroContract.TarifaEntry.COLUMN_TARIFA_PREU, tarifa_preu);
            tarifaValues.put(MetroContract.TarifaEntry.COLUMN_TARIFA_MAPA, nomMapa);
            tarifaValues.put(MetroContract.TarifaEntry.COLUMN_TARIFA_IDIOMA, idioma);

            Uri insertedUri = mContext.getContentResolver().insert(
                    MetroContract.TarifaEntry.CONTENT_URI,
                    tarifaValues
            );

            tarifaId = ContentUris.parseId(insertedUri);
        }
        return tarifaId;
    }


    long addPertany(String mapa, String linia, String parada, int ordre) {
        long pertanyId;

        Cursor pertanyCursor = mContext.getContentResolver().query(
                MetroContract.PertanyEntry.CONTENT_URI,
                null,
                MetroContract.PertanyEntry.COLUMN_PERTANY_MAPA + " = ? AND " +
                        MetroContract.PertanyEntry.COLUMN_PERTANY_LINIA + " = ? AND " +
                        MetroContract.PertanyEntry.COLUMN_PERTANY_PARADA + " = ?",
                new String[]{mapa, linia, parada},
                null);

        if (pertanyCursor != null && pertanyCursor.moveToFirst()) {
            int pertanyIndex = pertanyCursor.getColumnIndex(MetroContract.PertanyEntry.COLUMN_PERTANY_MAPA);
            pertanyId = pertanyCursor.getLong(pertanyIndex);
            pertanyCursor.close();
        } else {
            ContentValues pertanyValues = new ContentValues();
            pertanyValues.put(MetroContract.PertanyEntry.COLUMN_PERTANY_MAPA, mapa);
            pertanyValues.put(MetroContract.PertanyEntry.COLUMN_PERTANY_LINIA, linia);
            pertanyValues.put(MetroContract.PertanyEntry.COLUMN_PERTANY_PARADA, parada);
            pertanyValues.put(MetroContract.PertanyEntry.COLUMN_PERTANY_ORDRE, ordre);

            Uri insertedUri = mContext.getContentResolver().insert(
                    MetroContract.PertanyEntry.CONTENT_URI,
                    pertanyValues
            );

            pertanyId = ContentUris.parseId(insertedUri);
        }
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
            JSONObject linesColor = paradesJson.getJSONObject("linesColor");
            JSONArray linesOrder = paradesJson.getJSONArray("linesOrder");

            addMapa("Barcelona");

            //Linies
            for (int i = 0; i < linesOrder.length(); ++i) {
                String lineName = linesOrder.get(i).toString();
                String lineColor = linesColor.getString(lineName.replaceAll(" ", ""));
                addLinia(lineName, i, lineColor, -1, "Barcelona");
            }

            //Parades
            for (int i = 0; i < metroArray.length(); ++i) {
                String nomParada = metroArray.getJSONObject(i).getString("name");
                String accessibilitatParada = metroArray.getJSONObject(i).getString("accessibility");
                String liniaParada = metroArray.getJSONObject(i).getString("line");
                int ordreParada = metroArray.getJSONObject(i).getInt("paradaorder");

                addParada(nomParada, accessibilitatParada);
                addPertany("Barcelona", liniaParada, nomParada, ordreParada);
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

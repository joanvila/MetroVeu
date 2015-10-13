package com.metroveu.metroveu.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by Florencia Tarditti on 13/10/15.
 */
public class FetchParadesTask extends AsyncTask<String, Void, String[]> {

    private final String LOG_TAG = FetchParadesTask.class.getSimpleName();

    public FetchParadesTask() {

    }

    private String[] getParadesDataFromJson(String paradesJsonStr)
            throws JSONException {

        try {
            JSONObject paradesJson = new JSONObject(paradesJsonStr);
            paradesJson = paradesJson.getJSONObject("data");
            JSONArray metroArray = paradesJson.getJSONArray("metro");
            String[] paradesArray = new String[metroArray.length()];
            for (int i = 0; i < metroArray.length(); ++i) {
                paradesArray[i] = metroArray.getJSONObject(i).getString("name");
            }
            Log.v(LOG_TAG, Arrays.toString(paradesArray));
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
            final String PARADES_URL = "http://barcelonaapi.marcpous.com/metro/stations.json";

            Uri builtUri = Uri.parse(PARADES_URL);

            URL url = new URL(builtUri.toString());

            // Create the request to API, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

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
            // If the code didn't successfully get the station data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
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
        // This will only happen if there was an error getting or parsing the stations.
        return null;
    }
}

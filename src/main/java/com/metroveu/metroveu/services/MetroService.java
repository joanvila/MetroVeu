package com.metroveu.metroveu.services;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.metroveu.metroveu.data.MetroContract;

import java.util.ArrayList;

public class MetroService extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mGoogleApiClient;
    private static final String SHOW_LINES = "/show_lines";

    private void initGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        mGoogleApiClient.connect();
    }

    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                String path = dataEvent.getDataItem().getUri().getPath();
                if (path.equals("/get-lines")) {

                }
            }
        }
    }

    public void getLines() {
        ContentResolver contentResolver = getContentResolver();
        Cursor linies = contentResolver.query(MetroContract.LiniaEntry.CONTENT_URI,
                null,
                MetroContract.LiniaEntry.COLUMN_LINIA_NOM,
                null,
                null);
        ArrayList<String> liniesNames = new ArrayList<>();
        if (linies != null && linies.moveToFirst()){
            do {
                liniesNames.add(linies.getString(linies.getColumnIndex("linia_nom")));
            } while(linies.moveToNext());
            linies.close();
        }

        String listString = "";

        for (String s : liniesNames)
        {
            listString += s + "\t";
        }

        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/count");
        putDataMapReq.getDataMap().putString(SHOW_LINES, listString);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
    }

    public ArrayList<String> getLinies() {
        ContentResolver contentResolver = getContentResolver();
        Cursor linies = contentResolver.query(MetroContract.LiniaEntry.CONTENT_URI,
                null,
                MetroContract.LiniaEntry.COLUMN_LINIA_NOM,
                null,
                null);
        ArrayList<String> liniesNames = new ArrayList<>();
        if (linies != null && linies.moveToFirst()){
            do {
                liniesNames.add(linies.getString(linies.getColumnIndex("linia_nom")));
            } while(linies.moveToNext());
            linies.close();
        }
        return liniesNames;
    }

    @Override
    public void onConnected(Bundle bundle) {
        sendMessage(SHOW_LINES, String.valueOf(getLinies()));
    }

    private void sendMessage(final String path, final String text) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mGoogleApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mGoogleApiClient, node.getId(), path, text.getBytes() ).await();
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}

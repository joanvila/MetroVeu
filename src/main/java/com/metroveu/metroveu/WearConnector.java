package com.metroveu.metroveu;

import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class WearConnector extends WearableListenerService {

    public void onMessageReceived(MessageEvent messageEvent) {
        System.out.println("Message Received on Phone on launch of wear homepage");
        if(messageEvent.getPath().equals("/lines")) {
            //sendSavedDeals(); //fetch from db and make a datamap object using PutDataRequest
            Log.v("JOAN", "Message received");
        }
        else {
            Log.v("JOAN", "Wrong path");
        }

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                String path = dataEvent.getDataItem().getUri().getPath();
                if (path.equals("/lines")) {
                    int number = dataMap.getInt("number");
                    Log.v("JOAN", Integer.toString(number));
                }
            }
        }
    }
}

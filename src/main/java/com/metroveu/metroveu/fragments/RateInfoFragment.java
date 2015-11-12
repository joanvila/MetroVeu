package com.metroveu.metroveu.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;

import com.metroveu.metroveu.R;
import com.metroveu.metroveu.data.MetroDbHelper;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Carla on 12/11/2015.
 */
public class RateInfoFragment extends Fragment {
    private android.app.FragmentTransaction ft;
    private TextView nomTarifaView = null;
    private TextView descripcioTarifaView = null;
    private TextView preuTarifaView = null;
    private String descrT;
    private String preuT;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.rate_fragment, container, false);


        Bundle tarifaBundle = getArguments();
        final String nomT= tarifaBundle.getString("tarifa_nom");
        //final String descrT = tarifaBundle.getString("tarifa_descripcio");
        //final String preuT = tarifaBundle.getString("tarifa_preu");

        Cursor tarifa = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from tarifa where tarifa_nom =?", new String[]{nomT});

        if (tarifa != null && tarifa.moveToFirst()){
            descrT = tarifa.getString(tarifa.getColumnIndex("tarifa_descripcio"));
            preuT = tarifa.getString(tarifa.getColumnIndex("tarifa_preu"));
            tarifa.close();
        }


        nomTarifaView = (TextView) rootView.findViewById(R.id.nomTarifa);
        nomTarifaView.setText(nomT);
        descripcioTarifaView = (TextView) rootView.findViewById(R.id.descripcioTarifa);
        descripcioTarifaView.setText(descrT);
        preuTarifaView = (TextView) rootView.findViewById(R.id.preuTarifa);
        preuTarifaView.setText(preuT);

        return rootView;

    }
}

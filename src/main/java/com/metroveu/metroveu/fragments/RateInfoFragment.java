package com.metroveu.metroveu.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metroveu.metroveu.R;
import com.metroveu.metroveu.data.MetroDbHelper;

/**
 * Created by Carla on 12/11/2015.
 */
public class RateInfoFragment extends Fragment {

    private android.app.FragmentTransaction ft;
    private String descrT;
    private String preuT;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.rate_fragment, container, false);

        Bundle tarifaBundle = getArguments();
        final String nomT= tarifaBundle.getString("tarifa_nom");
        final String idiomaT = tarifaBundle.getString("tarifa_idioma");

        Cursor tarifa = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from tarifa where tarifa_nom = ? and tarifa_idioma = ?", new String[]{nomT, idiomaT});

        if (tarifa != null && tarifa.moveToFirst()){
            descrT = tarifa.getString(tarifa.getColumnIndex("tarifa_descripcio"));
            preuT = tarifa.getString(tarifa.getColumnIndex("tarifa_preu"));
            tarifa.close();
        }

        TextView nomTarifaView = (TextView) rootView.findViewById(R.id.nomTarifa);
        nomTarifaView.setText(nomT);
        TextView descripcioTarifaView = (TextView) rootView.findViewById(R.id.descripcioTarifa);
        descripcioTarifaView.setText(descrT);
        TextView preuTarifaView = (TextView) rootView.findViewById(R.id.preuTarifa);
        preuTarifaView.setText(preuT);

        return rootView;

    }
}

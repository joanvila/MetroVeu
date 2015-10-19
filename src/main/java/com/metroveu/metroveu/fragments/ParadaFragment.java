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
 * Created by Florencia Tarditti on 12/10/15.
 */
public class ParadaFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Creates view and links it to the parada fragment layout in res/layout
        View rootView = inflater.inflate(R.layout.parada_fragment, container, false);

        Bundle paradesBundle = getArguments();
        String parada = paradesBundle.getString("parada_nom");

        Cursor parades = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from parada where parada_nom =?", new String[]{parada});

        String nomParada = "";
        if (parades != null && parades.moveToFirst()){
            nomParada = parades.getString(parades.getColumnIndex("parada_nom"));
            parades.close();
        }

        TextView nomParadaView = (TextView) rootView.findViewById(R.id.nomParada);
        nomParadaView.setText(nomParada);

        return rootView;
    }
}

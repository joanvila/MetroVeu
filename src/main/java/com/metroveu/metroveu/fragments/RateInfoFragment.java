package com.metroveu.metroveu.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metroveu.metroveu.R;
import com.metroveu.metroveu.data.MetroDbHelper;

import java.util.ArrayList;

/**
 * Created by Carla on 12/11/2015.
 */
public class RateInfoFragment extends Fragment {
    private android.app.FragmentTransaction ft;
    ArrayList<String> tarifaData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.rate_fragment, container, false);

        Bundle tarifaBundle = getArguments();
        final String tipusTarifa = tarifaBundle.getString("tarifa_tipus");

        Cursor tarifes = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from tarifa where tarifa_tipus=?", new String[]{tipusTarifa});

        tarifaData = new ArrayList<>();

        if (tarifes != null && tarifes.moveToFirst()) {
            do {
                tarifaData.add(tarifes.getString(tarifes.getColumnIndex("tarifa_nom")));
            } while (tarifes.moveToNext());
            tarifes.close();
        }
        return rootView;

    }
}

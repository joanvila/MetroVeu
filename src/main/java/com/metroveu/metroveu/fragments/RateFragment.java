package com.metroveu.metroveu.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.metroveu.metroveu.R;
import com.metroveu.metroveu.data.MetroDbHelper;

import java.util.ArrayList;

/**
 * Created by Carla on 05/11/2015.
 */
public class RateFragment extends Fragment{
    private android.app.FragmentTransaction ft;
    ArrayList<String> tarifaData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.rates_fragment, container, false);

        Bundle tarifaBundle = getArguments();
        final String tipusTarifa = tarifaBundle.getString("tarifa_tipus");

        Cursor tarifes = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from tarifa whre tarifa_tipus=?", new String[]{tipusTarifa});

        tarifaData = new ArrayList<>();

        if(tarifes != null && tarifes.moveToFirst()) {
            do {
                tarifaData.add(tarifes.getString(tarifes.getColumnIndex("tarifa_nom")));
            } while (tarifes.moveToNext());
            tarifes.close();
        }
        ListView tarifesListView = (ListView) rootView.findViewById(R.id.tarifesListView);
        tarifesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                RateFragment rateFragment = new RateFragment();
                Bundle tarifesBundle = new Bundle();
                tarifesBundle.putString("tarifa_nom", tarifaData.get(position));
                rateFragment.setArguments(tarifesBundle);
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, rateFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return rootView;
    }

}
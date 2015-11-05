package com.metroveu.metroveu.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.metroveu.metroveu.R;
import com.metroveu.metroveu.data.MetroDbHelper;

import java.util.ArrayList;

/**
 * Created by carladivicuesta on 23/10/15.
 */
public class RatesFragment extends Fragment {

    private FragmentTransaction ft;
    ArrayList<String> tarifesData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.rates_list_view, container, false);

        Cursor tarifes = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from tarifa", null);
        tarifesData = new ArrayList<>();
        if(tarifes != null && tarifes.moveToFirst()) {
            do {
                tarifesData.add(tarifes.getString(tarifes.getColumnIndex("tarifa_tipus")));
            } while (tarifes.moveToNext());
            tarifes.close();
        }
        ListView tarifesListView = (ListView) rootView.findViewById(R.id.tarifesListView);
        tarifesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                RatesFragment ratesFragment = new RatesFragment();
                Bundle tarifesBundle = new Bundle();
                tarifesBundle.putString("tarifa_tipus", tarifesData.get(position));
                ratesFragment.setArguments(tarifesBundle);
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, ratesFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return rootView;
    }

}

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
import com.metroveu.metroveu.adapters.LiniesAdapter;
import com.metroveu.metroveu.data.MetroDbHelper;

import java.util.ArrayList;

/**
 * Created by joanvila on 14/10/15.
 */
public class LiniesFragment extends Fragment {

    private FragmentTransaction ft;
    ArrayList<String> liniesData;
    ArrayList<String> liniesColor;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.linies_fragment, container, false);

        Cursor linies = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from linia", null);
        liniesData = new ArrayList<>();
        liniesColor = new ArrayList<>();
        if (linies != null && linies.moveToFirst()){
            do {
                liniesData.add(linies.getString(linies.getColumnIndex("linia_nom")));
                liniesColor.add(linies.getString(linies.getColumnIndex("linia_color")));
            } while(linies.moveToNext());
            linies.close();
        }

        LiniesAdapter lAdapter = new LiniesAdapter(getActivity().getBaseContext(), liniesData, liniesColor);
        ListView liniesListView = (ListView) rootView.findViewById(R.id.liniesListView);
        liniesListView.setAdapter(lAdapter);

        liniesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ParadesFragment paradesFragment = new ParadesFragment();
                Bundle paradesBundle = new Bundle();
                paradesBundle.putString("linia_nom", liniesData.get(position));
                paradesFragment.setArguments(paradesBundle);
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, paradesFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return rootView;
    }
}

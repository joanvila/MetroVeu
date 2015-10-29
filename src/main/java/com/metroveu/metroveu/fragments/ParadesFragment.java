package com.metroveu.metroveu.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * Created by Florencia Tarditti on 19/10/15.
 */
public class ParadesFragment extends Fragment {

    private FragmentTransaction ft;
    ArrayList<String> paradesData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.parades_fragment, container, false);

        Bundle paradesBundle = getArguments();
        final String linia = paradesBundle.getString("linia_nom");

        Cursor liniaInfo = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from linia where linia_nom = ?", new String[] {linia});
        String colorLinia = "#FFF";
        if (liniaInfo != null && liniaInfo.moveToFirst()) {
            colorLinia = liniaInfo.getString(liniaInfo.getColumnIndex("linia_color"));
        }

        TextView liniaTextView = (TextView) rootView.findViewById(R.id.lineName);
        liniaTextView.setText(linia);
        rootView.setBackgroundColor(Color.parseColor(colorLinia));

        Cursor parades = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from pertany where pertany_linia = ? order by pertany_ordre", new String[] {linia});
        paradesData = new ArrayList<>();
        if (parades != null && parades.moveToFirst()){
            do {
                paradesData.add(parades.getString(parades.getColumnIndex("pertany_parada")));
            } while(parades.moveToNext());
            parades.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
                .getApplicationContext(), android.R.layout.simple_list_item_1,
                android.R.id.text1, paradesData);

        ListView paradesListView = (ListView) rootView.findViewById(R.id.paradesListView);
        paradesListView.setAdapter(adapter);

        paradesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ParadaFragment paradaFragment = new ParadaFragment();
                Bundle paradaBundle = new Bundle();
                paradaBundle.putStringArrayList("parades_data", paradesData);
                paradaBundle.putString("parada_nom", paradesData.get(position));
                paradaBundle.putString("linia_nom", linia);
                paradaFragment.setArguments(paradaBundle);
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, paradaFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return rootView;
    }

}

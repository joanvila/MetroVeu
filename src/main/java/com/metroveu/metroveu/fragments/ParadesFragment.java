package com.metroveu.metroveu.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        String linia = paradesBundle.getString("linia_nom");

        Cursor parades = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from pertany where pertany_linia =?", new String[] {linia});
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
                paradaBundle.putString("parada_nom", paradesData.get(position));
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

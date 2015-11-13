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
import java.util.Locale;

/**
 * Created by carladivicuesta on 23/10/15.
 */
public class RatesFragment extends Fragment {

    private FragmentTransaction ft;
    ArrayList<String> tarifesData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.rates_list_view, container, false);

        String idioma = Locale.getDefault().getDisplayLanguage();
        if (idioma != "Catala") idioma = "Catala";
        Cursor tarifes = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from tarifa where tarifa_idioma = ? group by tarifa_tipus", new String[] {idioma});
        tarifesData = new ArrayList<>();
        if(tarifes != null && tarifes.moveToFirst()) {
            do {
                tarifesData.add(tarifes.getString(tarifes.getColumnIndex("tarifa_tipus")));
            } while (tarifes.moveToNext());
            tarifes.close();
        }



        //RatesAdapter RAdapter = new RatesAdapter(getActivity().getBaseContext(), tarifesData);
        //ListView tarifesListView = (ListView) rootView.findViewById(R.id.tarifesListView);
        //tarifesListView.setAdapter(RAdapter);

        ArrayAdapter<String> RAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,android.R.id.text1,tarifesData);
        ListView tarifesListView = (ListView) rootView.findViewById(R.id.tarifesListView);
        tarifesListView.setAdapter(RAdapter);

        tarifesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                RateFragment rateFragment = new RateFragment();
                Bundle tarifesBundle = new Bundle();
                tarifesBundle.putString("tarifa_tipus", tarifesData.get(position));
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

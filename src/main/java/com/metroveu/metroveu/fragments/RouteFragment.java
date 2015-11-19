package com.metroveu.metroveu.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.metroveu.metroveu.R;
import com.metroveu.metroveu.data.MetroDbHelper;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Florencia Tarditti on 12/11/15.
 */
public class RouteFragment extends Fragment {

    private ArrayList<String> routeData;
    ArrayList<String> routeParades;
    private FragmentTransaction ft;
    ArrayList<String> paradesData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.route_fragment, container, false);

        Bundle routeBundle = getArguments();
        final String originalName = routeBundle.getString("original_name");

        Cursor routeInfo = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from ruta where ruta_nom =?", new String[] {originalName});
        routeData = new ArrayList<>();
        if(routeInfo != null && routeInfo.moveToFirst()) {
            do {
                String parades = routeInfo.getString(routeInfo.getColumnIndex("ruta_parades"));
                parades = parades.substring(1, parades.length() - 1);
                parades = parades.replaceAll(",\\s+",",");
                routeParades = new ArrayList<String>(Arrays.asList(parades.split(",")));
            } while (routeInfo.moveToNext());
            routeInfo.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
                .getApplicationContext(), android.R.layout.simple_list_item_1,
                android.R.id.text1, routeParades);

        ListView routesListView = (ListView) rootView.findViewById(R.id.routeListView);
        routesListView.setAdapter(adapter);

        routesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String[] paradaInfo = routeParades.get(position).split("-");

                Cursor liniaInfo = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                        rawQuery("select * from linia where linia_nom = ?", new String[]{paradaInfo[0]});
                String colorLinia = "#FFF";
                if (liniaInfo != null && liniaInfo.moveToFirst()) {
                    colorLinia = liniaInfo.getString(liniaInfo.getColumnIndex("linia_color"));
                    liniaInfo.close();
                }

                Cursor parades = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                        rawQuery("select * from pertany where pertany_linia = ? order by pertany_ordre", new String[] {paradaInfo[0]});
                paradesData = new ArrayList<>();
                if (parades != null && parades.moveToFirst()){
                    do {
                        paradesData.add(parades.getString(parades.getColumnIndex("pertany_parada")));
                    } while(parades.moveToNext());
                    parades.close();
                }

                final String finalColorLinia = colorLinia;

                ParadaFragment paradaFragment = new ParadaFragment();
                Bundle paradaBundle = new Bundle();
                paradaBundle.putStringArrayList("parades_data", paradesData);
                paradaBundle.putString("parada_nom", paradaInfo[1]);
                paradaBundle.putString("linia_nom", paradaInfo[0]);
                paradaBundle.putString("linia_color", finalColorLinia);
                paradaBundle.putBoolean("rutaStarted", false);
                paradaBundle.putStringArrayList("ruta", new ArrayList<String>());
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

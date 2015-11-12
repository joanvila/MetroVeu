package com.metroveu.metroveu.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
                parades = parades.substring(1, parades.length()-1);
                parades = parades.replaceAll(",\\s+",",");
                routeParades = new ArrayList<String>(Arrays.asList(parades.split(",")));
                Log.v("flor parades ruta ", String.valueOf(routeParades));
            } while (routeInfo.moveToNext());
            routeInfo.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
                .getApplicationContext(), android.R.layout.simple_list_item_1,
                android.R.id.text1, routeParades);

        ListView routesListView = (ListView) rootView.findViewById(R.id.routeListView);
        routesListView.setAdapter(adapter);

        return rootView;
    }
}

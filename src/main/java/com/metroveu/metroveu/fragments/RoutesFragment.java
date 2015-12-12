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
import com.metroveu.metroveu.adapters.RoutesAdapter;
import com.metroveu.metroveu.data.MetroDbHelper;

import java.util.ArrayList;

/**
 * Created by Florencia Tarditti on 11/11/15.
 */
public class RoutesFragment extends Fragment {

    private FragmentTransaction ft;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.routes_fragment, container, false);

        Cursor routes = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from ruta", null);
        ArrayList<String> routesData = new ArrayList<>();
        final ArrayList<String> originalNames = new ArrayList<>();
        if(routes != null && routes.moveToFirst()) {
            do {
                String rutaName = routes.getString(routes.getColumnIndex("ruta_nom"));
                originalNames.add(rutaName);
                String[] routeNames = rutaName.split("/");
                String partOne = routeNames[0];
                String partTwo = routeNames[1];
                String newRouteName = getResources().getString(R.string.de) + " " + partOne +
                        getResources().getString(R.string.a) + partTwo;
                routesData.add(newRouteName);
            } while (routes.moveToNext());
            routes.close();
        }

        RoutesAdapter adapter = new RoutesAdapter(getActivity().getBaseContext(), routesData);
        ListView routesListView = (ListView) rootView.findViewById(R.id.routesListView);
        routesListView.setAdapter(adapter);

        routesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RouteFragment routeFragment = new RouteFragment();
                Bundle routeBundle = new Bundle();
                routeBundle.putString("original_name", originalNames.get(position));
                routeFragment.setArguments(routeBundle);
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, routeFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return rootView;
    }
}

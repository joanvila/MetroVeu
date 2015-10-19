package com.metroveu.metroveu.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.metroveu.metroveu.R;
import com.metroveu.metroveu.data.MetroDbHelper;

import java.util.ArrayList;

/**
 * Created by joanvila on 14/10/15.
 */
public class LiniesFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.linies_fragment, container, false);

        ListView liniesListView = (ListView) rootView.findViewById(R.id.liniesListView);

        Cursor linies = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from linia order by linia_nom asc", null);
        ArrayList<String> data = new ArrayList<>();
        if (linies != null && linies.moveToFirst()){
            do {
                data.add(linies.getString(linies.getColumnIndex("linia_nom")));
            } while(linies.moveToNext());
            linies.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
                .getApplicationContext(), android.R.layout.simple_list_item_1,
                android.R.id.text1, data);

        liniesListView.setAdapter(adapter);

        return rootView;
    }
}

package com.metroveu.metroveu.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.metroveu.metroveu.R;
import com.metroveu.metroveu.activities.MainActivity;

import java.util.ArrayList;

/**
 * Created by joanvila on 14/10/15.
 */
public class LiniesFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.linies_fragment, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        /*Defined Array values to show in ListView
        String[] values = new String[] { "Android List View",
                "Adapter implementation", "Simple List View In Android",
                "Create List View Android", "Android Example",
                "List View Source Code", "List View Array Adapter",
                "Android Example List View" };*/

        MainActivity activity = (MainActivity) getActivity();
        String[] values = activity.getMyData().split(" ");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
                .getApplicationContext(), android.R.layout.simple_list_item_1,
                android.R.id.text1, values);

        listView.setAdapter(adapter);

        return rootView;
    }
}

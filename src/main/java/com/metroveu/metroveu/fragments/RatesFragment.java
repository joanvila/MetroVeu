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
 * Created by carladivicuesta on 23/10/15.
 */
public class RatesFragment extends Fragment {

    private FragmentTransaction ft;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.rate_fragment, container, false);

        Cursor tarifes = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from tarifa", null);


        return rootView;
    }
}

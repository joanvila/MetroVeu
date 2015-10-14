package com.metroveu.metroveu.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metroveu.metroveu.R;

/**
 * Created by joanvila on 14/10/15.
 */
public class HomeFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Creates view and links it to the parada fragment layout in res/layout
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);

        return rootView;
    }

}

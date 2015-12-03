package com.metroveu.metroveu.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.metroveu.metroveu.R;

/**
 * Created by joanvila on 14/10/15.
 */
public class HomeFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home_fragment, container, false);

        Bundle homeBundle = getArguments();
        int color = homeBundle.getInt("button_color");

        Button showLines = (Button) rootView.findViewById(R.id.show_lines_button);
        Button showRoutes = (Button) rootView.findViewById(R.id.show_routes_button);
        Button showRates = (Button) rootView.findViewById(R.id.show_rate_button);
        Button showConfig = (Button) rootView.findViewById(R.id.configuration_button);

        showLines.setBackgroundColor(getResources().getColor(color));
        showRoutes.setBackgroundColor(getResources().getColor(color));
        showRates.setBackgroundColor(getResources().getColor(color));
        showConfig.setBackgroundColor(getResources().getColor(color));

        return rootView;
    }

}

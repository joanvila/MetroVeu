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
public class ConfigFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.config_fragment, container, false);
    }

}

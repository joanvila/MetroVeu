package com.metroveu.metroveu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metroveu.metroveu.R;


/**
 * Created by Carla on 13/12/2015.
 */
public class AboutFragment extends ConfigFragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_info, container, false);


        return rootView;

    }
}
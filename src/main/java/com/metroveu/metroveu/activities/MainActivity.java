package com.metroveu.metroveu.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.metroveu.metroveu.R;
import com.metroveu.metroveu.fragments.HomeFragment;
import com.metroveu.metroveu.fragments.LiniesFragment;
import com.metroveu.metroveu.fragments.ParadaFragment;
import com.metroveu.metroveu.fragments.RatesFragment;
import com.metroveu.metroveu.tasks.FetchParadesTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FragmentTransaction ft;
    private ParadaFragment fragmentAnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get info from API and fill db
        updateParades();

        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, homeFragment).commit();
    }

    private void updateParades() {
        FetchParadesTask paradesTask = new FetchParadesTask(getApplicationContext());
        paradesTask.execute();
    }

    public void buttonClick(View v) {
        switch (v.getId()) {
            case R.id.show_lines_button:
                LiniesFragment liniesFragment = new LiniesFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, liniesFragment);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.show_rate_button:
                RatesFragment rateFragment = new RatesFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, rateFragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
    }

    public void transbord(ArrayList<String> paradesData, String nomParada, String btnText) {

        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();

        ParadaFragment paradaFragment = new ParadaFragment();
        Bundle paradaBundle = new Bundle();
        paradaBundle.putStringArrayList("parades_data", paradesData);
        paradaBundle.putString("parada_nom", nomParada);
        paradaBundle.putString("linia_nom", btnText);
        paradaFragment.setArguments(paradaBundle);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, paradaFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}

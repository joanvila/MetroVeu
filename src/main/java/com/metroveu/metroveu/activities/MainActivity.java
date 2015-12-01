package com.metroveu.metroveu.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.metroveu.metroveu.R;
import com.metroveu.metroveu.data.MetroContract;
import com.metroveu.metroveu.fragments.ConfigFragment;
import com.metroveu.metroveu.fragments.HomeFragment;
import com.metroveu.metroveu.fragments.LiniesFragment;
import com.metroveu.metroveu.fragments.ParadaFragment;
import com.metroveu.metroveu.fragments.RatesFragment;
import com.metroveu.metroveu.fragments.RoutesFragment;
import com.metroveu.metroveu.tasks.FetchParadesTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Cursor tema = this.getContentResolver().query(
                MetroContract.TemaEntry.CONTENT_URI,
                null,
                MetroContract.TemaEntry.COLUMN_TEMA_ACTUAL,
                null,
                null);
        String idTema = "3";
        if (tema != null && tema.moveToFirst()){
            do {
                idTema = tema.getString(tema.getColumnIndex("tema_temaactual"));
            } while(tema.moveToNext());
            tema.close();
        }

        if (idTema.equals("1"))
            setTheme(R.style.AppTheme2);
        else if (idTema.equals("2"))
            setTheme(R.style.AppTheme3);
        else
            setTheme(R.style.AppTheme1);

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

            case R.id.show_routes_button:
                RoutesFragment routesFragment = new RoutesFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, routesFragment);
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

            case R.id.configuration_button:
                ConfigFragment configFragment = new ConfigFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, configFragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
    }

    public void changeConfig(View v) {
        // Delete current set theme
        int mRowsDeleted = getContentResolver().delete(
                MetroContract.TemaEntry.CONTENT_URI,
                null,
                null
        );

        switch (v.getId()) {
            case R.id.config1_button:
                ContentValues temaValues = new ContentValues();
                temaValues.put(MetroContract.TemaEntry.COLUMN_TEMA_ACTUAL, 0);

                Uri insertedUri = this.getContentResolver().insert(
                        MetroContract.TemaEntry.CONTENT_URI,
                        temaValues
                );
                break;

            case R.id.config2_button:
                ContentValues temaValues2 = new ContentValues();
                temaValues2.put(MetroContract.TemaEntry.COLUMN_TEMA_ACTUAL, 1);

                Uri insertedUri2 = this.getContentResolver().insert(
                        MetroContract.TemaEntry.CONTENT_URI,
                        temaValues2
                );
                break;

            case R.id.config3_button:
                ContentValues temaValues3 = new ContentValues();
                temaValues3.put(MetroContract.TemaEntry.COLUMN_TEMA_ACTUAL, 2);

                Uri insertedUri3 = this.getContentResolver().insert(
                        MetroContract.TemaEntry.CONTENT_URI,
                        temaValues3
                );
                break;
        }
        themeSwitcher();
    }

    private void themeSwitcher() {
        Intent currentActivity = getIntent();
        finish();
        startActivity(currentActivity);
    }

    public void transbord(ArrayList<String> paradesData, String nomParada, String btnText, String colorLinia,
        boolean rutaStarted, ArrayList<String> ruta) {

        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();

        ParadaFragment paradaFragment = new ParadaFragment();
        Bundle paradaBundle = new Bundle();
        paradaBundle.putStringArrayList("parades_data", paradesData);
        paradaBundle.putString("parada_nom", nomParada);
        paradaBundle.putString("linia_nom", btnText);
        paradaBundle.putString("linia_color", colorLinia);
        paradaBundle.putBoolean("rutaStarted", rutaStarted);
        paradaBundle.putStringArrayList("ruta", ruta);
        paradaFragment.setArguments(paradaBundle);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, paradaFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}

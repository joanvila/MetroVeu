package com.metroveu.metroveu.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metroveu.metroveu.R;
import com.metroveu.metroveu.data.MetroDbHelper;

import java.util.ArrayList;

/**
 * Created by Florencia Tarditti on 12/10/15.
 */
public class ParadaFragment extends Fragment implements View.OnClickListener {

    private FragmentTransaction ft;
    float x1,x2;
    float y1, y2;
    String nomParada = "";
    ArrayList<String> paradesList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Creates view and links it to the parada fragment layout in res/layout
        final View rootView = inflater.inflate(R.layout.parada_fragment, container, false);
        rootView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);

        Bundle paradesBundle = getArguments();
        paradesList = paradesBundle.getStringArrayList("parades_data");
        String parada = paradesBundle.getString("parada_nom");
        final String linia = paradesBundle.getString("linia_nom");

        Cursor parades = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from parada where parada_nom =?", new String[]{parada});

        if (parades != null && parades.moveToFirst()){
            nomParada = parades.getString(parades.getColumnIndex("parada_nom"));
            parades.close();
        }

        final TextView nomParadaView = (TextView) rootView.findViewById(R.id.nomParada);
        nomParadaView.setText(nomParada);
        nomParadaView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);

        final TextView finalLinia = (TextView) rootView.findViewById(R.id.finalLinia);
        if (paradesList.get(0).equals(parada)) finalLinia.setText("Final de línia");
        else if (paradesList.get(paradesList.size()-1).equals(parada)) finalLinia.setText("Final de línia");
        else finalLinia.setText("");

        //Set station information
        setAccessibilitataDeParada(rootView, parada);
        setConnexions(rootView, linia, parada);
        setBGColor(rootView, linia);

        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 100;
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i("FLOR", "Right to Left");
                                // Right to Left
                                int index = paradesList.lastIndexOf(nomParadaView.getText())+1;
                                if(paradesList.get(index) != null)
                                    nomParada = paradesList.get(index);
                                    nomParadaView.setText(paradesList.get(index));
                                    setAccessibilitataDeParada(rootView, paradesList.get(index));
                                    setConnexions(rootView, linia, paradesList.get(index));
                                if (paradesList.get(0).equals(paradesList.get(index))) finalLinia.setText("Final de línia");
                                else if (paradesList.get(paradesList.size()-1).equals(paradesList.get(index))) finalLinia.setText("Final de línia");
                                else finalLinia.setText("");
                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i("FLOR", "Left to Right");
                                // Left to Right
                                int index = paradesList.lastIndexOf(nomParadaView.getText())-1;
                                if(index >= 0) {
                                    nomParada = paradesList.get(index);
                                    nomParadaView.setText(paradesList.get(index));
                                    setAccessibilitataDeParada(rootView, paradesList.get(index));
                                    setConnexions(rootView, linia, paradesList.get(index));
                                    if (paradesList.get(0).equals(paradesList.get(index))) finalLinia.setText("Final de línia");
                                    else if (paradesList.get(paradesList.size()-1).equals(paradesList.get(index))) finalLinia.setText("Final de línia");
                                    else finalLinia.setText("");
                                }
                            }
                            nomParadaView.sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });


        return rootView;
    }

    private void setBGColor(View rootView, String nomLinia) {
        Cursor linia = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from linia where linia_nom =?", new String[]{nomLinia});

        String color = "#000000";
        if (linia != null && linia.moveToFirst()){
            color = linia.getString(linia.getColumnIndex("linia_color"));
            linia.close();
        }

        rootView.setBackgroundColor(Color.parseColor(color));
    }

    private void setConnexions(View rootView, String linia, String nomParada) {
        Cursor pertany = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from pertany where pertany_parada =?", new String[]{nomParada});

        ArrayList<String> connexions = new ArrayList<>();
        if (pertany != null && pertany.moveToFirst()) {
            do {
                String connexioAux = pertany.getString(pertany.getColumnIndex("pertany_linia"));
                if (!connexioAux.equals(linia) && !connexions.contains(connexioAux))
                    connexions.add(connexioAux);
            } while (pertany.moveToNext());
        }

        LinearLayout connexionsLayout = (LinearLayout) rootView.findViewById(R.id.connexionsLayout);
        ((LinearLayout) connexionsLayout).removeAllViews();

        for (int i = 0; i < connexions.size(); ++i) {
            Button btnTag = new Button(getActivity().getApplicationContext());
            setBtnDrawable(btnTag, connexions.get(i));
            btnTag.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            btnTag.setText(connexions.get(i));
            btnTag.setId(i);
            btnTag.setOnClickListener(this);
            connexionsLayout.addView(btnTag);

        }
    }

    private void setBtnDrawable(Button btnTag, String connexio) {
        switch (connexio) {
            case "L1":
                btnTag.setBackgroundResource(R.drawable.buttonl1);
                break;
            case "L2":
                btnTag.setBackgroundResource(R.drawable.buttonl2);
                break;
            case "L3":
                btnTag.setBackgroundResource(R.drawable.buttonl3);
                break;
            case "L4":
                btnTag.setBackgroundResource(R.drawable.buttonl4);
                break;
            case "L5":
                btnTag.setBackgroundResource(R.drawable.buttonl5);
                break;
            case "L9":
                btnTag.setBackgroundResource(R.drawable.buttonl9);
                break;
            case "L10":
                btnTag.setBackgroundResource(R.drawable.buttonl10);
                break;
            case "L11":
                btnTag.setBackgroundResource(R.drawable.buttonl11);
                break;
            case "TRAMVIA BLAU":
                btnTag.setBackgroundResource(R.drawable.buttontb);
                break;
            case "FUNICULAR DE MONTJUÏC":
                btnTag.setBackgroundResource(R.drawable.buttonfm);
                break;
            case "TELEFÈRIC DE MONTJUÏC":
                btnTag.setBackgroundResource(R.drawable.buttontm);
                break;
        }
    }

    //TODO: Potser podem carregar totes les accessibilitats al principi a una estructura de dades
    private void setAccessibilitataDeParada(View rootView, String nomParada) {
        Cursor parades = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from parada where parada_nom =?", new String[]{nomParada});

        String accessibilitat = "";
        if (parades != null && parades.moveToFirst()){
            accessibilitat = parades.getString(parades.getColumnIndex("parada_accessibilitat"));
            parades.close();
        }

        final TextView accessibilitatView = (TextView) rootView.findViewById(R.id.accessibilitat);
        if (accessibilitat.equals("Adaptada"))
            accessibilitatView.setText(R.string.adaptada);
        else
            accessibilitatView.setText(R.string.noAdaptada);
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String btnText = button.getText().toString();

        Cursor parades = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from pertany where pertany_linia = ? order by pertany_ordre", new String[]{btnText});
        ArrayList<String> paradesData = new ArrayList<>();
        if (parades != null && parades.moveToFirst()){
            do {
                paradesData.add(parades.getString(parades.getColumnIndex("pertany_parada")));
            } while(parades.moveToNext());
            parades.close();
        }

        ParadaFragment paradaFragment = new ParadaFragment();
        Bundle paradaBundle = new Bundle();
        paradaBundle.putStringArrayList("parades_data", paradesData);
        paradaBundle.putString("parada_nom", nomParada);
        paradaBundle.putString("linia_nom", btnText);
        paradaFragment.setArguments(paradaBundle);
        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, paradaFragment);
        //ft.addToBackStack(null);
        ft.commit();
    }

}

package com.metroveu.metroveu.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metroveu.metroveu.R;
import com.metroveu.metroveu.activities.MainActivity;
import com.metroveu.metroveu.data.MetroDbHelper;

import java.util.ArrayList;

/**
 * Created by Florencia Tarditti on 12/10/15.
 */
public class ParadaFragment extends Fragment implements View.OnClickListener {

    private String nomParada = "";
    private String nomLinia = "";
    private ArrayList<String> paradesList;
    private String accessibilitat;
    private String colorLinia;
    private TextView nomParadaView = null;
    private TextView finalLinia = null;
    private Boolean rutaStarted = false;
    private ArrayList<String> ruta = new ArrayList<>();
    private LinearLayout finRutaLayout = null;
    private TextView rutaText = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.parada_fragment, container, false);
        rootView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);

        Bundle paradesBundle = getArguments();
        paradesList = paradesBundle.getStringArrayList("parades_data");
        nomParada = paradesBundle.getString("parada_nom");
        nomLinia = paradesBundle.getString("linia_nom");
        colorLinia = paradesBundle.getString("linia_color");

        getParadaInfo(nomParada);
        setParadaInfoInView(rootView);
        setConnexions(rootView, nomLinia, nomParada);

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
                            else if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                // Right to Left
                                int index = paradesList.lastIndexOf(nomParadaView.getText())+1;
                                if(paradesList.get(index) != null)
                                    nomParada = paradesList.get(index);
                                    nomParadaView.setText(paradesList.get(index));
                                    setAccessibilitatDeParada(rootView, paradesList.get(index));
                                    setConnexions(rootView, nomLinia, paradesList.get(index));
                                if (paradesList.get(0).equals(paradesList.get(index)) ||
                                        paradesList.get(paradesList.size()-1).equals(paradesList.get(index)))
                                    finalLinia.setText(R.string.final_linia);
                                else finalLinia.setText("");
                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                // Left to Right
                                int index = paradesList.lastIndexOf(nomParadaView.getText())-1;
                                if(index >= 0) {
                                    nomParada = paradesList.get(index);
                                    nomParadaView.setText(paradesList.get(index));
                                    setAccessibilitatDeParada(rootView, paradesList.get(index));
                                    setConnexions(rootView, nomLinia, paradesList.get(index));
                                    if (paradesList.get(0).equals(paradesList.get(index)) ||
                                            paradesList.get(paradesList.size()-1).equals(paradesList.get(index)))
                                        finalLinia.setText(R.string.final_linia);
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

        final CardView rutaCard = (CardView) rootView.findViewById(R.id.rutaCard);
        finRutaLayout = (LinearLayout) rootView.findViewById(R.id.finRutaLayout);
        rutaText = (TextView) rootView.findViewById(R.id.rutaText);
        rutaCard.setOnClickListener(afegirARutaOnClick);

        return rootView;
    }

    private void getParadaInfo(String nomParada) {
        Cursor parades = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from parada where parada_nom =?", new String[]{nomParada});

        if (parades != null && parades.moveToFirst()){
            accessibilitat = parades.getString(parades.getColumnIndex("parada_accessibilitat"));
            if (!accessibilitat.equals(getResources().getString(R.string.adaptada)))
                accessibilitat = getResources().getString(R.string.noAdaptada);
            parades.close();
        }
    }

    private void setParadaInfoInView(View rootView) {
        nomParadaView = (TextView) rootView.findViewById(R.id.nomParada);
        nomParadaView.setText(nomParada);
        nomParadaView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);

        finalLinia = (TextView) rootView.findViewById(R.id.finalLinia);
        if (paradesList.get(0).equals(nomParada)
                || paradesList.get(paradesList.size()-1).equals(nomParada))
            finalLinia.setText(R.string.final_linia);
        else finalLinia.setText("");

        final TextView accessibilitatView = (TextView) rootView.findViewById(R.id.accessibilitat);
        accessibilitatView.setText(accessibilitat);

        rootView.setBackgroundColor(Color.parseColor(colorLinia));
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
            pertany.close();
        }

        LinearLayout connexionsLayout = (LinearLayout) rootView.findViewById(R.id.connexionsLayout);
        ((LinearLayout) connexionsLayout).removeAllViews();

        for (int i = 0; i < connexions.size(); ++i) {
            Cursor connexioLinia = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                    rawQuery("select linia_color from linia where linia_nom =?", new String[]{connexions.get(i)});
            String colorLinia = "#aaa";
            if (connexioLinia != null && connexioLinia.moveToFirst()) {
                colorLinia = connexioLinia.getString(connexioLinia.getColumnIndex("linia_color"));
                connexioLinia.close();
            }

            CardView cardView = new CardView(getActivity().getApplicationContext());
            cardView.setLayoutParams(new ViewGroup.LayoutParams(300, 70));
            cardView.setCardBackgroundColor(Color.parseColor(colorLinia));
            cardView.setOnClickListener(this);
            TextView connectionName = new TextView(getActivity().getApplicationContext());
            connectionName.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            connectionName.setText(connexions.get(i));
            connectionName.setId(getResources().getIdentifier(connexions.get(i).trim(), "values",
                    getActivity().getApplicationContext().getPackageName()));
            connectionName.setTextSize(20);
            connectionName.setTextColor(getResources().getColor(R.color.colorWhite));
            connectionName.setTypeface(null, Typeface.BOLD);
            connectionName.setGravity(Gravity.CENTER);
            cardView.addView(connectionName);
            connexionsLayout.addView(cardView);
        }

    }

    //TODO: Potser podem carregar totes les accessibilitats al principi a una estructura de dades
    private void setAccessibilitatDeParada(View rootView, String nomParada) {
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

    public View.OnClickListener afegirARutaOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!rutaStarted) {
                rutaStarted = true;
                rutaText.setText(R.string.afegir_a_ruta);

                CardView cardView = new CardView(getActivity().getApplicationContext());
                CardView.LayoutParams cardViewLayout = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                cardViewLayout.setMargins(20, 20, 20, 20);
                cardView.setRadius(14);
                cardView.setLayoutParams(cardViewLayout);
                cardView.setCardBackgroundColor(getResources().getColor(R.color.colorBGray));
                cardView.setOnClickListener(acabarRutaOnClick);
                TextView finishRuta = new TextView(getActivity().getApplicationContext());
                finishRuta.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                finishRuta.setText(getResources().getString(R.string.final_ruta));
                finishRuta.setTextSize(20);
                finishRuta.setTextColor(getResources().getColor(R.color.colorWhite));
                finishRuta.setTypeface(null, Typeface.BOLD);
                finishRuta.setGravity(Gravity.CENTER);
                cardView.addView(finishRuta);
                finRutaLayout.addView(cardView);
            }
            if (!ruta.contains(nomParada)) {
                ruta.add(nomParada);
            }
        }
    };

    public View.OnClickListener acabarRutaOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ((LinearLayout) finRutaLayout).removeAllViews();
            //TODO: Save ruta to database
            //TODO: Save line name for each station
            Log.v("Ruta ", String.valueOf(ruta));
            rutaStarted = false;
            ruta.clear();
            rutaText.setText(R.string.comencar_ruta);
        }
    };

    @Override
    public void onClick(View v) {
        TextView textView = (TextView) v.findViewById(0);
        String text = (String) textView.getText();

        Cursor parades = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from pertany where pertany_linia = ? order by pertany_ordre", new String[]{text});
        ArrayList<String> paradesData = new ArrayList<>();
        String novaLinia = "";
        if (parades != null && parades.moveToFirst()){
            do {
                paradesData.add(parades.getString(parades.getColumnIndex("pertany_parada")));
                novaLinia = parades.getString(parades.getColumnIndex("pertany_linia"));
            } while(parades.moveToNext());
            parades.close();
        }

        Cursor linia = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from linia where linia_nom =?", new String[]{novaLinia});
        String color = "";
        if (linia != null && linia.moveToFirst()){
            color = linia.getString(linia.getColumnIndex("linia_color"));
            linia.close();
        }

        ((MainActivity)getActivity()).transbord(paradesData, nomParada, text, color);
    }
}
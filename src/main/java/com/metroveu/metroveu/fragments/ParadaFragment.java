package com.metroveu.metroveu.fragments;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.metroveu.metroveu.R;
import com.metroveu.metroveu.activities.MainActivity;
import com.metroveu.metroveu.data.MetroContract;
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

    private View rootView;
    private int previousStopIndex;
    private int actualStopIndex;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.parada_fragment, container, false);
        rootView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);

        Bundle paradesBundle = getArguments();
        paradesList = paradesBundle.getStringArrayList("parades_data");
        nomParada = paradesBundle.getString("parada_nom");
        nomLinia = paradesBundle.getString("linia_nom");
        colorLinia = paradesBundle.getString("linia_color");
        rutaStarted = paradesBundle.getBoolean("rutaStarted");
        ruta = paradesBundle.getStringArrayList("ruta");

        rutaText = (TextView) rootView.findViewById(R.id.rutaText);
        finRutaLayout = (LinearLayout) rootView.findViewById(R.id.finRutaLayout);
        if (rutaStarted) checkRutaButtonIsDisplayed();

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
                                previousStopIndex = paradesList.lastIndexOf(nomParadaView.getText());
                                int index = paradesList.lastIndexOf(nomParadaView.getText())+1;
                                actualStopIndex = index;
                                if(paradesList.get(index) != null)
                                    nomParada = paradesList.get(index);
                                    nomParadaView.setText(paradesList.get(index));
                                    setAccessibilitatDeParada(rootView, paradesList.get(index));
                                    setConnexions(rootView, nomLinia, paradesList.get(index));
                                if (paradesList.get(0).equals(paradesList.get(index)) ||
                                        paradesList.get(paradesList.size()-1).equals(paradesList.get(index)))
                                    finalLinia.setText(R.string.final_linia);
                                else finalLinia.setText("");
                                String proxParada = nomLinia + "-" + nomParada;
                                if (rutaStarted && !ruta.contains(proxParada)) ruta.add(proxParada);
                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                // Left to Right
                                previousStopIndex = paradesList.lastIndexOf(nomParadaView.getText());
                                int index = paradesList.lastIndexOf(nomParadaView.getText())-1;
                                actualStopIndex = index;
                                if(index >= 0) {
                                    nomParada = paradesList.get(index);
                                    nomParadaView.setText(paradesList.get(index));
                                    setAccessibilitatDeParada(rootView, paradesList.get(index));
                                    setConnexions(rootView, nomLinia, paradesList.get(index));
                                    if (paradesList.get(0).equals(paradesList.get(index)) ||
                                            paradesList.get(paradesList.size()-1).equals(paradesList.get(index)))
                                        finalLinia.setText(R.string.final_linia);
                                    else finalLinia.setText("");
                                    String proxParada = nomLinia + "-" + nomParada;
                                    if (rutaStarted && !ruta.contains(proxParada)) ruta.add(proxParada);
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
        rutaCard.setOnClickListener(afegirARutaOnClick);

        return rootView;
    }

    private void checkRutaButtonIsDisplayed() {
        rutaText.setText(R.string.eliminar_ultima_parada_afegida);

        CardView cardView = new CardView(getActivity().getApplicationContext());
        CardView.LayoutParams cardViewLayout = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardView.setRadius(14);
        cardView.setLayoutParams(cardViewLayout);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.colorBGray));
        cardView.setOnClickListener(acabarRutaOnClick);
        TextView finishRuta = new TextView(getActivity().getApplicationContext());
        finishRuta.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        finishRuta.setText(getResources().getString(R.string.final_ruta));
        finishRuta.setTextSize(20);
        finishRuta.setTextColor(getResources().getColor(R.color.colorWhite));
        finishRuta.setGravity(Gravity.CENTER);
        cardView.addView(finishRuta);
        finRutaLayout.addView(cardView);
    }

    private void getParadaInfo(String nomParada) {
        Cursor parades = new MetroDbHelper(getActivity().getApplicationContext()).getReadableDatabase().
                rawQuery("select * from parada where parada_nom =?", new String[]{nomParada});

        if (parades != null && parades.moveToFirst()){
            this.accessibilitat = parades.getString(parades.getColumnIndex("parada_accessibilitat"));
            if (!this.accessibilitat.equals(getResources().getString(R.string.adaptada)))
                this.accessibilitat = getResources().getString(R.string.noAdaptada);
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
        accessibilitatView.setText(this.accessibilitat);

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
            cardView.setLayoutParams(new ViewGroup.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT));
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
            connectionName.setEllipsize(TextUtils.TruncateAt.MIDDLE);
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
            this.accessibilitat = parades.getString(parades.getColumnIndex("parada_accessibilitat"));
            parades.close();
        }

        final TextView accessibilitatView = (TextView) rootView.findViewById(R.id.accessibilitat);
        if (this.accessibilitat.equals("Adaptada"))
            accessibilitatView.setText(R.string.adaptada);
        else
            accessibilitatView.setText(R.string.noAdaptada);
    }

    public View.OnClickListener afegirARutaOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
        if (!rutaStarted) {
            final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.show();

            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialog.setContentView(R.layout.pop_up_start_ruta);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            TextView add = (TextView) dialog.findViewById(R.id.add);
            TextView cancel = (TextView) dialog.findViewById(R.id.cancel);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rutaStarted = true;
                    ruta.add(nomLinia + "-" + nomParada);
                    rutaText.setText(R.string.eliminar_ultima_parada_afegida);

                    CardView cardView = new CardView(getActivity().getApplicationContext());
                    CardView.LayoutParams cardViewLayout = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    cardView.setRadius(14);
                    cardView.setLayoutParams(cardViewLayout);
                    cardView.setCardBackgroundColor(getResources().getColor(R.color.colorBGray));
                    cardView.setOnClickListener(acabarRutaOnClick);
                    TextView finishRuta = new TextView(getActivity().getApplicationContext());
                    finishRuta.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    finishRuta.setText(getResources().getString(R.string.final_ruta));
                    finishRuta.setTextSize(20);
                    finishRuta.setTextColor(getResources().getColor(R.color.colorWhite));
                    finishRuta.setGravity(Gravity.CENTER);
                    cardView.addView(finishRuta);
                    finRutaLayout.addView(cardView);
                    dialog.dismiss();
                }

            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }

            });

        } else if (ruta != null && ruta.size() > 0) {
            final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.show();

            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialog.setContentView(R.layout.pop_up_delete_last_stop);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            TextView add = (TextView) dialog.findViewById(R.id.add);
            TextView cancel = (TextView) dialog.findViewById(R.id.cancel);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                ruta.remove(ruta.size() - 1);
                if (ruta.size() > 0) {
                    goToPreviousStop();
                } else {
                    ((LinearLayout) finRutaLayout).removeAllViews();
                    rutaStarted = false;
                    ruta.clear();
                    rutaText.setText(R.string.comencar_ruta);
                }
                dialog.dismiss();
                }

            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }

            });

        }
        }
    };

    private void goToPreviousStop() {
        if(previousStopIndex >= 0) {
            nomParada = paradesList.get(previousStopIndex);
            nomParadaView.setText(paradesList.get(previousStopIndex));
            setAccessibilitatDeParada(rootView, paradesList.get(previousStopIndex));
            setConnexions(rootView, nomLinia, paradesList.get(previousStopIndex));
            if (paradesList.get(0).equals(paradesList.get(previousStopIndex)) ||
                    paradesList.get(paradesList.size()-1).equals(paradesList.get(previousStopIndex)))
                finalLinia.setText(R.string.final_linia);
            else finalLinia.setText("");
            String proxParada = nomLinia + "-" + nomParada;
            if (rutaStarted && !ruta.contains(proxParada)) ruta.add(proxParada);

            //Actualitzar last index
            if (previousStopIndex < actualStopIndex) {
                --previousStopIndex;
                --actualStopIndex;
            } else {
                ++previousStopIndex;
                ++actualStopIndex;
            }
        }
    }

    long addRuta(ArrayList<String> ruta) {

        long rutaId = 0;
        String nomRuta = "";

        if (ruta.get(0) != null && ruta.get(ruta.size()-1) != null)
            nomRuta = ruta.get(0) + " / " + ruta.get(ruta.size()-1);

        Cursor rutaCursor = getActivity().getApplicationContext().getContentResolver().query(
                MetroContract.RutaEntry.CONTENT_URI,
                null,
                MetroContract.RutaEntry.COLUMN_RUTA_NOM + " = ?",
                new String[]{nomRuta},
                null);

        if (rutaCursor != null && rutaCursor.moveToFirst()) {
            Toast rutaToast = Toast.makeText(getActivity().getApplicationContext(),
                    R.string.ruta_exists, Toast.LENGTH_LONG);
            rutaToast.show();
        } else {

            ContentValues rutaValues = new ContentValues();
            rutaValues.put(MetroContract.RutaEntry.COLUMN_RUTA_NOM, nomRuta);
            rutaValues.put(MetroContract.RutaEntry.COLUMN_RUTA_LLOCSINTERES, "");
            rutaValues.put(MetroContract.RutaEntry.COLUMN_RUTA_PARADES, String.valueOf(ruta));
            rutaValues.put(MetroContract.RutaEntry.COLUMN_RUTA_MAPA, "Barcelona");

            Uri insertedUri = getActivity().getApplicationContext().getContentResolver().insert(
                    MetroContract.RutaEntry.CONTENT_URI,
                    rutaValues
            );

            rutaId = ContentUris.parseId(insertedUri);
        }
        if (rutaCursor != null) rutaCursor.close();
        return rutaId;
    }

    public View.OnClickListener acabarRutaOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ((LinearLayout) finRutaLayout).removeAllViews();
            addRuta(ruta);
            rutaStarted = false;
            Log.v("ruta", String.valueOf(ruta));
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

        String proxParada = text + "-" + nomParada;
        if (rutaStarted && !ruta.contains(proxParada)) ruta.add(proxParada);

        ((MainActivity)getActivity()).transbord(paradesData, nomParada, text, color, rutaStarted, ruta);
    }
}
package com.metroveu.metroveu.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metroveu.metroveu.R;
import com.metroveu.metroveu.data.MetroContract;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Florencia Tarditti on 11/12/15.
 */
public class RoutesAdapter extends BaseAdapter {

    private Context context;
    ArrayList<String> info;

    public RoutesAdapter(Context context, ArrayList<String> info) {
        this.context = context;
        this.info = info;
    }


    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public Object getItem(int position) {
        return info.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    TextView mainName;
    LinearLayout deleteButton;

    public String getDbRutaName(String screenRutaName) {
        String systemLanguage = Locale.getDefault().getDisplayLanguage();
        String dbRutaName = "";
        if (systemLanguage.equals("català") || systemLanguage.equals("español")) {
            dbRutaName = screenRutaName.replace("De ","");
            dbRutaName = dbRutaName.replace("a","/");
        } else {
            dbRutaName = screenRutaName.replace("From ","");
            dbRutaName = dbRutaName.replace("to","/");
        }
        return dbRutaName;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.routes_list_view, null);
        }

        mainName = (TextView) convertView.findViewById(R.id.mainName);
        deleteButton = (LinearLayout) convertView.findViewById(R.id.deleteRouteClickable);

        String nomRuta = info.get(position);
        mainName.setText(nomRuta);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog dialog = new AlertDialog.Builder(parent.getContext()).create();
                dialog.show();

                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.setContentView(R.layout.pop_up_delete_ruta);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                TextView add = (TextView) dialog.findViewById(R.id.add);
                TextView cancel = (TextView) dialog.findViewById(R.id.cancel);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nomRuta = info.get(position);
                        info.remove(nomRuta);

                        String nomRutaDb = getDbRutaName(nomRuta);
                        int deletedRuta = context.getContentResolver().delete(
                                MetroContract.RutaEntry.CONTENT_URI,
                                MetroContract.RutaEntry.COLUMN_RUTA_NOM + " = ?",
                                new String[]{nomRutaDb});

                        notifyDataSetChanged();
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
        });


        return convertView;
    }
}

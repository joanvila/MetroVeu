package com.metroveu.metroveu.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metroveu.metroveu.R;

import java.util.ArrayList;

/**
 * Created by Carla on 12/11/2015.
 */
public class RatesAdapter extends BaseAdapter  {

    private Context context;
    ArrayList<String> tarifes;

    public RatesAdapter(Context context, ArrayList<String> tarifes) {
        this.context = context;
        this.tarifes = tarifes;

    }


    public int getCount() {
        return tarifes.size();
    }

    public Object getItem(int position) {
        return tarifes.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    TextView tarifa;

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.linies_list_view, null);
        }

        tarifa = (TextView) convertView.findViewById(R.id.lineName);

        String tipus_tarifa = tarifes.get(position);
        tarifa.setText(tipus_tarifa);

        return convertView;
    }
}

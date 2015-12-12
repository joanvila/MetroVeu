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
 * Created by Florencia Tarditti on 27/10/15.
 */
public class LiniesAdapter extends BaseAdapter {

    private Context context;
    ArrayList<String> linies;
    ArrayList<String> colors;

    public LiniesAdapter(Context context, ArrayList<String> linies, ArrayList<String> colors) {
        this.context = context;
        this.linies = linies;
        this.colors = colors;
    }


    @Override
    public int getCount() {
        return linies.size();
    }

    @Override
    public Object getItem(int position) {
        return linies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    TextView linia;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.linies_list_view, null);
        }

        linia = (TextView) convertView.findViewById(R.id.lineName);

        String nomLinia = linies.get(position);
        linia.setText(nomLinia);
        linia.setBackgroundColor(Color.parseColor(colors.get(position)));
        String color = "";
        switch (nomLinia) {
            case "L1":
                color = context.getResources().getString(R.string.L1);
                break;
            case "L2":
                color = context.getResources().getString(R.string.L2);
                break;
            case "L3":
                color = context.getResources().getString(R.string.L3);
                break;
            case "L4":
                color = context.getResources().getString(R.string.L4);
                break;
            case "L5":
                color = context.getResources().getString(R.string.L5);
                break;
            case "L9":
                color = context.getResources().getString(R.string.L9);
                break;
            case "L10":
                color = context.getResources().getString(R.string.L10);
                break;
            case "L11":
                color = context.getResources().getString(R.string.L11);
                break;
        }
        linia.setContentDescription(nomLinia+' '+color);

        return convertView;
    }
}

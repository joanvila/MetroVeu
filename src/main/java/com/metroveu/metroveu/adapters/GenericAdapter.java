package com.metroveu.metroveu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metroveu.metroveu.R;

import java.util.ArrayList;

/**
 * Created by Florencia Tarditti on 24/11/15.
 */
public class GenericAdapter extends BaseAdapter {

    private Context context;
    ArrayList<String> info;

    public GenericAdapter(Context context, ArrayList<String> info) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.generic_list_view, null);
        }

        mainName = (TextView) convertView.findViewById(R.id.mainName);

        String nomLinia = info.get(position);
        mainName.setText(nomLinia);

        return convertView;
    }
}

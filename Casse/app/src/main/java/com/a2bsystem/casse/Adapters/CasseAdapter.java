package com.a2bsystem.casse.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.a2bsystem.casse.Models.Casse;
import com.a2bsystem.casse.R;

public class CasseAdapter extends ArrayAdapter<Casse> {

    private ViewHolder viewHolder;

    private static class ViewHolder {
        private LinearLayout itemView;
    }

    public CasseAdapter(Context context, int textViewResourceId, ArrayList<Casse> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.liste_casses_lines, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = (LinearLayout) convertView.findViewById(R.id.mainLine);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Casse item = getItem(position);
        if (item != null) {
            TextView store = viewHolder.itemView.findViewById(R.id.main_casse);
            View type = viewHolder.itemView.findViewById(R.id.main_color);
            String name = "[" + item.getLagstalle() + "] " + item.getFtgnamn();
            store.setText(name);

            if (item.getStatus().equalsIgnoreCase("CREATED")) {
                type.setBackgroundColor(Color.RED);
            } else if (item.getStatus().equalsIgnoreCase("INPROGRESS")) {
                type.setBackgroundColor(Color.YELLOW);
            } else if (item.getStatus().equalsIgnoreCase("TRANSFERED")) {
                type.setBackgroundColor(Color.GREEN);
            } else {
                type.setBackgroundColor(Color.GRAY);
            }
        }

        return convertView;
    }
}
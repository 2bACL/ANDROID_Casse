package com.a2bsystem.casse.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.a2bsystem.casse.Models.Client;
import com.a2bsystem.casse.R;

public class ListViewAdapter extends BaseAdapter {
    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<Client> clientsList = null;
    private ArrayList<Client> arraylist;

    public ListViewAdapter(Context context, List<Client> clientsList) {
        mContext = context;
        this.clientsList = clientsList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(clientsList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return clientsList.size();
    }

    @Override
    public Client getItem(int position) {
        return clientsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_items, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText("[" + clientsList.get(position).getFtgnr().replaceAll(" ","") + "] " + clientsList.get(position).getFtgnamn());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        clientsList.clear();
        if (charText.length() == 0) {
            clientsList.addAll(arraylist);
        } else {
            for (Client wp : arraylist) {
                if (wp.getFtgnr().toLowerCase(Locale.getDefault()).contains(charText) || wp.getFtgnamn().toLowerCase(Locale.getDefault()).contains(charText)) {
                    clientsList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}

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

import com.a2bsystem.casse.Models.ArticleCasse;
import com.a2bsystem.casse.R;

public class ArticleCasseAdapter extends ArrayAdapter<ArticleCasse> {

    private ArticleCasseAdapter.ViewHolder viewHolder;

    private static class ViewHolder {
        private LinearLayout itemView;
    }

    public ArticleCasseAdapter(Context context, int textViewResourceId, ArrayList<ArticleCasse> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.casse_line, parent, false);

            viewHolder = new ArticleCasseAdapter.ViewHolder();
            viewHolder.itemView = convertView.findViewById(R.id.casseLine);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ArticleCasseAdapter.ViewHolder) convertView.getTag();
        }

        ArticleCasse item = getItem(position);
        if (item != null) {

            TextView article = viewHolder.itemView.findViewById(R.id.casse_article);
            article.setText(item.getArtnr());
            TextView date = viewHolder.itemView.findViewById(R.id.casse_livraison);
            date.setText(item.getDate());
            TextView paNet = viewHolder.itemView.findViewById(R.id.casse_pa_net);
            paNet.setText(item.getPanet());
            TextView quantite = viewHolder.itemView.findViewById(R.id.casse_qte);
            quantite.setText(item.getQte());
            TextView pvc = viewHolder.itemView.findViewById(R.id.casse_pvc);
            pvc.setText(item.getPvc());
            TextView comm = viewHolder.itemView.findViewById(R.id.casse_commentaire);
            comm.setText(item.getComm());

            if(item.getComm().equalsIgnoreCase("")){
                comm.setVisibility(View.GONE);
            }
            else {
                comm.setVisibility(View.VISIBLE);
            }

            View casseState = viewHolder.itemView.findViewById(R.id.casse_state);
            if (item.getTrans().equalsIgnoreCase("CREATED")){
                casseState.setBackgroundColor(Color.RED);
            }
            else if (item.getTrans().equalsIgnoreCase("TRANSFERED")){
                casseState.setBackgroundColor(Color.GREEN);
            }
            else {
                casseState.setBackgroundColor(Color.GRAY);
            }
        }
        return convertView;
    }
}

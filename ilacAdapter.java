package com.example.alzheimerhastatakip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ilacAdapter extends ArrayAdapter<IlacModel> {

    public ilacAdapter(Context context, List<IlacModel> list) {
      super(context,0,list);
    }


    @NonNull
    @Override
    public View getView(int position, View listItemView, @NonNull ViewGroup parent) {
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        IlacModel ilaclar = getItem(position);

        String name = ilaclar.getIlacAdi();
        ((TextView) listItemView).setText(name);

        return listItemView;
    }
}

package com.distanteam.exprezzocurrencydevice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int flags[];
    String[] countryNames;
    LayoutInflater inflter2;


    public CustomAdapter(Context applicationContext, int[] flags, String[] countryNames) {
        this.context = applicationContext;
        this.flags = flags;
        this.countryNames = countryNames;
        inflter2 = (LayoutInflater.from(applicationContext));
    }




    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter2.inflate(R.layout.spinner_item, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageview);
        TextView country = (TextView) view.findViewById(R.id.textView2);

        icon.setAlpha(200);
        icon.setImageResource(flags[i]);
        country.setText(countryNames[i]);

        return view;
    }
}
package com.distanteam.exprezzocurrencydevice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter2 extends BaseAdapter {
    Context context;
    int flags2[];
    String[] countryNames2;
    LayoutInflater inflter;

    public CustomAdapter2(Context applicationContext, int[] flags, String[] countryNames) {
        this.context = applicationContext;
        this.flags2 = flags;
        this.countryNames2 = countryNames;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return flags2.length;
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
        view = inflter.inflate(R.layout.spinner_item2, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageview2);
        TextView country2 = (TextView) view.findViewById(R.id.textView3);

      //  ColorMatrix matrix = new ColorMatrix();
       // matrix.setSaturation(0);

     //   ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
     //   icon.setColorFilter(filter);

        icon.setAlpha(200);
        icon.setImageResource(flags2[i]);
        country2.setText(countryNames2[i]);

        return view;
    }
}
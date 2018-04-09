package com.example.purva.sdcardapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by purva on 4/7/18.
 */

public class MyAdapter extends BaseAdapter {


    long avg;
    long total;
    ArrayList<File> fN;
    String[] Ext;
    long[] size;
    Context context;
    LayoutInflater layoutInflater;

    public MyAdapter(long total, ArrayList<File> fileNames, String[] extensions, long[] sizes,Context context) {
        this.total = total;
        this.fN = fileNames;
        this.Ext = extensions;
        this.size = sizes;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return fN.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        avg = total/fN.size()/1024;

        convertView = layoutInflater.inflate(R.layout.item_layout,null);
        TextView itemName = convertView.findViewById(R.id.textViewName);
        TextView itemSize = convertView.findViewById(R.id.textViewSize);
        TextView itemAvg = convertView.findViewById(R.id.textViewAvg);
        TextView itemExt = convertView.findViewById(R.id.textViewExt);


        itemName.setText("File Name: "+fN.get(position).getName());
        itemAvg.setText("Average file size in KB: "+avg);
        itemExt.setText("File Extension: "+Ext[position]);
        itemSize.setText("Size of the file in KB: "+size[position]);

        return convertView;
    }
}

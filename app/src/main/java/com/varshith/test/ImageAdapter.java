package com.varshith.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.varshith.test.R;


public class ImageAdapter extends BaseAdapter {
    Context c;
    String[] items;

    ImageAdapter(Context c, String arr[]) {
        this.c = c;
        items = arr;

    }

    @Override
    public int getCount() {
        return items.length;
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
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_layout, null);
        }
        Glide.with(c).clear(view);
        ImageView imageView = view.findViewById(R.id.imageView);

        Glide.with(c).load(items[i]).placeholder(R.drawable.load2).into(imageView);


        return view;
    }
}

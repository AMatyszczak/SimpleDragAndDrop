package com.example.adria.SimpleDragAndDrop.GridViewCustom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.adria.SimpleDragAndDrop.R;

import java.util.ArrayList;

public class AdapterCustom extends BaseAdapter {
    private Context context;
    private ArrayList<String> items;

    public AdapterCustom(ArrayList<String> items, Context context) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root = convertView;
        if(root == null)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            root = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView title = root.findViewById(R.id.text);
        title.setText(getItem(position));

        return root;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void swapItems(int one, int two)
    {
        String temp = items.get(one);
        items.set(one,items.get(two));
        items.set(two, temp);
    }
}
package com.EasyEstate.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.EasyEstate.R;

import java.util.ArrayList;

/**
 * Created by canturker on 08/05/15.
 */
public class TitleNavigationAdapter extends BaseAdapter {
    private ArrayList<String> textList;
    private TextView titleTextView;
    private Context context ;
    public TitleNavigationAdapter(Context context,ArrayList<String> list){
            this.textList = list;
            this.context = context;
    }

    @Override
    public int getCount() {
        return textList.size();
    }

    @Override
    public Object getItem(int position) {
        return textList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.textview_custom_actionbar,null);
        }
        titleTextView = (TextView)convertView.findViewById(R.id.TitleTextView);
        titleTextView.setText(textList.get(position));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.textview_custom_actionbar,null);
        }
        titleTextView = (TextView)convertView.findViewById(R.id.TitleTextView);
        titleTextView.setText(textList.get(position));
        return convertView;
    }
}

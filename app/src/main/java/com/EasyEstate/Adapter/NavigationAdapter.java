package com.EasyEstate.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.EasyEstate.R;

/**
 * Created by canturker on 05/04/15.
 */
public class NavigationAdapter extends BaseAdapter{
    private String [] menus ;
    private int [] icons;
    //Clickable fragments
    private Context context;
    public  NavigationAdapter(String [] menus,Context context,int [] icons){
        this.menus= menus;
        this.icons = icons;
        this.context = context;
    }
    @Override
    public int getCount() {
        return menus.length;
    }

    @Override
    public Object getItem(int position) {
        return menus[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder{
        TextView textView;
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int type = getItemViewType(position);
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.navigatormenu,null);
                 holder.textView =(TextView)convertView.findViewById(R.id.navigatorMenuTextView);
                holder.imageView =(ImageView)convertView.findViewById(R.id.navigatorMenuImageView);
                convertView.setTag(holder);

        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        // image Will be set...
        holder.imageView.setImageResource(icons[position]);
        holder.textView.setText(menus[position]);


        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }


}

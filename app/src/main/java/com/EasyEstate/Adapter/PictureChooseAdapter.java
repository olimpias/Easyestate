package com.EasyEstate.Adapter;


import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.EasyEstate.Activity.MyListingControlActivity;
import com.EasyEstate.Fragment.InsertImageFragment;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.BitmapTool;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by canturker on 13/03/15.
 */
public class PictureChooseAdapter extends BaseAdapter {
    List<String> bitmapList;

    FragmentActivity activity;
    InsertImageFragment fragment;
    public PictureChooseAdapter(FragmentActivity activity,InsertImageFragment fragment) {
        this.bitmapList=new ArrayList<>();
        this.activity = activity;
        this.fragment = fragment;
    }
    public void AddImage(Bitmap mp,String img_path){
        fragment.addBitmapToMemoryCache(img_path,mp);
        bitmapList.add(img_path);
    }
    @Override
    public int getCount() {
        return bitmapList.size();
    }

    @Override
    public Object getItem(int position) {
        return bitmapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void loadBitmap(String name, ImageView imageView) {
        final Bitmap bitmap = fragment.getBitmapFromMemCache(name);
        if (bitmap != null) {
            Bitmap bt = BitmapTool.newScaledThumbnailBitmap(bitmap, name);
            imageView.setImageBitmap(bt);
        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView= inflater.inflate(R.layout.insert_listing_image_block,null);
            holder=new ViewHolder();
            holder.imageView=(ImageView)convertView.findViewById(R.id.photoImageView);
            holder.deleteButton=(Button)convertView.findViewById(R.id.deleteButton);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();

        }
        loadBitmap(bitmapList.get(position),holder.imageView);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.remove(bitmapList.remove(position));
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    private class ViewHolder{
        Button deleteButton;
        ImageView imageView;
    }
}
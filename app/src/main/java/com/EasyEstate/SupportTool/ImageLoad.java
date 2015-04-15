package com.EasyEstate.SupportTool;

import android.widget.ImageView;

/**
 * Created by canturker on 15/04/15.
 */
public class ImageLoad {
    private ImageView imageView;
    private String url;
    public ImageLoad(ImageView imageView,String url){
        this.url = url;
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public String getUrl() {
        return url;
    }
}

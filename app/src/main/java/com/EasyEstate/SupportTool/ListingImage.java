package com.EasyEstate.SupportTool;

import android.graphics.Bitmap;
import com.EasyEstate.Model.Listing;

/**
 * Created by canturker on 09/05/15.
 */
public class ListingImage {
    private Bitmap bitmap;
    private Listing listing;
    public ListingImage(Listing listing){
        this.listing = listing;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Listing getListing() {
        return listing;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public String getUrl(){
        return Listing.IMAGE_URL+listing.getImagesURL().get(0);
    }
}

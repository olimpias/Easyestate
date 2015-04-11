package com.EasyEstate.SupportTool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by canturker on 04/04/15.
 */
public class BitmapTool {
    public static final int Image_WIDTH = 250;
    public static final int Image_HEIGHT = 175;
    private static  final int Thumbnail_WIDTH = 110;
    private static final int Thumbnail_HEIGHT = 120;
    public static String encodeTo64BitBitmap(Bitmap bitmap){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
        byte [] data = out.toByteArray();
        String image = Base64.encodeToString(data, 0);
        return image;
    }
    public static Bitmap newScaledThumbnailBitmap(Bitmap bitmap,String path){
        return Bitmap.createScaledBitmap(BitmapTool.imageOrientationValidator(bitmap, path),Thumbnail_WIDTH  , Thumbnail_HEIGHT, false);
    }
    public static Bitmap newScaledThumbnailBitmap(Bitmap bitmap){
        return Bitmap.createScaledBitmap(bitmap,Thumbnail_WIDTH  , Thumbnail_HEIGHT, false);
    }
    public static Bitmap newScaledThumbnailBitmap(InputStream in){
        Bitmap bitmap = BitmapFactory.decodeStream(in);
        return Bitmap.createScaledBitmap(bitmap,Thumbnail_WIDTH  , Thumbnail_HEIGHT, false);
    }
    public static Bitmap newScaledBitmap(Bitmap bitmap,String path){
        return Bitmap.createScaledBitmap(BitmapTool.imageOrientationValidator(bitmap, path),Image_WIDTH  , Image_HEIGHT, false);
    }
    public static Bitmap newScaledBitmap(Bitmap bitmap){
        return Bitmap.createScaledBitmap(bitmap,Image_WIDTH  , Image_HEIGHT, false);
    }
    public static Bitmap newScaledBitmap(InputStream in){
        Bitmap bitmap = BitmapFactory.decodeStream(in);
        return Bitmap.createScaledBitmap(bitmap,Image_WIDTH  , Image_HEIGHT, false);
    }
    public static Bitmap imageOrientationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
    private static Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }
    public static Bitmap generateBit(String name){
        return decodeFile(name);
    }
    //decodes image and scales it to reduce memory consumption
    private static Bitmap decodeFile(String name){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1=new FileInputStream(new File(name));
            BitmapFactory.decodeStream(stream1,null,o);
            stream1.close();

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=512;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            FileInputStream stream2=new FileInputStream(new File(name));
            Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            Log.e("Bit size", bitmap.getByteCount() + "");
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 250;
        int targetHeight = 250;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }
}

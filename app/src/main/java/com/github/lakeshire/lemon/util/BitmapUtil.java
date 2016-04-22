package com.github.lakeshire.lemon.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

/**
 * Created by nali on 2016/4/22.
 */
public class BitmapUtil {

    private static Resources getResources(Context context) {
        return context.getResources();
    }

    public static Bitmap resource2Bitmap(Context context, int resource) {
        Resources res = getResources(context);
        Bitmap bitmap = BitmapFactory.decodeResource(res, resource);
        return bitmap;
    }

    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static byte[]  bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap bytes2Bitmap(byte[] b){
        if(b.length != 0){
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static Bitmap reduce(String path, int width, int height) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeFile(path, opt);

        int picWidth  = opt.outWidth;
        int picHeight = opt.outHeight;

        opt.inSampleSize = 1;
        if (width > height) {
            if (picWidth > width)
                opt.inSampleSize = picWidth / width;
        }
        else {
            if (picHeight > height)
                opt.inSampleSize = picHeight / height;
        }

        opt.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, opt);
        return bm;
    }

    public static Bitmap reduce(Context context, int res, int width, int height) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeResource(getResources(context), res, opt);

        int picWidth  = opt.outWidth;
        int picHeight = opt.outHeight;

        opt.inSampleSize = 1;
        if (width > height) {
            if (picWidth > width)
                opt.inSampleSize = picWidth / width;
        }
        else {
            if (picHeight > height)
                opt.inSampleSize = picHeight / height;
        }

        opt.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeResource(getResources(context), res, opt);
        return bm;
    }
}

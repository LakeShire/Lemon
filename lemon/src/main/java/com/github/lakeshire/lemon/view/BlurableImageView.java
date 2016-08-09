package com.github.lakeshire.lemon.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.github.lakeshire.lemon.util.Blur;

import java.lang.ref.WeakReference;


public class BlurableImageView extends ImageView {


    private static int radius = 40;
    private Thread blurThread;
    private Drawable mDrawable;
    private String mUrl;


    public BlurableImageView(Context context) {
        super(context);
    }

    public BlurableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlurableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (blurThread != null) {
            blurThread.interrupt();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public Bitmap justBlur(Drawable drawable) {
        return null;
    }

    public void setResourceUrl(String url) {
        mUrl = url;
    }

    public void blur(Drawable drawable, final String key, final boolean cached) {
        if (drawable != null && drawable instanceof BitmapDrawable) {
            if (!TextUtils.isEmpty(key) && key.equals(mUrl)) {
                return;
            }
            mUrl = key;
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            final WeakReference<Bitmap> bitmapRef = new WeakReference<Bitmap>(bitmap);
            if (bitmap == null) {
                return;
            }
            if (blurThread != null) {
                blurThread.interrupt();
            }
            blurThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap createdBlurBitmap = null;
                    if (bitmapRef.get() == null) {
                        return;
                    }
                    try {
                        createdBlurBitmap = Blur.fastBlur(getContext(), bitmapRef.get(), radius);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                    if (createdBlurBitmap == null) {
                        return;
                    }
                    final Drawable blurDrawable = new BitmapDrawable(getResources(), createdBlurBitmap);
                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Drawable curDrawable = getDrawable();
                            Drawable oldLayerDrawable = null;
                            TransitionDrawable td = null;
                            if (curDrawable instanceof TransitionDrawable) {
                                td = (TransitionDrawable) curDrawable;
                                oldLayerDrawable = td.findDrawableByLayerId(td.getId(1));
                                td.setDrawableByLayerId(td.getId(0), oldLayerDrawable);
                                td.setDrawableByLayerId(td.getId(1), blurDrawable);
                            } else {
                                if (curDrawable == null) {
                                    oldLayerDrawable = new ColorDrawable(getResources().getColor(android.R.color.transparent));
                                } else if (curDrawable instanceof BitmapDrawable) {
                                    oldLayerDrawable = curDrawable;
                                }

                                td = new TransitionDrawable(new Drawable[]{oldLayerDrawable, blurDrawable});
                                td.setCrossFadeEnabled(true);
                                td.setId(0, 0);
                                td.setId(1, 1);
                                BlurableImageView.super.setImageDrawable(td);
                            }

                            td.startTransition(300);
                        }
                    });
                }
            });
            blurThread.start();
        }
    }

    public void blur() {
        mDrawable = getDrawable();
        blur(mDrawable, "", false);
    }

    public void recover() {
        if (blurThread != null) {
            blurThread.interrupt();
        }

        if (mDrawable != null) {
            setImageDrawable(mDrawable);
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
    }


}

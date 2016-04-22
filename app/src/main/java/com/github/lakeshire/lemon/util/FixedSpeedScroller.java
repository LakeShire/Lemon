package com.github.lakeshire.lemon.util;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;
  
public class FixedSpeedScroller extends Scroller {  
  
    private int mDuration = 800;  
  
    public FixedSpeedScroller(Context context) {  
        super(context);  
    }  
  
    public FixedSpeedScroller(Context context, Interpolator interpolator) {  
        super(context, interpolator);  
    }  
  
    public int getmDuration() {  
        return mDuration;  
    }  
  
    public void setmDuration(int time) {  
        mDuration = time;  
    }  
  
    @Override  
    public void startScroll(int startX, int startY, int dx, int dy) {  
        super.startScroll(startX, startY, dx, dy, mDuration);
    }  
  
    @Override  
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {  
        super.startScroll(startX, startY, dx, dy, mDuration);
    }  
}
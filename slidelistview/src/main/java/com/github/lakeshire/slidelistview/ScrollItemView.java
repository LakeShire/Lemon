package com.github.lakeshire.slidelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import java.lang.ref.WeakReference;

/**
 * 可左右滑动的列表项
 */
public class ScrollItemView extends HorizontalScrollView {

    WeakReference<SlideListView> wrListView;

    public ScrollItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollItemView(Context context) {
        super(context);
    }

    public void setSlideListView(SlideListView listview) {
        this.wrListView = new WeakReference(listview);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (wrListView != null && wrListView.get() != null) {
            wrListView.get().setTouchView(this);
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (wrListView != null && wrListView.get() != null && wrListView.get().getTouchView() == this) {
            wrListView.get().onScrollChanged(l, t, oldl, oldt);
        } else {
            super.onScrollChanged(l, t, oldl, oldt);
        }
    }
}

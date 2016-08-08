package com.github.lakeshire.lemonapp.view.morelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.github.lakeshire.lemonapp.view.LoadMoreListView;

public class MoreListView extends LoadMoreListView {

    private int lastX;
    private int lastY;
    private int downX;
    private int downY;

    /**
     * 是否有更多菜单
     */
    private boolean hasMore = true;
    private BaseAdapter adapter;

    public MoreListView(Context context) {
        super(context);
    }

    public MoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //  默认由List处理

        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downY = y;
                lastX = x;
                lastY = y;
                return super.onInterceptTouchEvent(ev);
            case MotionEvent.ACTION_UP:
                if (Math.abs(x - downX) < 5) {
                    adapter.notifyDataSetChanged();
                }
                return super.onInterceptTouchEvent(ev);
            case MotionEvent.ACTION_MOVE:
                int absDeltaX = Math.abs(x - lastX);
                int absDeltaY = Math.abs(y - lastY);
                lastX = x;
                lastY = y;
                // 横向由列表项处理
                if (absDeltaX >= absDeltaY) {
                        return false;
                } else {
                    adapter.notifyDataSetChanged();
                    return super.onInterceptTouchEvent(ev);
                }
            default:
                return true;
        }
    }

    public boolean hasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        this.setAdapter((ListAdapter) adapter);
    }

    public BaseAdapter getAdapter() {
        return this.adapter;
    }
}

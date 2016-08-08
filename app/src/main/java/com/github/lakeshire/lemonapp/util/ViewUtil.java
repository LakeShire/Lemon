package com.github.lakeshire.lemonapp.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

import com.github.lakeshire.lemon.util.ScreenUtil;

import java.lang.reflect.Field;

public class ViewUtil {
    /***
     * 根据item高度动态设置listview的高度（在item高度相同的情况下使用）
     *
     * @param listView
     */
    public static int setListViewHeight(ListView listView) {
        return setListViewHeight(listView, listView.getAdapter(), null);
    }

    public static int setListViewHeight(ListView listView, ListAdapter listAdapter, View headerView) {
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        if (headerView != null) {
            headerView.measure(0, 0);
            totalHeight += headerView.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() + 1));

        listView.setLayoutParams(params);
        return params.height;
    }

    public static void setExpandableListViewHeight(ListView listView, View headerView) {
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View view = listAdapter.getView(0, null, listView);
            view.measure(0, 0);
            totalHeight += view.getMeasuredHeight();
        }
        if (headerView != null) {
            headerView.measure(0, 0);
            totalHeight += headerView.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() + 1));

        listView.setLayoutParams(params);
    }

    public static void expandClickArea(final Context context, final View view, final int left, final int top, final int right, final int bottom) {
        view.post(new Runnable() {

            @Override
            public void run() {
                if (context == null) {
                    return;
                }
                Rect delegateArea = new Rect();
                View delegate = view;
                delegate.getHitRect(delegateArea);
                delegateArea.top -= ScreenUtil.dp2px(context, top);
                delegateArea.bottom += ScreenUtil.dp2px(context, bottom);
                delegateArea.right += ScreenUtil.dp2px(context, right);
                delegateArea.left -= ScreenUtil.dp2px(context, left);
                TouchDelegate expandedArea = new TouchDelegate(delegateArea, delegate);
                if (View.class.isInstance(delegate.getParent())) {
                    ((View) delegate.getParent()).setTouchDelegate(expandedArea);
                }
            }
        });
    }

    public static void setViewPagerScroller(ViewPager pager, Scroller scroller) {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mField.set(pager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static View getItemView(ListView listView, int wantedPosition) {

        if (wantedPosition < 0 || listView == null) {
            return null;
        }

        int firstPosition = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount();

        int wantedChild = wantedPosition - firstPosition;
        if (wantedChild < 0 || wantedChild >= listView.getChildCount()) {
            return null;
        }

        return listView.getChildAt(wantedChild);
    }

    public static void setSelectionIfOutScreen(ListView listview, int position) {
        position += listview.getHeaderViewsCount();
        if (position >= listview.getFirstVisiblePosition() && position <= listview.getLastVisiblePosition()) {
            return;
        }
        listview.setSelection(position);
    }

    public static ViewGroup getContentView(Window window) {
        if (window == null) {
            return null;
        }
        ViewGroup decorView = ((ViewGroup) window.getDecorView());
        if (decorView != null) {
            ViewGroup group = (ViewGroup) decorView.findViewById(android.R.id.content);
            if (group != null) {
                return (ViewGroup) group.getChildAt(0);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static View getChildView(View view) {
        if (view == null) {
            return null;
        }
        if (view instanceof ViewGroup) {
            View childView = ((ViewGroup) view).getChildAt(0);
            if (childView != null) {
                return childView;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static View getGrandChildView(View view) {
        if (view == null) {
            return null;
        }
        view = getChildView(view);
        if (view == null) {
            return null;
        }
        if (view instanceof ViewGroup) {
            View childView = ((ViewGroup) view).getChildAt(0);
            if (childView != null) {
                return childView;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}

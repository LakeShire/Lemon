package com.github.lakeshire.lemon.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.lakeshire.lemon.model.ImageHolder;
import com.github.lakeshire.lemon.util.ImageUtil;
import com.github.lakeshire.lemon.util.ScreenUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * 焦点图适配器
 *
 * @author chadwii
 */
public class FocusImageAdapter<T extends ImageHolder> extends PagerAdapter {
    private Context context;
    private ArrayList<ImageView> focusImageViews = new ArrayList<>();
    private List<T> datas = new ArrayList<>();

    private boolean isCycleScroll = false;
    private boolean isOnlyOnePage = false;

    public FocusImageAdapter(Context context, List<T> list) {
        this(context, list, true);
    }

    public FocusImageAdapter(Context context, List<T> list, boolean isFindingFocusImage) {
        datas = list;
        this.context = context;
        buildPages();
    }

    public FocusImageAdapter(Context context, List<T> list, boolean isFindingFocusImage, String from) {
        datas = list;
        this.context = context;
        buildPages();
    }

    public void setCycleScrollFlag(boolean isCycleScroll) {
        this.isCycleScroll = isCycleScroll;
    }

    public void setOnlyOnePageFlag(boolean isOnlyOnePage) {
        this.isOnlyOnePage = isOnlyOnePage;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (focusImageViews.size() != 0 && isCycleScroll) {
            position = position % focusImageViews.size();
        }
        ImageView iv = focusImageViews.get(position);
        if (iv.getDrawable() != null) {
            iv.getDrawable().setCallback(null);
        }
        iv.setImageBitmap(null);
        iv.setTag(null);
        container.removeView(iv);
    }

    @Override
    public int getCount() {
        if (!isOnlyOnePage && isCycleScroll) {
            return Integer.MAX_VALUE;
        } else {
            return focusImageViews.size();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (focusImageViews.size() != 0 && isCycleScroll) {
            position = position % focusImageViews.size();
        }
        ImageView iv = focusImageViews.get(position);
        final ImageHolder holder = datas.get(position);

        if (holder.url != null && !holder.url.equals("")) {
            ImageUtil.getInstance(context).setImage(iv, holder.url, 0, 0, false);
        } else if (holder.local != 0) {
            ImageUtil.getInstance(context).setImage(iv, holder.local, 0, 0, false);
        }

        if (iv.getParent() != null && iv.getParent() instanceof ViewGroup) {
            ((ViewGroup) iv.getParent()).removeView(iv);
        }
//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) iv.getLayoutParams();
//        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        container.addView(iv);
        return iv;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void notifyDataSetChanged() {
        buildPages();
        super.notifyDataSetChanged();
    }

    private void buildPages() {
        focusImageViews.clear();
        for (@SuppressWarnings("unused") ImageHolder model : datas) {
            ImageView ti = new ImageView(context);
            ti.setScaleType(ImageView.ScaleType.FIT_XY);
            int width = ScreenUtil.getScreenWidth((Activity) context);
            int height = (int) (width * (1080 / 1920f));
//            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, height);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
            ti.setLayoutParams(lp);
            focusImageViews.add(ti);
        }
    }

    @SuppressWarnings("deprecation")
    public void destory() {
        if (focusImageViews != null && focusImageViews.size() > 0) {
            Iterator<ImageView> iterator = focusImageViews.iterator();
            while (iterator.hasNext()) {
                ImageView imgView = iterator.next();
                iterator.remove();
                imgView.setImageDrawable(null);
                imgView.setBackgroundDrawable(null);
            }
        }
    }

    public Object getItem(int position) {
        return focusImageViews.get(position % focusImageViews.size());
    }
}


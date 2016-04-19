package com.github.lakeshire.lemon.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
    protected final SparseArray<View> mItemViewsArray;
    protected View mConvertView;
    protected Context mContext;
    public boolean isFirst = true;

    public ViewHolder(int resId, Context context) {
        mContext = context;
        mItemViewsArray = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(resId, null);
        mConvertView.setTag(this);
    }

    public static ViewHolder getView(View convertView, int layoutId, Context context) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder(layoutId, context);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return viewHolder;
    }


    /**
     * 设置View的显示状态
     *
     * @param resId        View的id
     * @param visiableType 显示状态
     */
    public void setVisibility(int resId, int visiableType) {
        if (resId > 0) {
            getItemView(resId).setVisibility(visiableType);
        }
    }

    /**
     * 设置文字颜色
     *
     * @param resId 控件id
     * @param res   文字颜色资源
     */
    public void setTextColor(int resId, int res) {
        if (resId > 0 && res > 0) {
            ((TextView) getItemView(resId)).setTextColor(mContext.getResources().getColor(res));
        }
    }

    /**
     * 设置背景
     *
     * @param resId 控件id
     * @param res   背景颜色资源
     */
    public void setBgColor(int resId, int res) {
        if (resId > 0 && res > 0) {
            ((View) getItemView(resId)).setBackgroundColor(res);
        }
    }

    /**
     * 设置背景
     *
     * @param resId 控件id
     * @param res   背景资源
     */
    public void setBgDrawable(int resId, int res) {
        if (resId > 0 && res > 0) {
            ((View) getItemView(resId)).setBackgroundResource(res);
        }
    }

    /**
     * 设置View的显示状态
     *
     * @param resId    View的id
     * @param visiable 是否显示
     */
    public void setVisibility(int resId, boolean visiable) {
        if (resId > 0) {
            if (visiable) {
                getItemView(resId).setVisibility(View.VISIBLE);
            } else {
                getItemView(resId).setVisibility(View.GONE);
            }

        }
    }

    /**
     * 初始化控件
     *
     * @param resId 资源控件的id
     * @return View 通过id初始化好的view
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getItemView(int resId) {
        View view = mItemViewsArray.get(resId);
        if (view == null) {
            view = mConvertView.findViewById(resId);
            if (view != null) {
                mItemViewsArray.put(resId, view);
            }
        }
        return (T) view;
    }

    /**
     * 给textview设置文字
     *
     * @param resId   TextView的id
     * @param content 内容
     */
    public void setText(int resId, String content) {
        if (resId > 0 && !TextUtils.isEmpty(content)) {
            ((TextView) getItemView(resId)).setText(content);
        }
    }

    /**
     * 给imageview设置网络图片资源加载
     *
     * @param resId ImageView控件的id
     * @param url   图片的链接
     */
    public void setImageResource(int resId, String url) {
        setImageResource(resId, url, -1);
    }

    /**
     * 给imageview设置网络图片资源加载
     *
     * @param resId        ImageView控件的id
     * @param url          图片的链接
     * @param defaultResId 默认图片的id
     */
    public void setImageResource(int resId, String url, int defaultResId) {
        if (resId > 0 && !TextUtils.isEmpty(url)) {
            //  TODO:
        }
    }

    /**
     * 给imageview设置本地图片资源加载
     *
     * @param resId ImageView的Id
     * @param res   图片resId
     */
    public void setImageResource(int resId, int res) {
        if (resId > 0 && res > 0) {
            ((ImageView) getItemView(resId)).setImageResource(res);
        }
    }

    /**
     * @return View 与viewholder绑定的convertView
     */
    public View getContertView() {
        return this.mConvertView;
    }
}

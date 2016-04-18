package com.github.lakeshire.lemon.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.github.lakeshire.lemon.R;

import java.util.List;

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter implements View.OnClickListener {
    protected Context mContext;
    protected List<T> mListData;
    protected int mLayoutResId;

    public BaseAdapter(Context mContext, List<T> mListData, int mLayoutResId) {
        super();
        this.mContext = mContext;
        this.mListData = mListData;
        this.mLayoutResId = mLayoutResId;
    }

    @Override
    public int getCount() {
        return mListData != null ? mListData.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return mListData != null ? mListData.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int indexOf(T item) {
        if (mListData != null) {
            return mListData.indexOf(item);
        } else {
            return -1;
        }
    }

    public boolean containItem(T item) {

        if (mListData == null) {
            return false;
        } else {
            return mListData.contains(item);
        }

    }

    public List<T> getListData() {
        return mListData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.getView(convertView, mLayoutResId, mContext);
        bindViewData(viewHolder, getItem(position), position);
        return viewHolder.getContertView();
    }

    /**
     * 给view绑定事件
     */
    public void setClickListener(View view, T t, int position, ViewHolder viewHolder) {
        view.setOnClickListener(this);
        view.setTag(R.id.view_holder_position, new Integer(position));
        view.setTag(R.id.view_holder_data, t);
        view.setTag(R.id.view_holder, viewHolder);
    }

    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag(R.id.view_holder_position);
        T t = (T) view.getTag(R.id.view_holder_data);
        ViewHolder holder = (ViewHolder) view.getTag(R.id.view_holder);
        onItemViewClick(view, t, position, holder);
    }

    /**
     * 当item的子view点击的事件
     * 如果需要监听点击事件，子类覆盖去重写此方法
     *
     * @param view
     * @param t
     * @param position
     * @param holder
     */
    protected void onItemViewClick(View view, T t, int position, ViewHolder holder) {

    }

    /**
     * @param viewHolder
     * @param item
     */
    public abstract void bindViewData(ViewHolder viewHolder, T item, int position);

}

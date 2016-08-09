package com.github.lakeshire.slidelistview;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.github.lakeshire.lemon.adapter.base.BaseAdapter;
import com.github.lakeshire.lemon.adapter.base.ViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

public abstract class SlideAdapter<T extends CheckableModel> extends BaseAdapter<T> {

    private WeakReference<SlideListView> wrListView = null;

    public SlideAdapter(Context mContext, List<T> mListData, int mLayoutResId, SlideListView list) {
        super(mContext, mListData, mLayoutResId);
        if (list instanceof SlideListView) {
            wrListView = new WeakReference(list);
        }
    }

    @Override
    public void bindViewData(ViewHolder viewHolder, final T item, int position) {
        ScrollItemView scrollView = viewHolder.getItemView(R.id.item_scroll);
        if (wrListView != null && wrListView.get() != null) {
            scrollView.setSlideListView(wrListView.get());
            wrListView.get().addHViews(scrollView);
        }

        if (wrListView != null && wrListView.get() != null) {
            int mode = wrListView.get().getMode();
            if (mode == SlideListView.MODE_DISPLAY) {
                viewHolder.setVisibility(R.id.check, View.GONE);
            } else {
                viewHolder.setVisibility(R.id.check, View.VISIBLE);
            }
        }

        //  对列表项中CheckBox的处理
        CheckBox checkBox = viewHolder.getItemView(R.id.check);
        if (checkBox != null) {
            checkBox.setChecked(getItem(position).isChecked());
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        item.setChecked(true);
                    } else {
                        item.setChecked(false);
                    }
                }
            });
        }
        bindData(viewHolder, item);
    }

    protected abstract void bindData(ViewHolder viewHolder, T item);
}

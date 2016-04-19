package com.github.lakeshire.lemon.adapter.slidelistview;

import android.content.Context;
import android.widget.TextView;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.adapter.ViewHolder;
import com.github.lakeshire.lemon.model.slidelistview.Model;
import com.github.lakeshire.lemon.model.slidelistview.MyModel;
import com.github.lakeshire.lemon.view.slidelistview.SlideListView;

import java.util.List;

/**
 * Created by nali on 2016/4/12.
 */
public class MySlideAdapter extends SlideAdapter<MyModel> {
    private final int red;
    private final int green;
    private final int black;

    public MySlideAdapter(Context mContext, List<MyModel> mListData, int mLayoutResId, SlideListView list) {
        super(mContext, mListData, mLayoutResId, list);
        red = mContext.getResources().getColor(R.color.red);
        green = mContext.getResources().getColor(R.color.green);
        black = mContext.getResources().getColor(R.color.black);
    }

    @Override
    protected void bindData(ViewHolder viewHolder, MyModel item) {
        viewHolder.setText(R.id.item_title, item.getModel().title);
        viewHolder.setText(R.id.item_data1, item.getModel().data1);
        int change = item.getModel().change;
        if (change == Model.INCREASE) {
            ((TextView) viewHolder.getItemView(R.id.item_data1)).setTextColor(red);
            ((TextView) viewHolder.getItemView(R.id.item_data2)).setTextColor(red);
            ((TextView) viewHolder.getItemView(R.id.item_data3)).setTextColor(red);
        } else if (change == Model.DECREASE) {
            ((TextView) viewHolder.getItemView(R.id.item_data1)).setTextColor(green);
            ((TextView) viewHolder.getItemView(R.id.item_data2)).setTextColor(green);
            ((TextView) viewHolder.getItemView(R.id.item_data3)).setTextColor(green);
        } else {
            ((TextView) viewHolder.getItemView(R.id.item_data1)).setTextColor(black);
            ((TextView) viewHolder.getItemView(R.id.item_data2)).setTextColor(black);
            ((TextView) viewHolder.getItemView(R.id.item_data3)).setTextColor(black);
        }
        viewHolder.setText(R.id.item_data2, item.getModel().data2);
        viewHolder.setText(R.id.item_data3, item.getModel().data3);
        viewHolder.setText(R.id.item_data4, item.getModel().data4);
        viewHolder.setText(R.id.item_data5, item.getModel().data5);
        viewHolder.setText(R.id.item_data6, item.getModel().data6);
        viewHolder.setText(R.id.item_data7, item.getModel().data7);
    }
}

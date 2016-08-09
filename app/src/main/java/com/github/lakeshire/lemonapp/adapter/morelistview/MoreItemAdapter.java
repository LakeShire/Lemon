package com.github.lakeshire.lemonapp.adapter.morelistview;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.github.lakeshire.extralistview.ExtraItem;
import com.github.lakeshire.lemon.adapter.base.BaseAdapter;
import com.github.lakeshire.lemon.adapter.base.ViewHolder;
import com.github.lakeshire.lemonapp.R;
import com.github.lakeshire.lemonapp.model.CheckableModel;

import java.util.List;

/**
 * Created by nali on 2016/4/7.
 */

public class MoreItemAdapter<T extends CheckableModel> extends BaseAdapter<T> {

    private final Context mContext;

    public interface Callback {
        void onExtraClicked(int res, int position);
        void onItemClicked(int position);
        void onItemLongClicked(int position);
    }
    protected Callback cb;

    public void setCb(Callback cb) {
        this.cb = cb;
    }

    public MoreItemAdapter(Context context, List<T> datas, int res) {
        super(context, datas, res);
        mContext = context;
    }

    @Override
    public void bindViewData(ViewHolder viewHolder, final T item, final int position) {
        ExtraItem convertView = ((ExtraItem) (viewHolder.getConvertView()));
        convertView.hideExtra();

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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb != null) {
                    cb.onItemClicked(position);
                }
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (cb != null) {
                    cb.onItemLongClicked(position);
                }
                return false;
            }
        });
    }
}
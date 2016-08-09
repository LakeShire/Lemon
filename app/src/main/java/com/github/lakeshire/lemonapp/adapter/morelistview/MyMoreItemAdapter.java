package com.github.lakeshire.lemonapp.adapter.morelistview;

import android.content.Context;
import android.view.View;

import com.github.lakeshire.lemon.adapter.base.ViewHolder;
import com.github.lakeshire.lemonapp.R;
import com.github.lakeshire.lemonapp.model.MoreModel;

import java.util.List;

/**
 * Created by nali on 2016/4/11.
 */
public class MyMoreItemAdapter extends MoreItemAdapter<MoreModel> {

    public MyMoreItemAdapter(Context context, List<MoreModel> datas, int res) {
        super(context, datas, res);
    }

    @Override
    public void bindViewData(ViewHolder viewHolder, MoreModel item, final int position) {
        super.bindViewData(viewHolder, item, position);
        viewHolder.getItemView(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb.onExtraClicked(R.id.delete, position);
            }
        });
        viewHolder.setText(R.id.tv_id, item.getModel().title);
    }
}

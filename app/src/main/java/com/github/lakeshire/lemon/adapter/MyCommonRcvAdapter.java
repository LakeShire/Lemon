package com.github.lakeshire.lemon.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.lakeshire.lemon.model.ButtonItem;
import com.github.lakeshire.lemon.model.DemoModel;
import com.github.lakeshire.lemon.model.ImageItem;
import com.github.lakeshire.lemon.model.TextItem;

import java.util.List;

import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

/**
 * Created by nali on 2016/4/25.
 */
public class MyCommonRcvAdapter extends CommonRcvAdapter<DemoModel> {

    public MyCommonRcvAdapter(List<DemoModel> data) {
        super(data);
    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public Object getItemType(DemoModel demoModel) {
        return demoModel.type;
    }

    @NonNull
    @Override
    public AdapterItem createItem(Object type) {
        switch (((String) type)) {
            case "text":
                return new TextItem();
            case "button":
                return new ButtonItem();
            case "image":
                return new ImageItem();
            default:
                throw new IllegalArgumentException("不合法的type");
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

//        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
//        lp.height = (int) (100 + Math.random() * 300);
//        holder.itemView.setLayoutParams(lp);

        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }
}

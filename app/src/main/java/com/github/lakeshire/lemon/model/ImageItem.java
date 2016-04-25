package com.github.lakeshire.lemon.model;

import android.view.View;
import android.widget.ImageView;
import com.github.lakeshire.lemon.R;
import kale.adapter.item.AdapterItem;

public class ImageItem implements AdapterItem<DemoModel> {

    @Override
    public int getLayoutResId() {
        return R.layout.item_common_image;
    }

    ImageView imageView;

    @Override
    public void bindViews(View root) {
        imageView = (ImageView) root.findViewById(R.id.imageview);
    }

    @Override
    public void setViews() { }

    @Override
    public void handleData(DemoModel model, int position) {
        imageView.setImageResource(R.drawable.path_music);
    }
}

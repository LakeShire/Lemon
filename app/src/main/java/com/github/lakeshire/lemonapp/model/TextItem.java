package com.github.lakeshire.lemonapp.model;

import android.view.View;
import android.widget.TextView;

import com.github.lakeshire.lemonapp.R;

import kale.adapter.item.AdapterItem;

public class TextItem implements AdapterItem<DemoModel> {

    @Override
    public int getLayoutResId() {
        return R.layout.item_common_text;
    }

    TextView textView;

    @Override
    public void bindViews(View root) {
        textView = (TextView) root.findViewById(R.id.textview);
    }

    @Override
    public void setViews() { }

    @Override
    public void handleData(DemoModel model, int position) {
        textView.setText(model.content);
    }
}
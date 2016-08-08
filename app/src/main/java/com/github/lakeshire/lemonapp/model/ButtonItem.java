package com.github.lakeshire.lemonapp.model;

import android.view.View;
import android.widget.Button;

import com.github.lakeshire.lemonapp.R;

import kale.adapter.item.AdapterItem;

public class ButtonItem implements AdapterItem<DemoModel> {

    @Override
    public int getLayoutResId() {
        return R.layout.item_common_button;
    }

    Button button;

    @Override
    public void bindViews(View root) {
        button = (Button) root.findViewById(R.id.button);
    }

    @Override
    public void setViews() { }

    @Override
    public void handleData(DemoModel model, final int position) {
        button.setText(model.content);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "点击了按钮: " + position, Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}

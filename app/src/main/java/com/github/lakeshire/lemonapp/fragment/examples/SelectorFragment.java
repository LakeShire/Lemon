package com.github.lakeshire.lemonapp.fragment.examples;

import android.view.View;

import com.github.lakeshire.lemon.fragment.base.BasePullFragment;
import com.github.lakeshire.lemonapp.R;

import butterknife.Bind;
import kale.injection.SelectorInjection;
import kale.ui.view.SelectorTextView;

/**
 * Created by nali on 2016/4/20.
 */
public class SelectorFragment extends BasePullFragment {

    @Bind(R.id.stv_normal)
    SelectorTextView view;
    private SelectorInjection injection;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_selector;
    }

    @Override
    public void initUi() {
        super.initUi();
        injection = view.getInjection();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setChecked(!view.isChecked());
            }
        });
    }
}

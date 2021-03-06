package com.github.lakeshire.lemonapp.fragment.examples;

import com.github.lakeshire.lemon.fragment.base.BasePullFragment;
import com.github.lakeshire.lemonapp.R;
import com.github.lakeshire.lemonapp.view.AnimatePieChart;

import butterknife.Bind;

public class AnimatePieFragment extends BasePullFragment {

    @Bind(R.id.pie)
    AnimatePieChart mPie;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_animate_pie;
    }

    @Override
    public void initUi() {
        super.initUi();
        mPie.setDataCount(5);
        mPie.addData(0, 5, getResources().getColor(R.color.tiffany));
        mPie.addData(1, 15, getResources().getColor(R.color.orange_fe9d01));
        mPie.addData(2, 20, getResources().getColor(R.color.green_1fda9a));
        mPie.addData(3, 5, getResources().getColor(R.color.blue_70e1ff));
        mPie.addData(4, 5, getResources().getColor(R.color.purple_ccccfb));
        mPie.startDraw();
    }
}

package com.github.lakeshire.lemonapp.fragment.examples;

import android.view.View;
import android.widget.ScrollView;

import com.github.lakeshire.lemon.fragment.base.BasePullFragment;
import com.github.lakeshire.lemonapp.R;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by nali on 2016/4/21.
 */
public class SlidingLayoutFragment extends BasePullFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sliding;
    }

    @Bind(R.id.scroll_view)
    ScrollView mScrollView;

    @Override
    public void initUi() {
        super.initUi();
    }

    protected void onRefresh(PtrFrameLayout frame) {
        onRefresh(frame);
    }

    protected boolean checkCanRefresh(PtrFrameLayout frame, View content, View header) {
        return mScrollView.getScrollY() <= 0;
    }
}

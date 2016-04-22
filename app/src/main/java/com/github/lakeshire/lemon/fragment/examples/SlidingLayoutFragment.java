package com.github.lakeshire.lemon.fragment.examples;

import android.view.View;
import android.widget.ScrollView;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.fragment.base.BasePullFragment;
import com.github.lakeshire.lemon.view.pulltofresh.EnhanceHeader;
import com.github.lakeshire.lemon.view.pulltofresh.EnhancePtrFrameLayout;
import com.github.lakeshire.lemon.view.pulltofresh.PtrHandler;

import butterknife.Bind;

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
        EnhanceHeader header = new EnhanceHeader(getActivity(), 0);
        header.setCustomTitleBar(true);

        mPtrFrameLayout.setDurationToCloseHeader(1500);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.setPinHeader(false);
        mPtrFrameLayout.addPtrUIHandler(header);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(EnhancePtrFrameLayout frame, View content, View header) {
                return mScrollView.getScrollY() <= 0;
            }

            @Override
            public void onRefreshBegin(EnhancePtrFrameLayout frame) {
                onRefresh(frame);
            }
        });
    }
}

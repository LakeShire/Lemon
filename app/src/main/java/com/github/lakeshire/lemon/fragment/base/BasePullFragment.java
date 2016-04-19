package com.github.lakeshire.lemon.fragment.base;

import android.view.View;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.view.pulltofresh.EnhanceHeader;
import com.github.lakeshire.lemon.view.pulltofresh.EnhancePtrFrameLayout;
import com.github.lakeshire.lemon.view.pulltofresh.PtrHandler;

public abstract class BasePullFragment extends BaseFragment {
	protected EnhancePtrFrameLayout mPtrFrameLayout;

	public void initUi() {
		super.initUi();
		mPtrFrameLayout = (EnhancePtrFrameLayout) mContainerView.findViewById(R.id.ptr_frame);
		EnhanceHeader header = new EnhanceHeader(getActivity(), 0);
		header.setCustomTitleBar(true);

		mPtrFrameLayout.setDurationToCloseHeader(1500);
		mPtrFrameLayout.setHeaderView(header);
		mPtrFrameLayout.setPinHeader(false);
		mPtrFrameLayout.addPtrUIHandler(header);
		mPtrFrameLayout.setPtrHandler(new PtrHandler() {
			@Override
			public boolean checkCanDoRefresh(EnhancePtrFrameLayout frame, View content, View header) {
				return checkCanRefresh(frame, content, header);
			}

			@Override
			public void onRefreshBegin(EnhancePtrFrameLayout frame) {
				onRefresh(frame);
			}
		});

	}

	protected void onRefresh(EnhancePtrFrameLayout frame) {
		mPtrFrameLayout.refreshComplete();
	}

	protected boolean checkCanRefresh(EnhancePtrFrameLayout frame, View content, View header) {
		return true;
	}
}

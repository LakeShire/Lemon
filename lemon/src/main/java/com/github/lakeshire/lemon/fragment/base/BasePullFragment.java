package com.github.lakeshire.lemon.fragment.base;

import android.view.View;

import com.github.lakeshire.lemon.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

public abstract class BasePullFragment extends BaseFragment {
	protected PtrFrameLayout mPtrFrameLayout;

	public void initUi() {
		super.initUi();
		mPtrFrameLayout = (PtrFrameLayout) mContainerView.findViewById(R.id.ptr_frame);
		MaterialHeader header = new MaterialHeader(getActivity());
		mPtrFrameLayout.setDurationToCloseHeader(1500);
		mPtrFrameLayout.setHeaderView(header);
		mPtrFrameLayout.addPtrUIHandler(header);
		mPtrFrameLayout.setPtrHandler(new PtrHandler() {
			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
				return checkCanRefresh(frame, content, header);
			}

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				onRefresh(frame);
			}
		});

	}

	protected void onRefresh(PtrFrameLayout frame) {
		mPtrFrameLayout.refreshComplete();
	}

	protected boolean checkCanRefresh(PtrFrameLayout frame, View content, View header) {
		return true;
	}
}

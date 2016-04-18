package com.github.lakeshire.lemon.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.activity.BaseActivity;
import com.github.lakeshire.lemon.view.pulltofresh.EnhanceHeader;
import com.github.lakeshire.lemon.view.pulltofresh.EnhancePtrFrameLayout;
import com.github.lakeshire.lemon.view.pulltofresh.PtrHandler;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

	protected View mContainerView;
	private EnhancePtrFrameLayout mPtrFrameLayout;

	public void startFragment(Class<?> clazz) {
		((BaseActivity) getActivity()).startFragment(clazz);
	}
	
	public void endFragment() {
		((BaseActivity) getActivity()).endFragment();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContainerView = inflater.inflate(getLayoutId(), container, false);
		ButterKnife.bind(this, mContainerView);
		initUi();
		return mContainerView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadData();
	}

	public void initUi() {
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

	public void loadData() {

	}

	protected void l(String log) {
		Log.i(this.getClass().getSimpleName(), log);
	}

	public abstract int getLayoutId();
}

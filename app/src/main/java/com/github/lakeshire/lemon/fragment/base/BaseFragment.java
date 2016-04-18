package com.github.lakeshire.lemon.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.lakeshire.lemon.activity.BaseActivity;
import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

	public void startFragment(Class<?> clazz) {
		((BaseActivity) getActivity()).startFragment(clazz);
	}
	
	public void endFragment() {
		((BaseActivity) getActivity()).endFragment();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(getLayoutId(), container, false);
		ButterKnife.bind(this, view);
		initUi();
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadData();
	}

	public void initUi() {

	}

	public void loadData() {

	}

	protected void l(String log) {
		Log.i(this.getClass().getSimpleName(), log);
	}

	public abstract int getLayoutId();
}

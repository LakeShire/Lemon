package com.github.lakeshire.lemon.fragment.examples;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.fragment.base.BaseFragment;

import butterknife.OnClick;

public class RunFragment extends BaseFragment {

	@OnClick(R.id.btn_run)
	public void run() {
		startFragment(TestFragment.class);
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_run;
	}
}

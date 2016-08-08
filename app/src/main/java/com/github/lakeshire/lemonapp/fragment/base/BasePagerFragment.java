package com.github.lakeshire.lemonapp.fragment.base;

import android.widget.ListView;

/**
 * 嵌套滑动页面Pager内容Fragment基类
 * 
 * @author louis.liu
 * 
 */
public abstract class BasePagerFragment extends BasePullFragment {

	private static final String TAG = "BasePagerFragment";

	@Override
	public void initUi() {
		super.initUi();
	}

	/**
	 * 获取页面中的ListView
	 * 
	 * @return
	 */
	public abstract ListView getListView();
}

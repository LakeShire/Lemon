package com.github.lakeshire.lemon.fragment.base;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;
import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.adapter.PagerAdapter;
import com.github.lakeshire.lemon.util.ScreenUtil;
import com.github.lakeshire.lemon.view.multiscroll.MultiScrollView;


/**
 * 嵌套滑动Fragment基类 布局要求有一个id为tabs的PagerSlidingTabStrip，一个id为pager的ViewPager
 * 或者有一个id为list的ListView
 * 
 * @author louis.liu
 * 
 */
public abstract class BaseScrollFragment extends BaseFragment {

	/**
	 * 嵌套滑动容器
	 */
	protected MultiScrollView mMainView;
	/**
	 * 数据列表 用于单页列表
	 */
//	protected List<StoryBaseModel> mData;
	/**
	 * 数据适配器 用于单页列表
	 */
//	protected StoryModuleDetailsAdapter mListAdapter;
	/**
	 * 单页列表
	 */
	protected ListView mListView;
	/**
	 * 滑动标签栏
	 */
	protected PagerSlidingTabStrip mTabs;
	/**
	 * 滑动页面
	 */
	protected ViewPager mPager;
	/**
	 * 滑动页面适配器
	 */
	protected PagerAdapter mPagerAdapter;

	@Override
	public void initUi() {
		super.initUi();

		mMainView = (MultiScrollView) mContainerView.findViewById(R.id.main_view);

		// Banner
		// 滑动时不需要一直显示的东西用addHeaderView
		initHeaders();

		// Tab
		initTabs();

		// Pager
		initPager();

		// List
		initList();

		// 设置保留高度 应由子类实现
//		setReserveHeight();
	}

	protected void initHeaders() {
		ViewGroup banner = (ViewGroup) mMainView.findViewById(R.id.banner);
		mMainView.addHeaderView(banner);
	}

	protected void initTabs() {
		mTabs = (PagerSlidingTabStrip) mMainView.findViewById(R.id.tabs);
		mMainView.setTabs(mTabs);
		if (mTabs != null) {
			initTabStyle();
		}
	}

	protected void initPager() {
		mPager = (ViewPager) mMainView.findViewById(R.id.pager);
		mMainView.setPager(mPager);
		if (mPager != null) {
			buildPages();
			mPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageSelected(int position) {
					// 这里需要通知一下Tab
					// 刷新一下主容器的列表引用
					mTabs.notifyDataSetChanged();
					mMainView.refreshView();
				}

			});
			if (mTabs != null) {
				buildTabs();
			}
		}
	}

	protected void initList() {
		mListView = (ListView) mMainView.findViewById(R.id.list);
		if (mListView != null) {
			getListAdapter();
//			mListView.setAdapter(mListAdapter);
			setListViewHeightBasedOnChildren(mListView);
		} else {
			// Fragment fragment = getActivity().getSupportFragmentManager()
			// .findFragmentById(R.id.fragment);
			// Log.i(TAG, "fragment: " + fragment);
			// if (fragment != null)
			// {
			// mListView = (ListView) fragment.getView().findViewById(
			// R.id.list);
			// Log.i(TAG, "list: " + mListView);
			// }

		}
		mMainView.setListView(mListView);
	}

	/**
	 * 获取列表数据适配器 应该由子类实现
	 */
	protected void getListAdapter() {
//		mListAdapter = new StoryModuleDetailsAdapter(getActivity(), mData, R.layout.item_common_content);
	}

	protected void setReserveHeight(int height) {
		mMainView.setReserveHeight(height);
	}

	protected void buildPages() {
		mPagerAdapter = getPagerAdapter();
		mPager.setAdapter(mPagerAdapter);
		mMainView.setPagerAdapter(mPagerAdapter);
	}

	protected void buildTabs() {
		mTabs.setViewPager(mPager);
	}

	/**
	 * 获取Pager数据适配器,类似 mPagerAdapter = new
	 * PagerAdapter(getActivity().getSupportFragmentManager(),
	 * "com.ximalaya.device.wifistorymachine.fragment.online.BlankFragment");
	 */
	abstract protected PagerAdapter getPagerAdapter();

	protected void initTabStyle() {
		mTabs.setDividerColor(Color.TRANSPARENT);
		mTabs.setIndicatorColor(getResources().getColor(R.color.tiffany));
		mTabs.setUnderlineHeight(0);
		mTabs.setDividerPadding(0);
//		mTabs.setDeactivateTextColor(Color.parseColor("#666666"));
//		mTabs.setActivateTextColor(Color.parseColor("#f86442"));
//		mTabs.setTabSwitch(true);
		mTabs.setShouldExpand(true);
		mTabs.setTextSize(20);
		mTabs.setIndicatorHeight(ScreenUtil.dp2px(getActivity(), 3));
		mTabs.setTabPaddingLeftRight(ScreenUtil.dp2px(getActivity(), 17));
//		mTabs.setDisallowInterceptTouchEventView((ViewGroup) mTabs.getParent());
	}

	/**
	 * 调整ListView高度 不知道这里有没有用
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}

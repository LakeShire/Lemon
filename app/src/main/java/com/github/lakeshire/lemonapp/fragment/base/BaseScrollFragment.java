package com.github.lakeshire.lemonapp.fragment.base;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.astuetz.PagerSlidingTabStrip;
import com.github.lakeshire.lemon.fragment.base.BaseFragment;
import com.github.lakeshire.lemon.util.ScreenUtil;
import com.github.lakeshire.lemonapp.R;
import com.github.lakeshire.stickyheaderlayout.PagerAdapter;
import com.github.lakeshire.stickyheaderlayout.StickyHeaderLayout;


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
	protected StickyHeaderLayout mMainView;
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
		mMainView = (StickyHeaderLayout) mContainerView.findViewById(R.id.main_view);
		initTabs();
		initPager();
	}

	protected void initTabs() {
		mTabs = (PagerSlidingTabStrip) mMainView.findViewById(R.id.tabs);
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
					mTabs.notifyDataSetChanged();
				}

			});
			if (mTabs != null) {
				buildTabs();
			}
		}
	}

	protected void buildPages() {
		mPagerAdapter = getPagerAdapter();
		mPager.setAdapter(mPagerAdapter);
		mMainView.setPagerAdapter(mPagerAdapter);
	}

	/**
	 * 获取Pager数据适配器
	 */
	abstract protected PagerAdapter getPagerAdapter();

	protected void buildTabs() {
		mTabs.setViewPager(mPager);
	}

	protected void initTabStyle() {
		mTabs.setDividerColor(Color.TRANSPARENT);
		mTabs.setIndicatorColor(getResources().getColor(R.color.tiffany));
		mTabs.setUnderlineHeight(0);
		mTabs.setDividerPadding(0);
		mTabs.setShouldExpand(true);
		mTabs.setTextSize(20);
		mTabs.setIndicatorHeight(ScreenUtil.dp2px(getActivity(), 3));
		mTabs.setTabPaddingLeftRight(ScreenUtil.dp2px(getActivity(), 17));
	}
}

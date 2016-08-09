package com.github.lakeshire.stickyheaderlayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

/**
 * a demo base fragment include a banner, a PagerSlidingTabStrip and a ViewPager
 * you can build your own fragment like this or inherit this directly
 */
public abstract class BaseStickyFragment extends Fragment {

    protected View mContainerView;
    protected StickyHeaderLayout mMainView;
    protected PagerSlidingTabStrip mTabs;
    protected ViewPager mPager;
    protected PagerAdapter mPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainerView = inflater.inflate(getLayoutResource(), null);
        mMainView = (StickyHeaderLayout) mContainerView.findViewById(R.id.main_view);
        initTabs();
        initPager();
        return mContainerView;
    }

    protected int getLayoutResource() {
        return R.layout.fragment_base_sticky;
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
            mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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
        mTabs.setIndicatorColor(Color.BLACK);
        mTabs.setUnderlineHeight(0);
        mTabs.setDividerPadding(0);
        mTabs.setShouldExpand(true);
        mTabs.setTextSize(20);
        mTabs.setIndicatorHeight(ScreenUtil.dp2px(getActivity(), 3));
        mTabs.setTabPaddingLeftRight(ScreenUtil.dp2px(getActivity(), 17));
    }
}

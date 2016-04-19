package com.github.lakeshire.lemon.fragment.examples;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.fragment.base.BasePullFragment;

public class PagerFragment extends BasePullFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pager;
    }

    private PagerSlidingTabStrip mTab;
    private ViewPager mPager;

    public String[] titles = {"音乐", "游戏", "软件"};
    public int[] icons = {R.drawable.music, R.drawable.game, R.drawable.soft};
    public PageDetailFragment fragments[];
    private MyAdapter mAdapter;

    @Override
    public void initUi() {
        super.initUi();
        fragments = new PageDetailFragment[3];
        for (int i = 0; i < 3; i++) {
            fragments[i] = PageDetailFragment.newInstance(titles[i], icons[i]);
        }

        mTab = (PagerSlidingTabStrip) find(R.id.tabs);
        mPager = (ViewPager) find(R.id.pager);
        mAdapter = new MyAdapter(getChildFragmentManager());
        mPager.setAdapter(mAdapter);
        mTab.setViewPager(mPager);
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Override
    public boolean onBackPressed() {
        PageDetailFragment fragment = fragments[mPager.getCurrentItem()];
        if (fragment.onBackPressed()) {
            return true;
        } else {
            return super.onBackPressed();
        }
    }
}

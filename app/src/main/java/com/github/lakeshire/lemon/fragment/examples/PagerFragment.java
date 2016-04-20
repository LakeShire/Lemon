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

    public String[] titles = {"音乐", "游戏", "ListView", "RecyclerView"};
    public int[] icons = {R.drawable.music, R.drawable.game, R.drawable.soft};
    public Fragment fragments[];
    private MyAdapter mAdapter;

    @Override
    public void initUi() {
        super.initUi();
        fragments = new Fragment[4];
        fragments[0] = PageDetailFragment.newInstance(titles[0], icons[0]);
        fragments[1] = PageDetailFragment.newInstance(titles[1], icons[1]);
        fragments[2] = new CommonListFragment();
        fragments[3] = new RecycleListFragment();

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
        Fragment fragment = fragments[mPager.getCurrentItem()];
        if (fragment instanceof  PageDetailFragment) {
            PageDetailFragment f = (PageDetailFragment) fragment;
            if (f.onBackPressed()) {
                return true;
            } else {
                return super.onBackPressed();
            }
        } else {
            return super.onBackPressed();
        }
    }
}

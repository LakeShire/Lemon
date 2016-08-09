package com.github.lakeshire.lemonapp.fragment.examples;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.github.lakeshire.lemon.util.ScreenUtil;
import com.github.lakeshire.lemonapp.R;
import com.github.lakeshire.lemonapp.adapter.FocusImageAdapter;
import com.github.lakeshire.lemonapp.fragment.base.BaseScrollFragment;
import com.github.lakeshire.lemonapp.model.ImageHolder;
import com.github.lakeshire.lemonapp.util.FixedSpeedScroller;
import com.github.lakeshire.lemonapp.util.ViewUtil;
import com.github.lakeshire.lemonapp.view.pageindicator.CirclePageIndicator;
import com.github.lakeshire.stickyheaderlayout.PageInfo;
import com.github.lakeshire.stickyheaderlayout.PagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nali on 2016/4/21.
 */
public class MultiScrollFragment extends BaseScrollFragment {

    public String[] titles = {"音乐", "游戏", "ListView", "RecyclerView"};
    private List<ImageHolder> mFocusImages = new ArrayList<>();
    private FocusImageAdapter mFocusAdapter;
    private CirclePageIndicator mFocusIndicator;
    private ViewPager mFocusPager;
    private TimerTask mTask;
//    public int[] icons = {R.drawable.music, R.drawable.game, R.drawable.soft};
//    public Fragment fragments[];

    @Override
    protected PagerAdapter getPagerAdapter() {
        List<PageInfo> pages = new ArrayList<>();
        pages.add(new PageInfo(CommonListFragment.class.getName(), null));
        pages.add(new PageInfo(CommonListFragment.class.getName(), null));
        pages.add(new PageInfo(CommonListFragment.class.getName(), null));
        pages.add(new PageInfo(CommonListFragment.class.getName(), null));
        return new PagerAdapter(getChildFragmentManager(), pages, titles);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_multi_scroll;
    }

    @Override
    public void initUi() {
        super.initUi();
        initBanner();
    }

    private void initBanner() {
        View mFocusImageRoot = find(R.id.banner);
        mFocusPager = (ViewPager) mFocusImageRoot.findViewById(R.id.pager_focus);
        mFocusIndicator = (CirclePageIndicator) mFocusImageRoot.findViewById(R.id.indicator_dot);
        mFocusIndicator.setVisibility(View.INVISIBLE);
        mFocusPager.setPageMargin(16);
        mFocusPager.setOffscreenPageLimit(1);
        mFocusPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                if (position < -1) {
//                    page.setAlpha(0.5f);
                    page.setRotationY(10);
                    page.setScaleY(1);
                } else if (position <= 1 && position >= -1) {
                    if (position < 0) {
//                        page.setAlpha((position + 1) * 0.5f + 0.5f);
                        page.setRotationY(-position * 10);
                        page.setScaleY(-position * 0.1f + 0.9f);
                    } else {
//                        page.setAlpha(1 - position * 0.5f);
                        page.setRotationY(-position * 10);
                        page.setScaleY(position * 0.1f + 0.9f);
                    }
                } else {
//                    page.setAlpha(0.5f);
                    page.setRotationY(-10);
                    page.setScaleY(1);
                }
            }
        });

        int width = ScreenUtil.getScreenWidth(getActivity());
        int height = (int) (width * (1080 / 1920f));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
        mFocusImageRoot.setLayoutParams(lp);

        FixedSpeedScroller mScroller = new FixedSpeedScroller(mFocusPager.getContext(), new DecelerateInterpolator());
        ViewUtil.setViewPagerScroller(mFocusPager, mScroller);

        mFocusAdapter = new FocusImageAdapter(getActivity(), mFocusImages);
        mFocusAdapter.setCycleScrollFlag(true);
        mFocusPager.setAdapter(mFocusAdapter);
        mFocusIndicator.setViewPager(mFocusPager);
    }

    @Override
    public void loadData() {
        super.loadData();
        mFocusImages.add(new ImageHolder(R.drawable.image1));
        mFocusImages.add(new ImageHolder(R.drawable.image2));
        mFocusImages.add(new ImageHolder(R.drawable.image3));
        mFocusImages.add(new ImageHolder(R.drawable.image4));
        updateFocusImageBar();

        Timer timer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mFocusPager.setCurrentItem(mFocusPager.getCurrentItem() + 1 % mFocusImages.size());
                    }
                });
            }
        };
        timer.schedule(mTask, 5000, 5000);
    }

    private void updateFocusImageBar() {
        mFocusPager.setCurrentItem(0);
        mFocusIndicator.setPagerRealCount(mFocusImages.size());
        mFocusAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTask != null) {
            mTask.cancel();
        }
    }
}

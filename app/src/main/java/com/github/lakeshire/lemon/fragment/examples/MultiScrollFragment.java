package com.github.lakeshire.lemon.fragment.examples;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.adapter.FocusImageAdapter;
import com.github.lakeshire.lemon.adapter.PagerAdapter;
import com.github.lakeshire.lemon.fragment.base.BaseScrollFragment;
import com.github.lakeshire.lemon.model.ImageHolder;
import com.github.lakeshire.lemon.util.FixedSpeedScroller;
import com.github.lakeshire.lemon.util.ScreenUtil;
import com.github.lakeshire.lemon.util.ViewUtil;
import com.github.lakeshire.lemon.view.pageindicator.CirclePageIndicator;

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
        return new PagerAdapter(getChildFragmentManager(), titles, CommonListFragment.class.getName());
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

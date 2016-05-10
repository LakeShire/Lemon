package com.github.lakeshire.lemon.fragment.examples;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.adapter.PagerAdapter;
import com.github.lakeshire.lemon.fragment.base.BasePullFragment;
import com.github.lakeshire.lemon.model.ImageHolder;
import com.github.lakeshire.lemon.view.verticalviewpager.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class VerticalViewPagerFragment extends BasePullFragment {

    @Bind(R.id.view)
    VerticalViewPager mView;

    public String[] titles;
    private PagerAdapter mAdapter;

    protected PagerAdapter getPagerAdapter() {
        titles = new String[1000];
        for (int i = 0; i < 1000; i++) {
            titles[i] = "page " + (i+1);
        }
        return new PagerAdapter(getChildFragmentManager(), titles, SimpleContentFragment.class.getName());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_vertical_viewpager;
    }

    @Override
    public void initUi() {

        List<ImageHolder> mFocusImages = new ArrayList<>();
        mFocusImages.add(new ImageHolder(R.drawable.image1));
        mFocusImages.add(new ImageHolder(R.drawable.image2));
        mFocusImages.add(new ImageHolder(R.drawable.image3));
        mFocusImages.add(new ImageHolder(R.drawable.image4));

        mAdapter = getPagerAdapter();
        mView.setOffScreenLimit(1);
        mView.setAdapter(mAdapter);
    }
}

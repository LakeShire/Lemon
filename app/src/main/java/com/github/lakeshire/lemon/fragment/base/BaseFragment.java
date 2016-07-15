package com.github.lakeshire.lemon.fragment.base;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.lakeshire.lemon.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by nali on 2016/4/19.
 */
public abstract class BaseFragment extends Fragment {

    protected ViewGroup mContainerView;
    private List<ImageView> mImageViews = new ArrayList<>();
    private List<Bitmap> mBitmaps = new ArrayList<>();

    public void startFragment(Class<?> clazz) {
        ((BaseActivity) getActivity()).startFragment(clazz);
    }

    public void endFragment() {
        ((BaseActivity) getActivity()).endFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContainerView = (ViewGroup) inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, mContainerView);
        initUi();
        return mContainerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    public void loadData() {

    }

    public void initUi() {

    }

    protected void l(String log) {
        Log.i(this.getClass().getSimpleName(), log);
    }

    public abstract int getLayoutId();

    protected View find(int res) {
        View view = mContainerView.findViewById(res);
        if (view instanceof ImageView) {
            mImageViews.add((ImageView) view);
        }
        return view;
    }

    public boolean onBackPressed() {
        endFragment();
        return true;
    }

    @Override
    public void onDestroy() {
        for (ImageView iv : mImageViews) {
            iv.setImageBitmap(null);
        }
        super.onDestroy();
    }
}

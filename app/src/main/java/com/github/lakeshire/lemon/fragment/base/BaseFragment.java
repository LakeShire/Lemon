package com.github.lakeshire.lemon.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lakeshire.lemon.activity.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by nali on 2016/4/19.
 */
public abstract class BaseFragment extends Fragment {

    protected View mContainerView;

    public void startFragment(Class<?> clazz) {
        ((BaseActivity) getActivity()).startFragment(clazz);
    }

    public void endFragment() {
        ((BaseActivity) getActivity()).endFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContainerView = inflater.inflate(getLayoutId(), container, false);
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
        return mContainerView.findViewById(res);
    }
}

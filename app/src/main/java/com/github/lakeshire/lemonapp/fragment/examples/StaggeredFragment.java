package com.github.lakeshire.lemonapp.fragment.examples;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.github.lakeshire.lemon.fragment.base.BasePullFragment;
import com.github.lakeshire.lemonapp.R;
import com.github.lakeshire.recyclerview.StaggeredHomeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nali on 2016/4/25.
 */
public class StaggeredFragment extends BasePullFragment {
    private RecyclerView mRecyclerView;
    private StaggeredHomeAdapter mStaggeredHomeAdapter;
    private List<String> mDatas = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_staggered;
    }

    @Override
    public void initUi() {
        super.initUi();

        mRecyclerView = (RecyclerView) find(R.id.id_recyclerview);

        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }

        mStaggeredHomeAdapter = new StaggeredHomeAdapter(getActivity(), mDatas);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mStaggeredHomeAdapter);
        // 设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        initEvent();
    }

    private void initEvent() {
        mStaggeredHomeAdapter.setOnItemClickLitener(new StaggeredHomeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), position + " click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getActivity(), position + " long click", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void loadData() {
//        mDatas = new ArrayList<String>();
//        for (int i = 'A'; i < 'z'; i++) {
//            mDatas.add("" + (char) i);
//        }
//        mStaggeredHomeAdapter.notifyDataSetChanged();
    }
}

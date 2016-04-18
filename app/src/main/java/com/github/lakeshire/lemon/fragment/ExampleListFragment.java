package com.github.lakeshire.lemon.fragment;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.adapter.BaseAdapter;
import com.github.lakeshire.lemon.adapter.ViewHolder;
import com.github.lakeshire.lemon.fragment.base.BaseFragment;
import com.github.lakeshire.lemon.fragment.examples.AnimatePieFragment;
import com.github.lakeshire.lemon.fragment.examples.RunFragment;
import com.github.lakeshire.lemon.fragment.examples.TestFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by nali on 2016/4/18.
 */
public class ExampleListFragment extends BaseFragment {

    @Bind(R.id.list)
    ListView mLvExample;
    private ArrayList<ExampleModel> mDatas = new ArrayList<>();
    private ExampleAdapter mAdapter;

    @Override
    public void initUi() {
        mAdapter = new ExampleAdapter(getActivity(), mDatas, R.layout.item_example);
        mLvExample.setAdapter(mAdapter);
        mLvExample.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExampleModel model = (ExampleModel) parent.getAdapter().getItem(position);
                startFragment(model.clazz);
            }
        });
    }

    @Override
    public void loadData() {
        mDatas.clear();
        mDatas.add(new ExampleModel("动效饼图", AnimatePieFragment.class));
        mDatas.add(new ExampleModel("Test", TestFragment.class));
        mDatas.add(new ExampleModel("Run", RunFragment.class));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public int getLayoutId() { return R.layout.fragment_list; }

    class ExampleModel {
        public ExampleModel(String name, Class<?> clazz) {
            this.name = name;
            this.clazz = clazz;
        }
        String name;
        Class<?> clazz;
    }

    class ExampleAdapter extends BaseAdapter<ExampleModel> {

        public ExampleAdapter(Context mContext, List<ExampleModel> mListData, int mLayoutResId) {
            super(mContext, mListData, mLayoutResId);
        }

        @Override
        public void bindViewData(ViewHolder viewHolder, ExampleModel item, int position) {
            viewHolder.setText(R.id.tv_title, item.name);
        }
    }
}

package com.github.lakeshire.lemon.fragment;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.adapter.BaseAdapter;
import com.github.lakeshire.lemon.adapter.ViewHolder;
import com.github.lakeshire.lemon.fragment.base.BasePullFragment;
import com.github.lakeshire.lemon.fragment.examples.AnimatePieFragment;
import com.github.lakeshire.lemon.fragment.examples.CircleProgressFragment;
import com.github.lakeshire.lemon.fragment.examples.MoreListViewFragment;
import com.github.lakeshire.lemon.fragment.examples.MultiScrollFragment;
import com.github.lakeshire.lemon.fragment.examples.PagerFragment;
import com.github.lakeshire.lemon.fragment.examples.PathMenuFragment;
import com.github.lakeshire.lemon.fragment.examples.PullToZoomFragment;
import com.github.lakeshire.lemon.fragment.examples.SelectorFragment;
import com.github.lakeshire.lemon.fragment.examples.SlideListViewFragment;
import com.github.lakeshire.lemon.fragment.examples.SlidingLayoutFragment;
import com.github.lakeshire.lemon.fragment.examples.SwitchButtonFragment;
import com.github.lakeshire.lemon.fragment.examples.TagViewFragment;
import com.github.lakeshire.lemon.view.pulltofresh.EnhancePtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ExampleListFragment extends BasePullFragment {

    @Bind(R.id.list)
    ListView mLvExample;
    private ArrayList<ExampleModel> mDatas = new ArrayList<>();
    private ExampleAdapter mAdapter;

    @Override
    public void initUi() {
        super.initUi();
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
        super.loadData();
        mDatas.clear();
        mDatas.add(new ExampleModel("动效饼图", AnimatePieFragment.class));
        mDatas.add(new ExampleModel("可横向滑动的宽列表", SlideListViewFragment.class));
        mDatas.add(new ExampleModel("隐藏额外按钮的列表", MoreListViewFragment.class));
        mDatas.add(new ExampleModel("仿豌豆荚的详情页", PagerFragment.class));
        mDatas.add(new ExampleModel("Path经典动画菜单", PathMenuFragment.class));
        mDatas.add(new ExampleModel("标签", TagViewFragment.class));
        mDatas.add(new ExampleModel("Selector注入器", SelectorFragment.class));
        mDatas.add(new ExampleModel("SwitchButton", SwitchButtonFragment.class));
        mDatas.add(new ExampleModel("环形进度条", CircleProgressFragment.class));
        mDatas.add(new ExampleModel("SlidingLayout", SlidingLayoutFragment.class));
        mDatas.add(new ExampleModel("嵌套滑动页面", MultiScrollFragment.class));
        mDatas.add(new ExampleModel("下拉缩放头部", PullToZoomFragment.class));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public int getLayoutId() { return R.layout.fragment_example_list; }

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

    @Override
    protected boolean checkCanRefresh(EnhancePtrFrameLayout frame, View content, View header) {
        ListView absListView = mLvExample;
        boolean canRefresh =  !(absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop()));
        l("canRefresh: " + canRefresh);
        return canRefresh;
    }

    /**
     * 主Fragment必须重写这个
     * @return
     */
    @Override
    public boolean onBackPressed() {
        return false;
    }
}

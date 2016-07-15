package com.github.lakeshire.lemon.fragment.examples;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.adapter.BaseAdapter;
import com.github.lakeshire.lemon.adapter.ViewHolder;
import com.github.lakeshire.lemon.fragment.base.BasePullFragment;
import com.github.lakeshire.lemon.util.BitmapUtil;
import com.github.lakeshire.lemon.view.BlurableImageView;
import com.github.lakeshire.lemon.view.pulltofresh.EnhancePtrFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

/**
 *
 * 这个页面展示了通用容器CommonAdapter的使用
 * 方便的使用适配器，根据模型类型不同使用不同的布局
 *
 */
public class StringListFragment extends BasePullFragment {

    private StringAdapter mAdapter;
    private ArrayList<DemoModel> data = new ArrayList();

    @Bind(R.id.list)
    ListView listView;
    private int mFocusItem;
    private int mMaxLevel;
    private int position;
    private boolean scrolling = false;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_string_list;
    }

    @Override
    public void initUi() {
//        super.initUi();
        BlurableImageView biv = (BlurableImageView) find(R.id.image);
        Bitmap bitmap = BitmapUtil.reduce(getContext(), R.drawable.image4, 256, 256);
        biv.blur(new BitmapDrawable(getActivity().getResources(), bitmap), "blur", true);

        mAdapter = new StringAdapter(getActivity(), data, R.layout.item_example);
        listView.setAdapter(mAdapter);
        listView.setDivider(null);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_TOUCH_SCROLL:
                    case SCROLL_STATE_FLING:
                        scrolling = true;
                        break;
                    case SCROLL_STATE_IDLE:
                        scrolling = false;
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount % 2 == 0) {
                    //  偶数项
                    int focus = firstVisibleItem + visibleItemCount / 2;
                    mAdapter.setMaxLevel(visibleItemCount / 2);
                    for (int i = firstVisibleItem; i < focus; i++) {
                        data.get(i).level = i - firstVisibleItem;
                        data.get(i).position = i - firstVisibleItem;
                    }
                    for (int i = focus; i < firstVisibleItem + visibleItemCount; i++) {
                        data.get(i).level = focus - i + visibleItemCount / 2;
                        data.get(i).position = i - firstVisibleItem;
                    }
                } else {
                    //  奇数项
                    int focus = firstVisibleItem + visibleItemCount / 2 + 1;
                    mAdapter.setMaxLevel(visibleItemCount / 2 + 1);
                    for (int i = firstVisibleItem; i < focus; i++) {
                        data.get(i).level = i - firstVisibleItem;
                        data.get(i).position = i - firstVisibleItem;
                    }
                    for (int i = focus; i < firstVisibleItem + visibleItemCount; i++) {
                        data.get(i).level = focus - i + visibleItemCount / 2 + 1;
                        data.get(i).position = i - firstVisibleItem;
                    }
                }

                mAdapter.notifyDataSetChanged();
            }
        });


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!scrolling) {
                                listView.smoothScrollBy(listView.getHeight() / 13, 1000);
//                                listView.setSelection(position);
//                                listView.smoothScrollToPosition(position);
//                                position++;
                            }
                        }
                    });
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 500, 2000);
    }


    @Override
    public void loadData() {
        super.loadData();
        data.clear();
        data.add(new DemoModel("还没好好的感受"));
        data.add(new DemoModel("雪花绽放的气候"));
        data.add(new DemoModel("我们一起颤抖"));
        data.add(new DemoModel("会更明白什么是温柔"));
        data.add(new DemoModel("还没跟你牵着手"));
        data.add(new DemoModel("走过荒芜的沙丘"));
        data.add(new DemoModel("可能从此以后学会珍惜"));
        data.add(new DemoModel("天长和地久"));
        data.add(new DemoModel("有时候 有时候"));
        data.add(new DemoModel("相聚离开都有时候"));
        data.add(new DemoModel("没有什么会永垂不朽"));
        data.add(new DemoModel("可是我有时候"));
        data.add(new DemoModel("宁愿选择留恋不放手"));
        data.add(new DemoModel("等到风景都看透"));
        data.add(new DemoModel("也许你会陪我看细水长流"));
        data.add(new DemoModel("还没为你把红豆"));
        data.add(new DemoModel("熬成缠绵的伤口"));
        data.add(new DemoModel("然后一起分享"));
        data.add(new DemoModel("会更明白相思的哀愁"));
        data.add(new DemoModel("还没好好的感受"));
        data.add(new DemoModel("醒着亲吻的温柔"));
        data.add(new DemoModel("可能在我左右"));
        data.add(new DemoModel("你才追求孤独的自由"));
        data.add(new DemoModel("有时候 有时候"));
        data.add(new DemoModel("我会相信一切有尽头"));
        data.add(new DemoModel("相聚离开都有时候"));
        data.add(new DemoModel("没有什么会永垂不朽"));
        data.add(new DemoModel("可是我有时候"));
        data.add(new DemoModel("宁愿选择留恋不放手"));
        data.add(new DemoModel("等到风景都看透"));
        data.add(new DemoModel("也许你会陪我看细水长流"));
        data.add(new DemoModel("有时候 有时候"));
        data.add(new DemoModel("我会相信一切有尽头"));
        data.add(new DemoModel("相聚离开都有时候"));
        data.add(new DemoModel("没有什么会永垂不朽"));
        data.add(new DemoModel("可是我有时候"));
        data.add(new DemoModel("宁愿选择留恋不放手"));
        data.add(new DemoModel("等到风景都看透"));
        data.add(new DemoModel("也许你会陪我看细水长流"));
        data.add(new DemoModel(""));
        data.add(new DemoModel(""));
        data.add(new DemoModel(""));
        data.add(new DemoModel(""));
        data.add(new DemoModel(""));
        data.add(new DemoModel(""));
        mAdapter.notifyDataSetChanged();
    }

    class DemoModel {
        public String content;
        public boolean current = false;
        public int level = 1;
        public int position = 0;
        public DemoModel(String content) {
            this.content = content;
        }
    }

    class StringAdapter extends BaseAdapter<DemoModel> {

        private float gap = -1;
        private float rotateGap = -1;

        private int[] sizes = {10, 11, 12, 13, 14, 16, 20, 24};
        private int level = 0;

        public StringAdapter(Context mContext, List<DemoModel> mListData, int mLayoutResId) {
            super(mContext, mListData, mLayoutResId);
        }

        @Override
        public void bindViewData(ViewHolder viewHolder, DemoModel item, int position) {

            TextView tvTitle = viewHolder.getItemView(R.id.tv_title);
            tvTitle.setTextSize(sizes[item.level]);
            tvTitle.setTextColor(getResources().getColor(R.color.white));
            tvTitle.setText(item.content);
            if (gap != -1) {
                tvTitle.setAlpha(gap * item.level);
//                tvTitle.setRotationX(rotateGap * (level - item.position));
            }
        }

        public void setMaxLevel(int level) {
            this.level = level;
            gap = 1f / level;
            rotateGap = 60f / level;
        }
    }

    @Override
    protected boolean checkCanRefresh(EnhancePtrFrameLayout frame, View content, View header) {
        ListView absListView = listView;
        return !(absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop()));
    }
}
